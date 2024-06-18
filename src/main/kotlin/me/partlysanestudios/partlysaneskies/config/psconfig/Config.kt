package me.partlysanestudios.partlysaneskies.config.psconfig

class Config(): ConfigOption() {

    companion object {
        val ConfigOption.asConfig: Config
            get() {
                return this as Config
            }
    }
    // Recursively find paths for options
    fun find(path: String): ConfigOption? {
        val indexOfSplit = path.indexOf("/")

        if (indexOfSplit == -1) {
            return options[path]
        }

        val firstKey = path.substring(0, indexOfSplit)

        val newConfig = options[firstKey]
        if (newConfig !is ConfigOption) {
            return null
        }
        return newConfig.asConfig.find(path)
    }

    private val options = HashMap<String, ConfigOption>()
    // Recursively create new options to get to the path
    fun registerOption(path: String, configOption: ConfigOption) {
        val indexOfSplit = path.indexOf("/")

        if (indexOfSplit == -1) {
            options[path] = configOption
            return
        }

        val firstKey = path.substring(0, indexOfSplit)

        val newConfig = Config()
        options[firstKey] = newConfig
        newConfig.registerOption(path.substring(indexOfSplit), configOption)
    }
}