//
// Written by Su386.
// See LICENSE for copyright and license notices.
//


package me.partlysanestudios.partlysaneskies.features.discord.webhooks

import gg.essential.elementa.ElementaVersion
import gg.essential.elementa.WindowScreen
import gg.essential.elementa.components.ScrollComponent
import gg.essential.elementa.components.UIBlock
import gg.essential.elementa.components.UIWrappedText
import gg.essential.elementa.constraints.CenterConstraint
import gg.essential.elementa.dsl.*
import gg.essential.universal.UMatrixStack
import me.partlysanestudios.partlysaneskies.PartlySaneSkies.Companion.config
import me.partlysanestudios.partlysaneskies.PartlySaneSkies.Companion.minecraft
import me.partlysanestudios.partlysaneskies.commands.PSSCommand
import me.partlysanestudios.partlysaneskies.config.psconfig.Config
import me.partlysanestudios.partlysaneskies.config.psconfig.Toggle
import me.partlysanestudios.partlysaneskies.config.psconfig.Toggle.Companion.asToggle
import me.partlysanestudios.partlysaneskies.features.themes.ThemeManager
import me.partlysanestudios.partlysaneskies.render.gui.components.PSSToggle
import me.partlysanestudios.partlysaneskies.render.gui.constraints.ScaledPixelConstraint.Companion.scaledPixels
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
        x = 15.scaledPixels
        y = 10.scaledPixels
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
        x = 15.scaledPixels
        y = 10.scaledPixels
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

    private val webhookOptionsList = ScrollComponent().constrain {
        x = CenterConstraint()
        y = CenterConstraint()
        width = 100.percent
        height = 100.percent
    } childOf webhookOptionsPanel

    private val webhookOptionsHeader = UIWrappedText().constrain {
        x = 15.scaledPixels
        y = 10.scaledPixels
        width = 100.percent
        textScale = 1.5.scaledPixels
    } childOf webhookOptionsList

    private val sideSwitchButton = ThemeManager.currentBackgroundUIImage.constrain {
        x = CenterConstraint()
        y = CenterConstraint() - (300 / 2).scaledPixels + 20.scaledPixels
        width = 50.scaledPixels
        height = 50.scaledPixels
    }.onMouseClick {
        if (selectedIcon == null) {
            return@onMouseClick
        }

        selectedIcon?.enabled = !(selectedIcon?.enabled ?: false)
        updateLocations()
    } childOf window

    private var webhookIcons = ArrayList<WebhookIcon>()
    private var selectedIcon: WebhookIcon? = null

    private val activeParameters = ArrayList<WebhookOption>()

    init {
        addAllRegisteredWebhooks()
        updateLocations()
    }

    private fun addAllRegisteredWebhooks() {
        for (event in WebhookEventManager.webhookEvents) {
            val icon = WebhookIcon(event)
            icon.menu = this
            webhookIcons.add(icon)
        }
    }

    private fun updateSelected() {
        updateParameters()

        for (icon in webhookIcons) {
            icon.toggle.setState(false)
        }

        selectedIcon?.toggle?.setState(true)

        if (selectedIcon != null) {
            webhookOptionsHeader.setText("§e${selectedIcon?.webhookEvent?.name ?: ""} Options:")
        }
    }

    private fun updateParameters() {
        val selectedConfig = selectedIcon?.webhookEvent?.config
        if (selectedConfig == null) {
            for (param in activeParameters) {
                param.parameterBlock.hide(true)
                param.textComponent.hide(true)
                param.toggle.hide(true)
                webhookOptionsHeader.setText("")
            }
            activeParameters.clear()
        } else {
            val startX = 3.0

            var x = startX
            var y = 30
            val width = 20
            for (key in selectedConfig.getAllOptions().keys) {

                if (x + width > 100) {
                    y += 30
                    x = startX
                }

                if (selectedConfig.getAllOptions()[key] !is Toggle) {
                    continue
                }

                val option = WebhookOption(selectedConfig, key)
                option.menu = this
                option.parameterBlock.constrain {
                    this.x = x.percent
                    this.y = y.scaledPixels
                    this.width = width.percent
                    height = 20.scaledPixels
                    color = Color(0, 0, 0, 0).constraint
                }.setChildOf(webhookOptionsList)

                activeParameters.add(option)


                x += 85.0 / 3.0 + 3.1
            }
        }
    }
    private fun updateLocations() {
        val enabled = ArrayList<WebhookIcon>()
        val disabled = ArrayList<WebhookIcon>()
        for (icon in webhookIcons) {
            if (icon.webhookEvent.hidden && !config.showHiddenWebhooks) {
                continue
            }

            if (icon.enabled) {
                enabled.add(icon)
            } else {
                disabled.add(icon)
            }
        }

        val startX = 10.0

        var x = startX
        var y = 30.0
        val width = 30
        val height = 30
        val pad = 7.5

        for (icon in disabled) {
            icon.iconBox
                .setX(x.scaledPixels)
                .setY(y.scaledPixels)
                .setWidth(width.scaledPixels)
                .setHeight(height.scaledPixels)
                .setChildOf(inactiveWebhooksPanel)

            x += width + pad
            if (x + width > 300) {
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

        for (parameter in activeParameters) {
            parameter.syncToggle()
        }
    }


    override fun onDrawScreen(matrixStack: UMatrixStack, mouseX: Int, mouseY: Int, partialTicks: Float) {
        super.onDrawScreen(matrixStack, mouseX, mouseY, partialTicks)

        for (icon in webhookIcons) {
            if (icon.hovering && icon.text.isNotEmpty()) {
                drawHoveringText(icon.text, mouseX, mouseY)
                break
            }
        }

        for (option in activeParameters) {
            if (option.hovering && option.text.isNotEmpty()) {
                drawHoveringText(option.text, mouseX, mouseY)
                break
            }
        }
    }

    private class WebhookIcon(val webhookEvent: Webhook) {
        var hovering = false
        var menu: WebhookMenu? = null
        var text = arrayListOf("§d${webhookEvent.name}")
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
            menu?.updateSelected()
        }

        val toggle = PSSToggle()
            .setHeight(100.percent)
            .setWidth(100.percent)
            .setX(CenterConstraint())
            .setY(CenterConstraint())
            .setChildOf(iconBox)

        init {
            webhookEvent.icon.constrain {
                x = CenterConstraint()
                y = CenterConstraint()
                height = 100.percent
                width = 100.percent
            }
            webhookEvent.icon.setChildOf(toggle.component)
        }
    }

    private class WebhookOption(val config: Config, val optionPath: String) {
        var hovering = false
        var menu: WebhookMenu? = null

        val parameterBlock = UIBlock().onMouseEnter {
            hovering = true
        }.onMouseLeave {
            hovering = false
        }

        val toggle = PSSToggle()
            .setX(0.percent)
            .setY(0.percent)
            .setHeight(100.percent)
            .setWidth(20.scaledPixels)
            .setChildOf(parameterBlock)

        val textComponent = UIWrappedText(centered = false).constrain {
            x = 0.pixels(alignOpposite = true)
            y = CenterConstraint()
            textScale = 1.scaledPixels
            width = 70.percent

        } childOf parameterBlock

        val text: MutableList<String> get() {
            val list = arrayListOf<String>()

            for (line in (config.find(optionPath)?.asToggle?.description ?: "").split("\n")) {
                list.add("§7$line")
            }
            return list
        }

        init {
            parameterBlock.onMouseClickConsumer {
                toggle.toggleState()
                config.find(optionPath)?.asToggle?.state = toggle.getState()
                menu?.updateLocations()
            }

            toggle.setState(config.find(optionPath)?.asToggle?.state ?: false)

            textComponent.setText("§7${config.find(optionPath)?.asToggle?.name ?: "(Unknown)"}" )
        }

        fun syncToggle() {
            toggle.setState(config.find(optionPath)?.asToggle?.state ?: false)
        }
    }
}