//
// Written by J10a1n15 and ItsEmpa.
// See LICENSE for copyright and license notices.
//


package me.partlysanestudios.partlysaneskies.features.chat

object OwO {
    private val replacements = listOf(
        "r" to "w",
        "l" to "w",
        "R" to "W",
        "L" to "W",
        "no" to "nyo",
        "No" to "Nyo",
        "NO" to "NYO",
        "na" to "nya",
        "Na" to "Nya",
        "NA" to "NYA",
        "ne" to "nye",
        "Ne" to "Nye",
        "NE" to "NYE",
        "ni" to "nyi",
        "Ni" to "Nyi",
        "NI" to "NYI",
        "nu" to "nyu",
        "Nu" to "Nyu",
        "NU" to "NYU",
        "n" to "ny",
        "N" to "Ny",
        "ove" to "uv",
        "Ove" to "Uv",
        "OVE" to "UV",
        "o" to "owo",
        "O" to "OwO",
        "!" to " >w<",
        "?" to " owo?",
        "." to " owo.",
        "," to " owo,"
    )

    fun owoify(text: String): String {
        var owoifiedText = text
        for ((oldValue, newValue) in replacements) {
            owoifiedText = owoifiedText.replace(oldValue, newValue, ignoreCase = false)
        }
        return owoifiedText
    }
}
