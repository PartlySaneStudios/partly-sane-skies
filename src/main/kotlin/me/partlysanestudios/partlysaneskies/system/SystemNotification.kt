package me.partlysanestudios.partlysaneskies.system

import java.awt.*
import javax.swing.ImageIcon
import javax.swing.JOptionPane

object SystemNotification {

    private const val NOTIFICATION_TITLE = "PartlySaneSkies"
    private val tray: SystemTray? = if (SystemTray.isSupported()) SystemTray.getSystemTray() else null
    private val iconImage: ImageIcon = loadImageIcon("/assets/partlysaneskies/textures/logo.png")

    fun showNotification(text: String) {
        if (tray != null) {
            showNativeNotification(text)
        } else {
            showJOptionPaneBackup(text)
        }
    }

    private fun showNativeNotification(text: String) {
        try {
            val trayIcon = TrayIcon(iconImage.image, NOTIFICATION_TITLE)
            trayIcon.isImageAutoSize = true
            trayIcon.toolTip = text
            tray?.add(trayIcon)
            trayIcon.displayMessage(NOTIFICATION_TITLE, text, TrayIcon.MessageType.INFO)
        } catch (e: Exception) {
            e.printStackTrace()
            showJOptionPaneBackup(text)
        }
    }

    private fun showJOptionPaneBackup(text: String) {
        JOptionPane.showMessageDialog(null, text, NOTIFICATION_TITLE, JOptionPane.INFORMATION_MESSAGE)
    }

    private fun loadImageIcon(path: String): ImageIcon {
        val url = SystemNotification::class.java.getResource(path)
        return if (url != null) {
            ImageIcon(url)
        } else {
            throw IllegalArgumentException("Image not found: $path")
        }
    }
}