/*
 * A Kotlin class written by Erymanthus[#5074] | (u/)RayDeeUx
 * for Su386 and FlagMaster's Partly Sane Skies mod.
 * See LICENSE for copyright and license notices.
 *
 * KOTLIN ON TOP BABYYYYYYYY
 *
 * Skirtdungoner — Today at 10:41 AM
 * /checksanity <input
 * > Partly Sane Skies > <input> has xxx% sanity.
 * Or smth idk
 *
*/

package me.partlysanestudios.partlysaneskies.features.misc

import com.google.gson.JsonObject
import com.google.gson.JsonParser
import me.partlysanestudios.partlysaneskies.PartlySaneSkies
import me.partlysanestudios.partlysaneskies.commands.PSSCommand
import me.partlysanestudios.partlysaneskies.data.pssdata.PublicDataManager
import me.partlysanestudios.partlysaneskies.utils.ChatUtils
import me.partlysanestudios.partlysaneskies.utils.SkyCryptUtils
import me.partlysanestudios.partlysaneskies.utils.SystemUtils.getJsonFromPath
import net.minecraft.command.ICommandSender
import java.util.regex.Pattern

object SanityCheck {

    private val playerName: String by lazy { PartlySaneSkies.minecraft.thePlayer.name }
    private val sanityCheckPath: String = "constants/sanity_check_data.json"
    private val usernameRegex: String = "^[a-zA-Z0-9_]{2,16}\$"
    private val usernamePattern: Pattern = Pattern.compile(usernameRegex)

    fun registerCommand() {
        PSSCommand("sanitycheck")
            .addAlias("/checksanity")
            .addAlias("/psssanity")
            .addAlias("/pssinsanity")
            .addAlias("/pssinsane")
            .addAlias("/psssane")
            .setDescription("Checks for one's sanity. This command is purely for fun; do not take its results seriously.")
            .setRunnable { s: ICommandSender, a: Array<String> ->
                ChatUtils.sendClientMessage("Attempting to begin sanity analysis...")
                Thread() {
                    var username = playerName
                    if (a.size > 1) {
                        ChatUtils.sendClientMessage("Correct Usage: /sanitycheck {username}")
                        return@Thread
                    } else if (a.size == 1) {
                        username = a[0]
                    }
                    if (validateUsernameByRegex(username)) {
                        val sanityCheckDataJsonObject: JsonObject = JsonParser().parse(PublicDataManager.getFile(sanityCheckPath)).getAsJsonObject()
                        val highestSkyblockNetworth: Double = sanityCheckDataJsonObject.getJsonFromPath("highestnwlong")?.asLong?.toDouble() ?: 360567766418.0
                        val oldestSkyblockFirstJoin: Long = sanityCheckDataJsonObject.getJsonFromPath("oldestprofileunixlong")?.asLong ?: 1560276201428
                        val currentProfileNetworth: Double = SkyCryptUtils.getSkyCryptNetworth(username)
                        val currentProfileFirstJoin: Long = SkyCryptUtils.getFirstJoinEpoch(username)

                        if (currentProfileNetworth != -1.0 && currentProfileFirstJoin != -1L) {
                            val networthRatio = 1.0 - (currentProfileNetworth / highestSkyblockNetworth)
                            val firstJoinRatio = 1.0 - (currentProfileFirstJoin.toDouble() / oldestSkyblockFirstJoin.toDouble())
                            ChatUtils.sendClientMessage("§a${if (username != playerName) "$username is" else "You are"} ${(networthRatio * 100) + (firstJoinRatio * 100)}% insane.")
                        } else {
                            ChatUtils.sendClientMessage("§eIt appears that $username does not qualify for a PSS sanity check, due to current API circumstances. Try again later, or report this to us via §9/pssdiscord §eif this issue persists.")
                        }
                    } else {
                        ChatUtils.sendClientMessage("§cPlease enter a valid Minecraft username to perform a §9/sanitycheck §con.")
                        return@Thread
                    }
                }.start()
            }.register()
    }

    private fun validateUsernameByRegex(username: String): Boolean {
        return usernamePattern.matcher(username).find()
    }

}