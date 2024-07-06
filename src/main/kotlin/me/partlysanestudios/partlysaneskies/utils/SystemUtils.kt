//
// Written by Su386.
// See LICENSE for copyright and license notices.
//


package me.partlysanestudios.partlysaneskies.utils

import com.google.gson.JsonElement
import com.google.gson.JsonObject
import me.partlysanestudios.partlysaneskies.PartlySaneSkies
import me.partlysanestudios.partlysaneskies.utils.ChatUtils.sendClientMessage
import net.minecraft.launchwrapper.Launch
import net.minecraft.nbt.CompressedStreamTools
import net.minecraft.nbt.NBTTagCompound
import org.apache.logging.log4j.Level
import java.awt.Toolkit
import java.awt.datatransfer.DataFlavor
import java.awt.datatransfer.Transferable
import java.awt.datatransfer.UnsupportedFlavorException
import java.io.BufferedReader
import java.io.ByteArrayInputStream
import java.io.IOException
import java.io.InputStreamReader
import java.net.*
import java.util.*


object SystemUtils {

    /**
     * Logs a message to the console
     * @param level The level to log the message at
     * @param message The message to log
     */
    fun log(level: Level = Level.INFO, message: String) {
        for (line in message.split("\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()) {
            PartlySaneSkies.LOGGER.log(level, line)
        }
    }

    fun log(message: String) {
        log(Level.INFO, message)
    }

    fun base64ToNbt(base64String: String): NBTTagCompound {
        val bytes = Base64.getDecoder().decode(base64String)
        return CompressedStreamTools.readCompressed(ByteArrayInputStream(bytes))
    }


    /**
     * Copies a string to the clipboard
     * @param string The string to copy
     */
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


    @Deprecated("Use RequestManager and Requests instead")
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


    /**
     * Checks if a string is a valid URL
     */
    fun isValidURL(urlString: String?): Boolean {
        return try {
            // Create a URL object
            URL(urlString)

            // If the URL is created without throwing an exception, it's valid
            true
        } catch (e: MalformedURLException) {
            // MalformedURLException is thrown if the URL is not valid
            false
        }
    }

    /**
     * Opens a link in the default browser
     * @param url The url to open
     */
    fun openLink(url: String?) {
        try {
            val uri: URI = URI(url)
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

    /**
     * Gets the json element from a path string in format /key/key/key/key/
     * @param path The path to get the json element from
     * @return The json element at the path
     */
    fun JsonObject.getJsonFromPath(path: String): JsonElement? {
        val splitPath = path.split("/".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        var obj: JsonObject? = this
        // Gets the object up until the very last one
        for (i in 0 until splitPath.size - 1) {
            if (splitPath[i].isEmpty()) {
                continue
            }
            obj = obj!!.getAsJsonObject(splitPath[i])
            if (obj == null) {
                return null
            }
        }

//        Gets the last object as a JsonElement
        return obj!![splitPath[splitPath.size - 1]]
    }

    /**
     * @author https://www.minecraftforum.net/forums/mapping-and-modding-java-edition/minecraft-mods/modification-development/2423852-detecting-the-development-environment-solved
     * @return True if the environment is a development environment
     */
    fun isDevelopmentEnvironment(): Boolean = (Launch.blackboard["fml.deobfuscatedEnvironment"] as Boolean?) ?: false
}