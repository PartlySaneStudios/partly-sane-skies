//
// Written by Su386.
// See LICENSE for copyright and license notices.
//

package me.partlysanestudios.partlysaneskies.features.misc


import me.partlysanestudios.partlysaneskies.PartlySaneSkies.Companion.config
import me.partlysanestudios.partlysaneskies.utils.SystemUtils.log
import org.apache.logging.log4j.Level

object PrivacyMode {
    private var enablePrivacyMode = false

    fun shouldBlockTelemetry(): Boolean {
        return config.privacyMode == 3 || (config.privacyMode != 0 && enablePrivacyMode)
    }

    fun enablePrivacyMode() {
        log(Level.INFO, "Privacy mode has been enabled. Privacy Mode Option: ${config.privacyMode}")
    }

}