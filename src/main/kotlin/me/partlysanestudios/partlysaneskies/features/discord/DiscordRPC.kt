//
// Written by Su386 and from https://github.com/JnCrMx/discord-game-sdk4j/blob/master/examples.
// See LICENSE for copyright and license notices.
//


package me.partlysanestudios.partlysaneskies.features.discord

import de.jcm.discordgamesdk.Core
import de.jcm.discordgamesdk.CreateParams
import de.jcm.discordgamesdk.activity.Activity
import de.jcm.discordgamesdk.activity.ActivityType
import me.partlysanestudios.partlysaneskies.PartlySaneSkies
import me.partlysanestudios.partlysaneskies.PartlySaneSkies.Companion.config
import me.partlysanestudios.partlysaneskies.utils.HypixelUtils
import me.partlysanestudios.partlysaneskies.utils.SystemUtils
import org.apache.logging.log4j.Level
import java.io.File
import java.io.IOException
import java.net.URL
import java.nio.file.Files
import java.security.cert.X509Certificate
import java.time.Instant
import java.util.*
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream
import javax.net.ssl.*


const val NORMAL_APPLICATION_ID = 1195613263845666849
const val SBE_BAD_APPLICATION_ID = 1195625408167686175

object DiscordRPC {
//    private var discordLibraryPath: String = "./config/partly-sane-skies/discord-native-library"
    private var discordLibrary: File? = null
    private var sdkDownloaded = false
    private var lastName = "sbe bad"
    private var lastMessage = "Playing Hypixel Skyblock"
    private var startTimeStamp = Instant.now()
    fun init() {
        startTimeStamp = Instant.now()
        discordLibrary = downloadDiscordLibrary()
        if(discordLibrary == null) {
            SystemUtils.log(Level.ERROR, "Error downloading Discord SDK.")
            sdkDownloaded = false
            return
        } else {
            sdkDownloaded = true
        }
        // Initialize the Core
        Core.init(discordLibrary)

        while (true) {
            if (!shouldRun()) {
                try {
                    // Sleep a bit to save CPU
                    Thread.sleep(600)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
            } else {
                SystemUtils.log(Level.INFO, "Creating new discord RPC parameters")
                try {
                    run()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                try {
                    // Sleep a bit to save CPU
                    Thread.sleep(600)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun shouldRun(): Boolean {
        if (!config.discordRPC) { // if disabled, return false
            return false
        }

        return if (config.discordRPCOnlySkyblock) { // if true, check if it's in skyblock// if not in skyblock return true
            // if in skyblock, return true
            HypixelUtils.isSkyblock()

        } else { // if the rpc in enabled, but the only in skyblock is disabled, return true
            true
        }
    }

        fun run() {
        // Set parameters for the Core
        CreateParams().use { params ->
            val sbeBadMode = config.discordPlayingMode == 1
            val applicationId = if (sbeBadMode) {
                SBE_BAD_APPLICATION_ID
            } else {
                NORMAL_APPLICATION_ID
            }
            params.clientID = applicationId
            params.flags = CreateParams.getDefaultFlags()
            Core(params).use { core ->
                try {
                    val startActivity = createNewActivity()
                    core.activityManager().updateActivity(startActivity)
                } catch (e: Exception) {
                    e.printStackTrace()
                } finally {

                }


                // Run callbacks forever
                while (true) {
                    // If it should run or if the mode has changed, return so the run function can be called again with the right application id
                    if (!shouldRun() || (config.discordPlayingMode == 1) != sbeBadMode) {
                        core.close()
                        return@run
                    }

                    try {
                        core.runCallbacks()

                        if (config.discordRPCName != lastName || config.discordRPCDescription != lastMessage) {
                            lastName = config.discordRPCName
                            lastMessage = config.discordRPCDescription

                            val activity = createNewActivity()
                            core.activityManager().updateActivity(activity)
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    } finally {

                    }


                    try {
                        // Sleep a bit to save CPU
                        Thread.sleep(50)
                    } catch (e: InterruptedException) {
                        e.printStackTrace()
                    }
                }
            }
        }
    }

    private fun createNewActivity(): Activity {
        Activity().use { activity ->
            activity.setDetails(lastName)
            activity.setState(lastMessage)
            activity.timestamps().start = startTimeStamp
            activity.assets().largeImage = "large_logo"
            activity.assets().smallImage = "small_logo"
            activity.assets().largeText = "Partly Sane Skies by Partly Sane Studios"
            activity.type = ActivityType.PLAYING

            return activity
        }
    }




    @Throws(IOException::class)
    fun downloadDiscordLibrary(): File? {
        // Find out which name Discord's library has (.dll for Windows, .so for Linux)
        val name = "discord_game_sdk"
        val suffix: String
        val osName = System.getProperty("os.name").lowercase(Locale.ROOT)
        var arch = System.getProperty("os.arch").lowercase(Locale.ROOT)
        suffix = if (osName.contains("windows")) {
            ".dll"
        } else if (osName.contains("linux")) {
            ".so"
        } else if (osName.contains("mac os")) {
            ".dylib"
        } else {
            throw RuntimeException("cannot determine OS type: $osName")
        }

        /*
        * Some systems report "amd64" (e.g. Windows and Linux), some "x86_64" (e.g. Mac OS).
        * At this point we need the "x86_64" version, as this one is used in the ZIP.
        */
        if (arch == "amd64") arch = "x86_64"

        // Path of Discord's library inside the ZIP
        val zipPath = "lib/$arch/$name$suffix"

        val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager {
            override fun getAcceptedIssuers(): Array<X509Certificate> {
                return arrayOf()
            }

            override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) {
            }

            override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) {
            }
        })

        // Install the all-trusting trust manager
        val sslContext = SSLContext.getInstance("SSL")
        sslContext.init(null, trustAllCerts, java.security.SecureRandom())


        // Create an all-trusting host name verifier
        val allHostsValid = HostnameVerifier { _, _ -> true }


        // Open the URL as a ZipInputStream
        val downloadUrl = URL("https://dl-game-sdk.discordapp.net/2.5.6/discord_game_sdk.zip")
        val connection: HttpsURLConnection = downloadUrl.openConnection() as HttpsURLConnection
        connection.setRequestProperty("User-Agent", "discord-game-sdk4j (https://github.com/JnCrMx/discord-game-sdk4j)")
        val zin = ZipInputStream(connection.inputStream)

        connection.hostnameVerifier = allHostsValid
        connection.sslSocketFactory = sslContext.socketFactory
        // Search for the right file inside the ZIP
        var entry: ZipEntry
        while (zin.getNextEntry().also { entry = it } != null) {
            if (entry.name == zipPath) {
                // Create a new temporary directory
                // We need to do this, because we may not change the filename on Windows
                val tempDir = File(System.getProperty("java.io.tmpdir"), "java-" + name + System.nanoTime())
                if (!tempDir.mkdir()) throw IOException("Cannot create temporary directory")
                tempDir.deleteOnExit()

                // Create a temporary file inside our directory (with a "normal" name)
                val temp = File(tempDir, name + suffix)
                temp.deleteOnExit()

                // Copy the file in the ZIP to our temporary file
                Files.copy(zin, temp.toPath())

                // We are done, so close the input stream
                zin.close()

                // Return our temporary file
                return temp
            }
            // next entry
            zin.closeEntry()
        }
        zin.close()
        // We couldn't find the library inside the ZIP
        return null
    }
}