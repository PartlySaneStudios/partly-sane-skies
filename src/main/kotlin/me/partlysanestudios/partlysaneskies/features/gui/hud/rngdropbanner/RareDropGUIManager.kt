package me.partlysanestudios.partlysaneskies.features.gui.hud.rngdropbanner

import cc.polyfrost.oneconfig.utils.gui.GuiUtils
import me.partlysanestudios.partlysaneskies.commands.PSSCommand

object RareDropGUIManager {

    fun registerCommand() {
        PSSCommand("raredrop")
            .addAlias("rd")
            .setDescription("Opens the Rare Drop GUI")
            .setRunnable { _, _ ->
                openGui()
            }
            .register()
    }

    private fun openGui() {
        RareDropGUI.populateGui()
        GuiUtils.displayScreen(RareDropGUI)
    }
}