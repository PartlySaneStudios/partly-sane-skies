//
// Written by Su386 and from https://github.com/JnCrMx/discord-game-sdk4j/blob/master/examples.
// See LICENSE for copyright and license notices.
//

package me.partlysanestudios.partlysaneskies.features.discord

import com.jagrosh.discordipc.IPCClient
import com.jagrosh.discordipc.IPCListener
import com.jagrosh.discordipc.entities.RichPresence
import me.partlysanestudios.partlysaneskies.PartlySaneSkies.Companion.config
import me.partlysanestudios.partlysaneskies.features.debug.DebugKey
import me.partlysanestudios.partlysaneskies.utils.HypixelUtils
import me.partlysanestudios.partlysaneskies.utils.SystemUtils
import org.apache.logging.log4j.Level
import java.time.Instant

object DiscordRPC {
    private const val NORMAL_APPLICATION_ID = 1195613263845666849
    private const val SBE_BAD_APPLICATION_ID = 1195625408167686175

    private var lastName = "sbe bad"
    private var lastMessage = "Playing Hypixel Skyblock"
    private var startTimeStamp = Instant.now()

    fun init() {
        startTimeStamp = Instant.now()

        while (true) {
            if (!shouldRun()) {
                try {
                    // Sleep a bit to save CPU
                    Thread.sleep(600)
                } catch (e: InterruptedException) {
                    if (DebugKey.isDebugMode()) {
                        e.printStackTrace()
                    }
                }
            } else {
                if (DebugKey.isDebugMode()) {
                    SystemUtils.log(Level.INFO, "Creating new discord RPC parameters")
                }
                try {
                    run()
                } catch (e: Exception) {
                    if (DebugKey.isDebugMode()) {
                        e.printStackTrace()
                    }
                }
                try {
                    // Sleep a bit to save CPU
                    Thread.sleep(600)
                } catch (e: InterruptedException) {
                    if (DebugKey.isDebugMode()) {
                        e.printStackTrace()
                    }
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
        val sbeBadMode = config.discordPlayingMode == 1
        val applicationId =
            if (sbeBadMode) {
                SBE_BAD_APPLICATION_ID
            } else {
                NORMAL_APPLICATION_ID
            }
        val client = IPCClient(applicationId)
        client.setListener(
            object : IPCListener {
                override fun onReady(client: IPCClient) {
                    val builder: RichPresence.Builder = buildActivity()

                    client.sendRichPresence(builder.build())
                }
            },
        )
        client.connect()

        while (true) {
            // If it should run or if the mode has changed, return so the run function can be called again with the right application id
            if (!shouldRun() || (config.discordPlayingMode == 1) != sbeBadMode) {
                client.close()
                return@run
            }

            try {
                if (config.discordRPCName != lastName || config.discordRPCDescription != lastMessage) {
                    lastName = config.discordRPCName
                    lastMessage = config.discordRPCDescription

                    val builder = buildActivity()
                    client.sendRichPresence(builder.build())
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

    fun buildActivity(): RichPresence.Builder =
        RichPresence
            .Builder()
            .setState(config.discordRPCName)
            .setDetails(config.discordRPCDescription)
            .setStartTimestamp(startTimeStamp.epochSecond)
            .setLargeImage("large_logo", "")
            .setSmallImage("small_logo", "Partly Sane Skies by Partly Sane Studios")
}
