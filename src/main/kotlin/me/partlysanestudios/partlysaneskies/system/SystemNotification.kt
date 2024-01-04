package me.partlysanestudios.partlysaneskies.system

import java.awt.SystemTray
import java.awt.Toolkit
import java.awt.TrayIcon
import java.awt.TrayIcon.MessageType
import java.net.URL
import javax.swing.JOptionPane


object SystemNotification {
    private const val NOTIFICATION_TITLE = "PartlySaneSkies"

    fun showNotification(text: String) {
        if (SystemTray.isSupported()) {
            showTrayNotification(text)
        } else {
            showJOptionPaneBackup(text)
            System.err.println("System tray not supported!")
        }
    }

    private fun showTrayNotification(text: String) {
        try {
            val tray = SystemTray.getSystemTray()

            // Custom Icon doesnt seem to be possible anyway sooo idk
            val image = Toolkit.getDefaultToolkit().createImage(URL("https://cdn.modrinth.com/data/jlWHBQtc/8be3d6a35e683c41f9ddf086fd6d56146a494d75.jpeg"))
            val trayIcon = TrayIcon(image, "PartlySaneSkies")

            trayIcon.setImageAutoSize(true)

            trayIcon.setToolTip("PartlySaneSkies")
            tray.add(trayIcon)

            trayIcon.displayMessage("PartlySaneSkies", text, MessageType.INFO)
        } catch (e: Exception) {
            e.printStackTrace()
            showJOptionPaneBackup(text)
        }
    }

    private fun showJOptionPaneBackup(text: String) {
        JOptionPane.showMessageDialog(null, text, NOTIFICATION_TITLE, JOptionPane.INFORMATION_MESSAGE)
    }
}