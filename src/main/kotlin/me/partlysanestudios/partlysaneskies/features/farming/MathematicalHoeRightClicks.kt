//
// Written by Su386.
// See LICENSE for copyright and license notices.
//
package me.partlysanestudios.partlysaneskies.features.farming

import com.google.gson.JsonParser
import me.partlysanestudios.partlysaneskies.PartlySaneSkies
import me.partlysanestudios.partlysaneskies.PartlySaneSkies.Companion.config
import me.partlysanestudios.partlysaneskies.PartlySaneSkies.Companion.minecraft
import me.partlysanestudios.partlysaneskies.PartlySaneSkies.Companion.time
import me.partlysanestudios.partlysaneskies.commands.PSSCommand
import me.partlysanestudios.partlysaneskies.data.pssdata.PublicDataManager.getFile
import me.partlysanestudios.partlysaneskies.events.SubscribePSSEvent
import me.partlysanestudios.partlysaneskies.events.data.LoadPublicDataEvent
import me.partlysanestudios.partlysaneskies.utils.HypixelUtils.getItemId
import me.partlysanestudios.partlysaneskies.utils.MathUtils.onCooldown
import net.minecraft.command.ICommandSender
import net.minecraft.event.ClickEvent
import net.minecraft.util.ChatComponentText
import net.minecraft.util.IChatComponent
import net.minecraftforge.event.entity.player.PlayerInteractEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

object MathematicalHoeRightClicks {

    private var lastMessageSendTime: Long = 0
    var lastAllowHoeRightClickTime: Long = 0
    private val hoes = ArrayList<String>()

    @SubscribePSSEvent
    fun loadHoes(event: LoadPublicDataEvent?) {
        val str = getFile("constants/mathematical_hoes.json")
        val array = JsonParser().parse(str).getAsJsonObject()["hoes"].getAsJsonArray()
        hoes.clear()
        for (i in 0 until array.size()) {
            val hoe = array[i].asString
            hoes.add(hoe)
        }
    }

    @SubscribeEvent
    fun onClick(event: PlayerInteractEvent) {
        if (!config.blockHoeRightClicks) {
            return
        }
        if (event.action == PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK || event.action == PlayerInteractEvent.Action.RIGHT_CLICK_AIR) {
            if (onCooldown(lastAllowHoeRightClickTime, (config.allowRightClickTime * 60L * 1000L).toLong())) {
                return
            }
            if (!isHoldingHoe) {
                return
            }
            if (!onCooldown(lastMessageSendTime, 3000)) {
                val message: IChatComponent = ChatComponentText(
                    """${PartlySaneSkies.CHAT_PREFIX}§8Right Clicks are disabled while holding a Mathematical Hoe
§7Click this message or run /allowhoerightclick to allow right clicks for ${config.allowRightClickTime} minutes."""
                )
                message.chatStyle.setChatClickEvent(ClickEvent(ClickEvent.Action.RUN_COMMAND, "/allowhoerightclick"))
                minecraft.ingameGUI.chatGUI.printChatMessage(message)
                lastMessageSendTime = time
            }
            event.setCanceled(true)
        }
    }


    private val isHoldingHoe: Boolean
        get() {
            if (hoes == null) {
                return false
            }
            if (minecraft.thePlayer == null) {
                return false
            }
            val heldItem = minecraft.thePlayer.heldItem ?: return false
            return hoes.contains(heldItem.getItemId())
        }

    fun registerCommand() {
        PSSCommand("allowhoerightclick")
            .addAlias("allowhoerightclicks")
            .addAlias("ahrc")
            .setDescription("Allows hoe right clicks for a few minutes")
            .setRunnable { s: ICommandSender, a: Array<String> ->
                val canRightClickHoe =
                    onCooldown(lastAllowHoeRightClickTime, (config.allowRightClickTime * 60L * 1000L).toLong())

                lastAllowHoeRightClickTime = if (canRightClickHoe) {
                    val message: IChatComponent =
                        ChatComponentText("${PartlySaneSkies.CHAT_PREFIX}§bThe ability to right-click with a hoe has been §cdisabled§b again.\n§dClick this message or run /allowhoerightclick to allow right-clicks for ${config.allowRightClickTime} again.")
                    message.chatStyle.setChatClickEvent(
                        ClickEvent(
                            ClickEvent.Action.RUN_COMMAND,
                            "/allowhoerightclick"
                        )
                    )

                    minecraft.ingameGUI.chatGUI.printChatMessage(message)

                    0
                } else {
                    val message: IChatComponent =
                        ChatComponentText("${PartlySaneSkies.CHAT_PREFIX}§bThe ability to right-click with a hoe has been §aenabled§b for ${config.allowRightClickTime} minutes.\n§dClick this message or run /allowhoerightclick to disable right-clicks again.")
                    message.chatStyle.setChatClickEvent(
                        ClickEvent(
                            ClickEvent.Action.RUN_COMMAND,
                            "/allowhoerightclick"
                        )
                    )

                    minecraft.ingameGUI.chatGUI.printChatMessage(message)

                    time
                }
            }
            .register()
    }

}
