//
// Written by hannibal002 and Su386.
// See LICENSE for copyright and license notices.
//

package me.partlysanestudios.partlysaneskies.features.security.modschecker

import com.google.gson.annotations.Expose

class ModDataJson {
    @Expose
    val mods: Map<String, ModInfo>? = null

    class ModInfo {
        @Expose
        var name: String = ""

        @Expose
        val download: String = ""

        @Expose
        val versions: Map<String, String>

        @Expose
        val betaVersions: Map<String, String>

        init {
            versions = HashMap()
            betaVersions = HashMap()
        }
    }
}
