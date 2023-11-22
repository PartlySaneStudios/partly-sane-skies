//
// Written by Su386.
// See LICENSE for copyright and license notices.
//


package me.partlysanestudios.partlysaneskies.utils

import com.google.gson.JsonElement
import com.google.gson.JsonObject
import me.partlysanestudios.partlysaneskies.PartlySaneSkies
import me.partlysanestudios.partlysaneskies.utils.ChatUtils.sendClientMessage
import org.apache.logging.log4j.Level
import java.awt.Toolkit
import java.awt.datatransfer.DataFlavor
import java.awt.datatransfer.Transferable
import java.awt.datatransfer.UnsupportedFlavorException
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URI
import java.net.URISyntaxException
import java.net.URL

object SystemUtils {

    fun log(level: Level?, message: String) {
        for (line in message.split("\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()) {
            PartlySaneSkies.LOGGER.log(level, line)
        }
    }
    fun copyStringToClipboard(string: String) {
        Toolkit.getDefaultToolkit().systemClipboard.setContents(getTransferableString(string), null)
    }
    private fun getTransferableString(string: String): Transferable {
        return object : Transferable {
            override fun getTransferDataFlavors(): Array<DataFlavor> {
                return arrayOf(DataFlavor.stringFlavor)
            }

            override fun isDataFlavorSupported(flavor: DataFlavor): Boolean {
                return DataFlavor.stringFlavor.equals(flavor)
            }

            @Throws(UnsupportedFlavorException::class)
            override fun getTransferData(flavor: DataFlavor): Any {
                if (DataFlavor.stringFlavor.equals(flavor)) {
                    return string
                }
                throw UnsupportedFlavorException(flavor)
            }
        }
    }


    @Deprecated("") // Deprecated: Use RequestManager and Requests instead
    @Throws(IOException::class)
    fun getRequest(urlString: String): String {
        val url = URL(urlString)
        val httpURLConnection = url.openConnection() as HttpURLConnection
        httpURLConnection.setRequestProperty("User-Agent", "Partly-Sane-Skies/" + PartlySaneSkies.VERSION)
        val responseCode = httpURLConnection.getResponseCode()
        return if (responseCode == HttpURLConnection.HTTP_OK) { // success
            val `in` = BufferedReader(InputStreamReader(httpURLConnection.inputStream))
            var inputLine: String?
            val response = StringBuilder()
            while (`in`.readLine().also { inputLine = it } != null) {
                response.append(inputLine)
            }
            `in`.close()
            httpURLConnection.disconnect()
            response.toString()
        } else {
            if (PartlySaneSkies.config.printApiErrors) {
                sendClientMessage(
                    """
                        Error: ${httpURLConnection.getResponseMessage()}:${httpURLConnection.getResponseCode()}
                        Contact PSS admins for more information
                        """.trimIndent()
                )
            } else {
                log(
                    Level.ERROR,
                    """
                        Error: ${httpURLConnection.getResponseMessage()}:${httpURLConnection.getResponseCode()}
                        Contact PSS admins for more information
                        """.trimIndent()
                )
            }
            log(
                Level.ERROR,
                """
                    Error: ${httpURLConnection.getResponseMessage()}:${httpURLConnection.getResponseCode()}
                    URL: $urlString
                    """.trimIndent()
            )
            httpURLConnection.disconnect()
            "Error$responseCode"
        }
    }


    // Opens a link with a given URL
    fun openLink(url: String?) {
        val uri: URI
        try {
            uri = URI(url)
            try {
                val oclass = Class.forName("java.awt.Desktop")
                val `object` = oclass.getMethod("getDesktop", *arrayOfNulls(0)).invoke(null)
                oclass.getMethod("browse", *arrayOf<Class<*>>(URI::class.java)).invoke(`object`, uri)
            } catch (throwable: Throwable) {
                sendClientMessage("Couldn't open link")
                throwable.printStackTrace()
            }
        } catch (except: URISyntaxException) {
            sendClientMessage("Couldn't open link")
            except.printStackTrace()
        }
    }

    //    Gets the json element from a path string in format /key/key/key/key/
    fun JsonObject.getJsonFromPath(path: String): JsonElement? {
        val splitPath = path.split("/".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        var obj = this
        // Gets the object up until the very last one
        for (i in 0 until splitPath.size - 1) {
            if (splitPath[i].isEmpty()) {
                continue
            }
            obj = obj.getAsJsonObject(splitPath[i])
            if (obj == null) {
                return null
            }
        }


//        Gets the last object as a JsonElement
        return obj[splitPath[splitPath.size - 1]]
    }
}