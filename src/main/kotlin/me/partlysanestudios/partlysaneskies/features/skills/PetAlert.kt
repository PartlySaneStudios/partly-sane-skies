//
// Written by Su386.
// See LICENSE for copyright and license notices.
//

package me.partlysanestudios.partlysaneskies.features.skills

import gg.essential.elementa.UIComponent
import gg.essential.elementa.components.UIBlock
import gg.essential.elementa.components.UIWrappedText
import gg.essential.elementa.constraints.CenterConstraint
import gg.essential.elementa.dsl.childOf
import gg.essential.elementa.dsl.constrain
import gg.essential.elementa.dsl.constraint
import gg.essential.elementa.dsl.percent
import me.partlysanestudios.partlysaneskies.PartlySaneSkies
import me.partlysanestudios.partlysaneskies.PartlySaneSkies.Companion.config
import me.partlysanestudios.partlysaneskies.PartlySaneSkies.Companion.minecraft
import me.partlysanestudios.partlysaneskies.PartlySaneSkies.Companion.time
import me.partlysanestudios.partlysaneskies.commands.PSSCommand
import me.partlysanestudios.partlysaneskies.data.cache.PetData.getCurrentPetLevel
import me.partlysanestudios.partlysaneskies.data.cache.PetData.getCurrentPetName
import me.partlysanestudios.partlysaneskies.data.cache.PetData.getCurrentPetRarity
import me.partlysanestudios.partlysaneskies.data.skyblockdata.Rarity
import me.partlysanestudios.partlysaneskies.features.gui.SidePanel
import me.partlysanestudios.partlysaneskies.render.gui.constraints.ScaledPixelConstraint.Companion.scaledPixels
import me.partlysanestudios.partlysaneskies.utils.ChatUtils.sendClientMessage
import me.partlysanestudios.partlysaneskies.utils.ElementaUtils.applyBackground
import me.partlysanestudios.partlysaneskies.utils.HypixelUtils.getItemId
import me.partlysanestudios.partlysaneskies.utils.HypixelUtils.isSkyblock
import me.partlysanestudios.partlysaneskies.utils.MathUtils.onCooldown
import me.partlysanestudios.partlysaneskies.utils.MinecraftUtils.containerInventory
import me.partlysanestudios.partlysaneskies.utils.StringUtils.pluralize
import me.partlysanestudios.partlysaneskies.utils.StringUtils.removeColorCodes
import net.minecraft.client.audio.PositionedSoundRecord
import net.minecraft.client.gui.inventory.GuiChest
import net.minecraft.client.gui.inventory.GuiContainer
import net.minecraft.event.ClickEvent
import net.minecraft.util.ChatComponentText
import net.minecraft.util.ResourceLocation
import net.minecraftforge.client.event.GuiScreenEvent
import java.awt.Color

object PetAlert : SidePanel() {
    override val panelBaseComponent: UIComponent =
        UIBlock().applyBackground().constrain {
            x = 800.scaledPixels
            y = CenterConstraint()
            width = 175.scaledPixels
            height = 100.scaledPixels
            color = Color(0, 0, 0, 0).constraint
        }

    private val textComponent =
        UIWrappedText(centered = true).constrain {
            x = CenterConstraint()
            y = CenterConstraint()
            width = 95.percent
            textScale = 1.scaledPixels
        } childOf panelBaseComponent

    override fun onPanelRender(event: GuiScreenEvent.BackgroundDrawnEvent) {
        alignPanel()
        var currentlySelectedPetName = getCurrentPetName()
        if (currentlySelectedPetName.isEmpty()) {
            currentlySelectedPetName = "§8(Unknown)"
        }

        val petColorCode =
            if (config.selectedPet.isEmpty()) {
                "§d"
            } else if (currentlySelectedPetName.equals(config.selectedPet, ignoreCase = true)) {
                "§a"
            } else {
                "§c"
            }

        var petLevel = ""
        if (getCurrentPetLevel() != -1) {
            petLevel = "[Lvl${getCurrentPetLevel()}] "
        }

        var petRarity = ""
        if (getCurrentPetRarity() !== Rarity.UNKNOWN) {
            petRarity = getCurrentPetRarity().displayName + " "
        }

        val textString =
            """
            §eCurrently Selected Pet:
            $petColorCode$petLevel$petRarity$currentlySelectedPetName
            
            §eDesired Pet:
            §d${config.selectedPet}
            """.trimIndent()
        textComponent.setText(textString)
    }

    private var lastMessageSendTime: Long = 0
    private var lastSoundTime: Long = 0
    private var lastMuteTime: Long = 0

    fun runPetAlertTick() {
        if (!config.incorrectPetForMinionAlert) {
            return
        }

        if (!isMinionGui()) {
            return
        }

        var petName = getCurrentPetName()
        if (petName.isEmpty()) {
            petName = "§8(Unknown)"
        }

        val selectedPetName = config.selectedPet

        if (petName.equals(selectedPetName, ignoreCase = true)) {
            return
        }

        if (onCooldown(lastMuteTime, (config.petAlertMuteTime * 60L * 1000L).toLong())) {
            return
        }

        if (!onCooldown(lastSoundTime, 750)) {
            minecraft.soundHandler.playSound(PositionedSoundRecord.create(ResourceLocation("partlysaneskies", "bell")))

            if (config.incorrectPetForMinionAlertSiren) {
                minecraft.soundHandler.playSound(
                    PositionedSoundRecord.create(
                        ResourceLocation(
                            "partlysaneskies",
                            "airraidsiren",
                        ),
                    ),
                )
            }

            lastSoundTime = time
        }

        if (!onCooldown(lastMessageSendTime, 3000)) {
            val message =
                ChatComponentText(
                    "${PartlySaneSkies.CHAT_PREFIX}§cYOU CURRENTLY HAVE $petName§c SELECTED AS YOUR PET. YOU WANTED TO UPGRADE $selectedPetName.\n§dClick this message or run /mutepetalert to mute the alert for ${config.petAlertMuteTime} ${
                        "minutes".pluralize(config.petAlertMuteTime)
                    }.",
                )
            message.chatStyle.setChatClickEvent(ClickEvent(ClickEvent.Action.RUN_COMMAND, "/mutepetalert"))
            minecraft.ingameGUI.chatGUI.printChatMessage(message)
            lastMessageSendTime = time
        }
    }

    private fun isMinionGui(): Boolean {
        if (minecraft.currentScreen !is GuiChest) {
            return false
        }

        val upper = (minecraft.currentScreen as GuiChest).containerInventory
        val inventoryNameMatches =
            upper.displayName.formattedText
                .removeColorCodes()
                .contains("Minion")
        if (!inventoryNameMatches) {
            return false
        }

//        Gets the slot where the minion head is supposed to be
        val minionHeadSlot = upper.getStackInSlot(4) ?: return false
        val displayName = minionHeadSlot.displayName.removeColorCodes()
        return displayName.contains("Minion")
    }

    fun favouritePet() {
        if (!isSkyblock()) {
            return
        }

        if (!isPetGui()) {
            return
        }

        val container = minecraft.currentScreen as GuiContainer
        val slot = container.slotUnderMouse ?: return
        val item = slot.stack

        if (item.getItemId().isEmpty()) {
            return
        }

        val petName = parsePetNameFromItem(item.displayName)
        config.selectedPet = petName
        sendClientMessage("Set $petName as your favorite pet.")
        config.save()
    }

    fun registerCommand() {
        PSSCommand("mutepetalert")
            .setDescription("Mutes the pet alert for a set amount of minutes.")
            .setRunnable {
                sendClientMessage(
                    "§bPet alert has been muted for ${config.petAlertMuteTime} ${"minute".pluralize(config.petAlertMuteTime)}.",
                )
                lastMuteTime = time
            }.register()
    }

    override fun shouldDisplayPanel(): Boolean {
        if (!config.selectedPetInformation) {
            return false
        }

        if (minecraft.currentScreen !is GuiChest) {
            return false
        }

        return isMinionGui()
    }

    private fun isPetGui(): Boolean {
        if (minecraft.currentScreen !is GuiChest) {
            return false
        }

        val upper = (minecraft.currentScreen as GuiChest).containerInventory
        return upper.displayName.formattedText
            .removeColorCodes()
            .contains("Pets")
    }

    private fun parsePetNameFromItem(name: String): String {
        val fixedName = name.removeColorCodes()
        val petNameStartIndex =
            fixedName.indexOf("] ") + 2 // Finds the start of the pet name. Ex: "[Lv100] Su386's *Black Cat"
        return fixedName.substring(petNameStartIndex)
    }
}
