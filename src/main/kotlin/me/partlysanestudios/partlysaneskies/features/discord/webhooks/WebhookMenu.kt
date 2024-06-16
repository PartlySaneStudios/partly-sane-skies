package me.partlysanestudios.partlysaneskies.features.discord.webhooks

import gg.essential.elementa.ElementaVersion
import gg.essential.elementa.WindowScreen
import gg.essential.elementa.components.UIBlock
import gg.essential.elementa.components.UIWrappedText
import gg.essential.elementa.constraints.CenterConstraint
import gg.essential.elementa.dsl.*
import gg.essential.universal.UMatrixStack
import me.partlysanestudios.partlysaneskies.PartlySaneSkies.Companion.minecraft
import me.partlysanestudios.partlysaneskies.commands.PSSCommand
import me.partlysanestudios.partlysaneskies.features.themes.ThemeManager
import me.partlysanestudios.partlysaneskies.render.gui.constraints.ScaledPixelConstraint.Companion.scaledPixels
import me.partlysanestudios.partlysaneskies.utils.ChatUtils.sendClientMessage
import java.awt.Color

class WebhookMenu: WindowScreen(ElementaVersion.V5) {

    companion object {
        fun registerWebhookCommand() {

            PSSCommand("webhook")
                .addAlias("webhooks", "wh", "discordwebhook", "discordwebhooks")
                .setRunnable { _, _ ->
                    Thread() {
                        minecraft.addScheduledTask { minecraft.displayGuiScreen(WebhookMenu()) }
                    }.start()
                }
                .setDescription("Opens the menu to edit webhook events")
                .register()
        }
    }


    private val inactiveWebhooksPanel = ThemeManager.currentBackgroundUIImage.constrain {
        x = CenterConstraint()  - (300 / 2).scaledPixels - (75 / 2).scaledPixels
        y = CenterConstraint() - 20.scaledPixels - (150 / 2).scaledPixels
        width = 300.scaledPixels
        height = 350.scaledPixels
    } childOf window
    private val inactiveWebhookText = UIWrappedText("Inactive Webhooks:").constrain {
        x = 7.scaledPixels
        y = 7.scaledPixels
        width = (300 - (7 * 2)).scaledPixels
        textScale = 1.5.scaledPixels
        color = Color.red.constraint
    } childOf inactiveWebhooksPanel

    private val activeWebhooksPanel  = ThemeManager.currentBackgroundUIImage.constrain {
        x = CenterConstraint() + (300 / 2).scaledPixels + (75 / 2).scaledPixels
        y = CenterConstraint() - 20.scaledPixels - (150 / 2).scaledPixels
        width = 300.scaledPixels
        height = 350.scaledPixels
    } childOf window

    private val activeWebhookText = UIWrappedText("Active Webhooks:").constrain {
        x = 7.scaledPixels
        y = 7.scaledPixels
        width = (300 - (7 * 2)).scaledPixels
        textScale = 1.5.scaledPixels
        color = Color.green.constraint
    } childOf activeWebhooksPanel

    private val webhookOptionsPanel = ThemeManager.currentBackgroundUIImage.constrain {
        x = CenterConstraint()
        y = CenterConstraint() + 20.scaledPixels + (300 / 2).scaledPixels
        width = (300 + 300 + 75).scaledPixels
        height = 150.scaledPixels
    } childOf window

    private val sideSwitchButton = ThemeManager.currentBackgroundUIImage.constrain {
        x = CenterConstraint()
        y = CenterConstraint()
        width = 50.scaledPixels
        height = 50.scaledPixels
    }.onMouseClick {
        if (selectedIcon == null) {
            return@onMouseClick
        }

    } childOf window

    private var webhookIcons = ArrayList<WebhookIcon>()
    var selectedIcon: WebhookIcon? = null

    init {
        addAllRegisteredWebhooks()
        updateLocations()
    }

    private fun addAllRegisteredWebhooks() {
        sendClientMessage(WebhookEventManager.webhookEvents.size.toString())
        for (event in WebhookEventManager.webhookEvents) {
            sendClientMessage("Adding webhook")
            val icon = WebhookIcon(event)
            icon.menu = this
            webhookIcons.add(icon)
        }
    }

    private fun updateLocations() {
        val enabled = ArrayList<WebhookIcon>()
        val disabled = ArrayList<WebhookIcon>()
        for (icon in webhookIcons) {
            if (icon.enabled) {
                enabled.add(icon)
            } else {
                disabled.add(icon)
            }
        }

        val startX = 10.0

        var x = startX
        var y = 25.0
        val width = 22.5
        val height = 22.5
        val pad = 7.5

        for (icon in disabled) {
            icon.iconBox
                .setX(x.scaledPixels)
                .setY(y.scaledPixels)
                .setWidth(width.scaledPixels)
                .setHeight(height.scaledPixels)
                .setChildOf(inactiveWebhooksPanel)

            x += width + pad
            if (x > 300) {
                x = startX
                y += height + pad
            }
        }

        x = startX
        y = 25.0
        for (icon in enabled) {
            icon.iconBox
                .setX(x.scaledPixels)
                .setY(y.scaledPixels)
                .setWidth(width.scaledPixels)
                .setHeight(height.scaledPixels)
                .setChildOf(activeWebhooksPanel)

            x += width + pad
            if (x > 300) {
                x = startX
                y += height + pad
            }
        }
    }


    override fun onDrawScreen(matrixStack: UMatrixStack, mouseX: Int, mouseY: Int, partialTicks: Float) {
        super.onDrawScreen(matrixStack, mouseX, mouseY, partialTicks)

        for (icon in webhookIcons) {
            if (icon.hovering) {
                drawHoveringText(icon.text, mouseX, mouseY)
                break
            }
        }
    }

    class WebhookIcon(private val webhookEvent: WebhookEvent) {
        var hovering = false
        var menu: WebhookMenu? = null
        var text = arrayListOf("Test")
        var enabled: Boolean get() {
            return webhookEvent.enabled
        } set(value) {
            webhookEvent.enabled = value
        }
        val iconBox = UIBlock().constrain {

        }.onMouseEnter {
            hovering = true
        }.onMouseLeave {
            hovering = false
        }.onMouseClick {
            if (menu?.selectedIcon == this@WebhookIcon) {
                menu?.selectedIcon = null
            } else {
                menu?.selectedIcon = this@WebhookIcon
            }
        }
        init {
            webhookEvent.icon.setChildOf(iconBox)
        }
    }
}