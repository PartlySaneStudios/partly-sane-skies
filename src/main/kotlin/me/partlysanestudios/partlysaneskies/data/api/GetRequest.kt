package me.partlysanestudios.partlysaneskies.data.api

import me.partlysanestudios.partlysaneskies.PartlySaneSkies
import me.partlysanestudios.partlysaneskies.utils.ChatUtils.sendClientMessage
import me.partlysanestudios.partlysaneskies.utils.SystemUtils.log
import org.apache.logging.log4j.Level
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import javax.net.ssl.HttpsURLConnection
import java.net.URL

import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager
import java.security.cert.X509Certificate
import javax.net.ssl.HostnameVerifier

class GetRequest(
    url: URL,
    function: RequestRunnable?,
    inMainThread: Boolean = false,
    executeOnNextFrame: Boolean = false,
    acceptAllCertificates: Boolean = false
) : Request(url, function, inMainThread, executeOnNextFrame, acceptAllCertificates) {
    constructor(
        url: String,
        function: RequestRunnable?,
        inMainThread: Boolean = false,
        executeOnNextFrame: Boolean = false,
        acceptAllCertificates: Boolean = false
    ): this(URL(url), function, inMainThread, executeOnNextFrame, acceptAllCertificates)

    //    Constructor without certificate option
    constructor(
        url: URL,
        function: RequestRunnable?,
        inMainThread: Boolean = false,
        executeOnNextFrame: Boolean = false
    ): this(url, function, inMainThread, executeOnNextFrame, false)

    //    Constructor without certificates option
    constructor(
        url: String,
        function: RequestRunnable?,
        inMainThread: Boolean = false,
        executeOnNextFrame: Boolean = false
    ): this(URL(url), function, inMainThread, executeOnNextFrame, false)
    override fun startRequest() {
        // Opens a new connection with the url
        val connection = url.openConnection() as HttpURLConnection
        // Sets the browser as Mozilla to bypass an insecure restrictions
        connection.setRequestProperty("User-Agent", "Partly-Sane-Skies/" + PartlySaneSkies.VERSION)
        if (acceptAllCertificates && connection is HttpsURLConnection) {
            // Create a trust manager that does not validate certificate chains
            val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager {
                override fun getAcceptedIssuers(): Array<X509Certificate> = arrayOf()
                override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) {}
                override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) {}
            })

            // Install the all-trusting trust manager for a specific SSL context
            val sslContext = SSLContext.getInstance("SSL")
            sslContext.init(null, trustAllCerts, java.security.SecureRandom())

            // Apply the all-trusting SSLSocketFactory to this specific connection
            connection.sslSocketFactory = sslContext.socketFactory

            // Optional: Apply a hostname verifier that does not perform any checks (not recommended)
            connection.hostnameVerifier = HostnameVerifier { _, _ -> true }
        }

        // Gets the response code
        val responseCode = connection.getResponseCode()
        this.responseCode = responseCode
        // Gets the response message
        this.responseMessage = connection.getResponseMessage()

        // If the code is not HTTP_OK -- if the request failed
        if (responseCode !in 200..299) {
            if (tryAgainOnCodes.contains(responseCode)) {
                RequestsManager.newRequest(this)
                return
            }

            // If the print API errors setting is on, send a message to the client
            if (PartlySaneSkies.config.printApiErrors) {
                sendClientMessage(
                    """
                Error: ${connection.getResponseMessage()}:${connection.getResponseCode()}
                Contact PSS admins for more information
                """.trimIndent()
                )
            } else {
                log(
                    Level.ERROR,
                    """
                Error: ${connection.getResponseMessage()}:${connection.getResponseCode()}
                Contact PSS admins for more information
                """.trimIndent()
                )
            }
            log(
                Level.ERROR,
                """
            Error: ${connection.getResponseMessage()}:${connection.getResponseCode()}
            URL: ${url}
            """.trimIndent()
            )
            // Disconnect the connection
            connection.disconnect()
        }

        // Read the response as a string
        val `in` = BufferedReader(InputStreamReader(connection.inputStream))
        var inputLine: String?
        val response = StringBuilder()
        while (`in`.readLine().also { inputLine = it } != null) {
            response.append(inputLine)
        }
        `in`.close()

        // set the requestResponse to the string that was read as a response
        this.response = response.toString()

        // Disconnect
        connection.disconnect()
        if (function == null) {
            return
        }

        // If supposed to run in the next frame, run in the next frame
        if (executeOnNextFrame) {
            PartlySaneSkies.minecraft.addScheduledTask { function.run(this) }
            return
        }

        // Runs on current thread
        function.run(this)
    }

}