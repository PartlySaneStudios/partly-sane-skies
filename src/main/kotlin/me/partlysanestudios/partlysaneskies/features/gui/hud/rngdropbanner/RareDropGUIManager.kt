package me.partlysanestudios.partlysaneskies.features.gui.hud.rngdropbanner

import me.partlysanestudios.partlysaneskies.PartlySaneSkies.Companion.minecraft
import me.partlysanestudios.partlysaneskies.commands.PSSCommand
import net.minecraft.command.ICommandSender

object RareDropGUIManager {

    fun registerCommand() {
        PSSCommand("raredrop")
            .addAlias("rd")
            .setDescription("Opens the Rare Drop GUI")
            .setRunnable { _: ICommandSender?, _: Array<String> ->
                openGui()
            }
            .register()
    }

    private fun openGui() {
        RareDropGUI.populateGui()
        Thread {
            minecraft.addScheduledTask {
                minecraft.displayGuiScreen(RareDropGUI)
            }
        }.start()
    }
}