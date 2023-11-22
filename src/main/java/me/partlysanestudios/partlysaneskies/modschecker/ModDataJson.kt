package me.partlysanestudios.partlysaneskies.modschecker

object ModDataJson {
    val mods: MutableMap<String, ModInfo> = HashMap()

    class ModInfo {
        var download: String = ""

        val versions: MutableMap<String, String>

        init {
            versions = HashMap()
        }
    }
}
