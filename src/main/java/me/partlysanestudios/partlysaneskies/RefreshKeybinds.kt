/*
 * A Kotlin class written by Erymanthus[#5074] | (u/)RayDeeUx
 * for Su386 and FlagMaster's Partly Sane Skies mod.
 * See LICENSE for copyright and license notices.
 *
 * KOTLIN ON TOP BABYYYYYYYY
 *
 * j10a — Today at 5:03 PM
 * Like for example the party finder page has
 * a reload button, why not be able to reload
 * ui's with the keybinds
 *
 */
package me.partlysanestudios.partlysaneskies

import me.partlysanestudios.partlysaneskies.utils.MinecraftUtils
import net.minecraft.client.gui.inventory.GuiChest
import net.minecraft.inventory.ContainerChest
import net.minecraft.item.ItemStack
import net.minecraftforge.client.event.GuiScreenEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import org.lwjgl.input.Keyboard

class RefreshKeybinds {
    private val operatingSystem: String = System.getProperty("os.name").lowercase()
    private var lastClick = -1L //credit to NEU's WardrobeMouseButtons.kt for this framework

    @SubscribeEvent
    fun onGuiKeyboardInput(event: GuiScreenEvent.KeyboardInputEvent.Pre) {
        if (!PartlySaneSkies.config.refreshKeybind) return
        checkKeybinds(event)
    }

    private fun checkKeybinds(event: GuiScreenEvent) {
        if (!PartlySaneSkies.config.refreshKeybind) return
        val gui: GuiChest = event.gui as? GuiChest ?: return
        val keyFnFiveDown: Boolean = Keyboard.isKeyDown(Keyboard.KEY_F5)
        val keyRDown: Boolean = Keyboard.isKeyDown(Keyboard.KEY_R)
        val refreshKeyDownWindowsLinux: Boolean = ((Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) xor Keyboard.isKeyDown(Keyboard.KEY_RCONTROL)) && keyRDown)
        val refreshKeyDownMacOS: Boolean = ((Keyboard.isKeyDown(Keyboard.KEY_LMETA) xor Keyboard.isKeyDown(Keyboard.KEY_RMETA)) && keyRDown)
        if ((keyFnFiveDown) xor ((refreshKeyDownWindowsLinux && !operatingSystem.contains("mac")) || (refreshKeyDownMacOS && operatingSystem.contains("mac")))) {
            val container: ContainerChest = (gui).inventorySlots as ContainerChest
            for (i: Int in 0..53) {
                val stackItem: ItemStack = container.getSlot(i).stack ?: continue
                if (stackItem.displayName.contains("Refresh") && (System.currentTimeMillis() - lastClick > 300)) {
                    MinecraftUtils.clickOnSlot(i)
                    lastClick = System.currentTimeMillis()
                    event.isCanceled = true
                    break
                }
            }
        }
    }
}