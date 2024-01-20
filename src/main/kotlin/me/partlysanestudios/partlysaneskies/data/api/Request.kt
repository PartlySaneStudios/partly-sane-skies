//
// Written by Su386.
// See LICENSE for copyright and license notices.
//


package me.partlysanestudios.partlysaneskies.data.api

import me.partlysanestudios.partlysaneskies.PartlySaneSkies
import me.partlysanestudios.partlysaneskies.utils.ChatUtils.sendClientMessage
import me.partlysanestudios.partlysaneskies.utils.SystemUtils.log
import org.apache.logging.log4j.Level
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import javax.net.ssl.HttpsURLConnection
import java.net.URL

import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager
import java.security.cert.X509Certificate
import javax.net.ssl.HostnameVerifier

/**
 * @param url The requests URL
 * @param function The function that will activate when the response is finished. [RequestRunnable]s take the instance of the request as a parameter
 * @param inMainThread If true, the request will execute while in the main thread of the client freezing it and stopping anything else from happening while awaiting a response. Only really useful in specific cases.
 * @param executeOnNextFrame If true, the runnable will execute on the next frame of the main thread. Useful for when modifying things with the client
 * @param acceptAllCertificates If true, the request will accept all certificates for response
 * @author Su386
 */
open class Request(
    private val url: URL,
    private val function: RequestRunnable?,
    private val inMainThread:
    Boolean = false,
    private val executeOnNextFrame: Boolean = false,
    private val acceptAllCertificates: Boolean = false
) {
//    Constructor with string url and certificate option
    constructor(
        url: String,
        function: RequestRunnable?,
        inMainThread: Boolean = false,
        executeOnNextFrame: Boolean = false,
        acceptAllCertificates: Boolean = false
    ): this(URL(url), function, inMainThread, executeOnNextFrame, acceptAllCertificates)

//    Constructor without certificate option
    @Deprecated("Use constructor with acceptAllCertificates option")
    constructor(
        url: URL,
        function: RequestRunnable?,
        inMainThread: Boolean = false,
        executeOnNextFrame: Boolean = false
    ): this(url, function, inMainThread, executeOnNextFrame, false)

//    Constructor without certificates option
    @Deprecated("Use constructor with acceptAllCertificates option")
    constructor(
        url: String,
        function: RequestRunnable?,
        inMainThread: Boolean = false,
        executeOnNextFrame: Boolean = false
    ): this(URL(url), function, inMainThread, executeOnNextFrame, false)

    // A string that contains the response message (not body)
    private var responseMessage = ""
    // An int that contains the response code
    private var responseCode = -1
    // A boolean to determining if there was an unknown failure
    // Gets set to true when setFailed(reason) is called
    private var hasFailed = false
    // The String that contains the JSON response
    private var response = ""
    // A list that contains all the HTTP codes where the request should rerun
    private var tryAgainOnCodes = ArrayList<Int>()


    /**
     * Flags the request as failed
     *
     * @param reason optional failure message
     */
    fun setFailed(reason: String = "") {
        if (reason != "") {

            this.responseMessage = reason
        }
        this.hasFailed = true
    }

    /**
     * @return if the request has failed
     */
    fun hasSucceeded(): Boolean {
        return if (this.responseCode != HttpsURLConnection.HTTP_OK) {
            false
        } else !hasFailed
    }


    /**
     * Sends the get request
     */
    @Throws(IOException::class)
    open fun startRequest() {
        // Opens a new connection with the url
        val httpURLConnection = url.openConnection() as HttpsURLConnection
        // Sets the browser as Mozilla to bypass an insecure restrictions
        httpURLConnection.setRequestProperty("User-Agent", "Partly-Sane-Skies/" + PartlySaneSkies.VERSION)
        if (acceptAllCertificates) {
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
            httpURLConnection.sslSocketFactory = sslContext.socketFactory

            // Optional: Apply a hostname verifier that does not perform any checks (not recommended)
            httpURLConnection.hostnameVerifier = HostnameVerifier { _, _ -> true }
        }

        // Gets the response code
        val responseCode = httpURLConnection.getResponseCode()
        this.responseCode = responseCode
        // Gets the response message
        this.responseMessage = httpURLConnection.getResponseMessage()

        // If the code is not HTTP_OK -- if the request failed
        if (responseCode != HttpsURLConnection.HTTP_OK) {
            if (tryAgainOnCodes.contains(responseCode)) {
                RequestsManager.newRequest(this)
                return
            }

            // If the print API errors setting is on, send a message to the client
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
            URL: ${url}
            """.trimIndent()
            )
            // Disconnect the connection
            httpURLConnection.disconnect()
        }

        // Read the response as a string
        val `in` = BufferedReader(InputStreamReader(httpURLConnection.inputStream))
        var inputLine: String?
        val response = StringBuilder()
        while (`in`.readLine().also { inputLine = it } != null) {
            response.append(inputLine)
        }
        `in`.close()

        // set the requestResponse to the string that was read as a response
        this.response = response.toString()

        // Disconnect
        httpURLConnection.disconnect()
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

    /**
     * @return the request runnable
     */
    fun getWhatToRunWhenFinished(): RequestRunnable? {
        return function
    }

    /**
     * @return the request url
     */
    fun getURL(): URL {
        return url
    }

    /**
     * Adds codes to the try again list. Whenever the response encounters one of these codes, it will send a new request.
     *
     * @param responseCode the code to add
     * @return an instance of itself for easy chaining
     */
    fun addTryAgainResponse(responseCode: Int): Request {
        tryAgainOnCodes.add(responseCode)
        return this
    }

    /**
     * @return a string with the response, or an empty json object if the response is empty.
     */
    open fun getResponse(): String {
        if (this.response.isEmpty()) {
            return "{}"
        }
        return this.response
    }

    /**
     * @return if the request will run in the main thread
     */
    fun isMainThread(): Boolean {
        return inMainThread
    }

    /**
     * @return if the RequestManager will run the RequestRunnable in the main thread on the next frame
     */
    fun isRunNextFrame(): Boolean {
        return executeOnNextFrame
    }

    /**
     * @return the error message and response code
     */
    fun getErrorMessage(): String {
        return "Error: " + this.responseMessage + ":" + this.responseCode
    }



}