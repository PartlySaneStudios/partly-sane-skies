//
// Written by Su386.
// See LICENSE for copyright and license notices.
//

package me.partlysanestudios.partlysaneskies.features.misc


import me.partlysanestudios.partlysaneskies.PartlySaneSkies.Companion.config

object PrivacyMode {
    var enablePrivacyMode = false

    fun shouldBlockTelemetry(): Boolean {
        return config.privacyMode == 3 || (config.privacyMode != 0 && enablePrivacyMode)
    }
}