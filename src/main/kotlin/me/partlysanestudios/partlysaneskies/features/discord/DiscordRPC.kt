package me.partlysanestudios.partlysaneskies.features.discord

import de.jcm.discordgamesdk.Core
import de.jcm.discordgamesdk.CreateParams
import de.jcm.discordgamesdk.activity.Activity
import de.jcm.discordgamesdk.activity.ActivityType
import me.partlysanestudios.partlysaneskies.PartlySaneSkies
import me.partlysanestudios.partlysaneskies.utils.SystemUtils
import org.apache.logging.log4j.Level
import java.io.File
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL
import java.nio.file.Files
import java.time.Instant
import java.util.*
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream


val NORMAL_APPLICATION_ID = 1195613263845666849
val SBE_BAD_APPLICATION_ID = 1195625408167686175

object DiscordRPC {
    var discordLibraryPath: String = "./config/partly-sane-skies/discord-native-library"
    var discordLibrary: File? = null
    var sdkDownloaded = false
    var lastName = "sbe bad"
    var lastMessage = "Playing Hypixel Skyblock"
    var startTimeStamp = Instant.now()
    fun init() {
        if (!PartlySaneSkies.config.discordRPC) {
            return
        }
        startTimeStamp = Instant.now()
        discordLibrary = downloadDiscordLibrary(discordLibraryPath)
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
            SystemUtils.log(Level.INFO, "Creating new discord RPC parameters")
            if (PartlySaneSkies.config?.sbeBadMode == true) {
                run()
            } else {
                run()
            }
            try {
                // Sleep a bit to save CPU
                Thread.sleep(600)
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }
    }

    fun run() {
        // Set parameters for the Core
        CreateParams().use { params ->
            val sbeBadMode = PartlySaneSkies.config?.sbeBadMode?: false
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
                    if (PartlySaneSkies.config?.discordRPC != true) {
                        try {
                            // Sleep a bit to save CPU
                            Thread.sleep(600)
                        } catch (e: InterruptedException) {
                            e.printStackTrace()
                        }
                        continue
                    }
                    // If the mode has changed, return so the run function can be called again with the right application id
                    if ((PartlySaneSkies.config?.sbeBadMode == true) != sbeBadMode) {
                        return
                    }

                    try {
                        core.runCallbacks()

                        if (PartlySaneSkies.config?.discordRPCName != lastName || PartlySaneSkies.config?.discordRPCDescription != lastMessage) {
                            lastName = PartlySaneSkies.config?.discordRPCName?: lastName
                            lastMessage =  PartlySaneSkies.config?.discordRPCDescription?: lastMessage

                            val activity = createNewActivity()
                            core.activityManager().updateActivity(activity)
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    } finally {

                    }


                    try {
                        // Sleep a bit to save CPU
                        Thread.sleep(16)
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



    // From https://github.com/JnCrMx/discord-game-sdk4j/blob/master/examples/DownloadNativeLibrary.java
    @Throws(IOException::class)
    fun downloadDiscordLibrary(path: String): File? {
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
        *At this point we need the "x86_64" version, as this one is used in the ZIP.
        */
        if (arch == "amd64") arch = "x86_64"

        // Path of Discord's library inside the ZIP
        val zipPath = "lib/$arch/$name$suffix"

        // Open the URL as a ZipInputStream
        val downloadUrl = URL("https://dl-game-sdk.discordapp.net/2.5.6/discord_game_sdk.zip")
        val connection: HttpURLConnection = downloadUrl.openConnection() as HttpURLConnection
        connection.setRequestProperty("User-Agent", "discord-game-sdk4j (https://github.com/JnCrMx/discord-game-sdk4j)")
        val zin = ZipInputStream(connection.getInputStream())

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