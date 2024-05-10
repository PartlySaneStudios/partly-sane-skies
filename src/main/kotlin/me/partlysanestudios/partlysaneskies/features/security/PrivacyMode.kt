package me.partlysanestudios.partlysaneskies.features.security

import me.partlysanestudios.partlysaneskies.PartlySaneSkies
import me.partlysanestudios.partlysaneskies.utils.SystemUtils
import org.apache.logging.log4j.Level

object PrivacyMode {

    private var enablePrivacyMode = false

    fun shouldBlockTelemetry(): Boolean {
        return PartlySaneSkies.config.privacyMode == 3 || (PartlySaneSkies.config.privacyMode != 0 && enablePrivacyMode)
    }

    fun enablePrivacyMode() {
        SystemUtils.log(
            Level.INFO,
            "Privacy mode has been enabled. Privacy Mode Option: ${PartlySaneSkies.config.privacyMode}"
        )
    }

}