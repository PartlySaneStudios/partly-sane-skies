//
// Written by Su386.
// See LICENSE for copyright and license notices.
//
package me.partlysanestudios.partlysaneskies.features.commands

import gg.essential.elementa.components.Window.Companion.enqueueRenderOperation
import me.partlysanestudios.partlysaneskies.PartlySaneSkies.Companion.config
import me.partlysanestudios.partlysaneskies.commands.CommandManager
import me.partlysanestudios.partlysaneskies.commands.PSSCommand
import me.partlysanestudios.partlysaneskies.utils.ChatUtils.sendClientMessage
import net.minecraft.command.ICommandSender
import org.lwjgl.input.Keyboard
import java.util.*

object HelpCommand {
    fun registerConfigCommand() {
        PSSCommand("pssconfig")
            .addAlias("pssc")
            .addAlias("pssconf")
            .setDescription("Opens the config menu")
            .setRunnable { _: ICommandSender?, _: Array<String?>? ->
                sendClientMessage("§bOpening config menu...")
                enqueueRenderOperation(Runnable { config.openGui() })
            }
            .register()
    }

    fun registerPSSCommand() {
        PSSCommand("pss")
            .setDescription("Prints help message and opens the config menu")
            .setRunnable { _: ICommandSender?, _: Array<String?>? ->
                printHelpMessage()
                sendClientMessage("§bOpening config menu...")
                enqueueRenderOperation(Runnable { config.openGui() })
            }.register()
    }

    private var configAliases: List<String> = mutableListOf("conf", "c", "config")
    fun registerHelpCommand() {
        PSSCommand("psshelp")
            .addAlias("pssh")
            .addAlias("pshelp")
            .addAlias("helpss")
            .addAlias("helppss")
            .addAlias("psshelp")
            .addAlias("helpihavenoideawhatpartlysaneskiesis")
            .setDescription("Show the Partly Sane Skies help message")
            .setRunnable { _: ICommandSender?, a: Array<String> ->
                if (a.isNotEmpty() && configAliases.contains(a[0].lowercase(Locale.getDefault()))) {
                    sendClientMessage("Opening config GUI...")
                    enqueueRenderOperation(Runnable { config.openGui() })
                    return@setRunnable
                }
                printHelpMessage()
            }.register()
    }

    private val oneConfigKeyBindString: String
        get() {
            if (config.oneConfigKeybind.size == 0) {
                return Keyboard.getKeyName(Keyboard.CHAR_NONE)
            }
            var str = Keyboard.getKeyName(config.oneConfigKeybind.keyBinds[0])
            for (i in 1 until config.oneConfigKeybind.size) {
                str += " + " + Keyboard.getKeyName(config.oneConfigKeybind.keyBinds[i])
            }
            return str
        }

    fun printHelpMessage() {
        val str = StringBuilder(
            """§3§m-----------------------------------------------------§r

§b§l§nWelcome to Partly Sane Skies!§r
Partly Sane Skies is a mod developed by Su386 and FlagMaster. This mod aims to be a quality of life mod for Hypixel SkyBlock.

 §6> Open the config: 
    §6> §ePress $oneConfigKeyBindString or use /pssc
    §6> §eMost features are turned off by default so to use the mod, you will need to configure the settings


 §1> Join the discord
    §1> §9To receive any future updates
    §1> §9/pssdiscord

 §5> Visit the GitHub
    §5> §dAll of the features wouldn't fit in this message, so check out the GitHub to see all of the features.
§3§m-----------------------------------------------------§r
§dCommands:""" // Can't trim indent because it will mess with formatting
        )
        for ((_, command) in CommandManager.commandList) {
            str.append("\n")
            str.append("\n §b> /").append(command.name)
            for (alias in command.aliases) {
                str.append(", /").append(alias)
            }
            str.append("\n§3    > ").append(command.description)
        }
        str.append("\n§3§m-----------------------------------------------------§r")
        sendClientMessage(str.toString(), true)
    }
}
