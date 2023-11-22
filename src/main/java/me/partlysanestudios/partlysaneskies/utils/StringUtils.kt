//
// Written by Su386.
// See LICENSE for copyright and license notices.
//


package me.partlysanestudios.partlysaneskies.utils

import me.partlysanestudios.partlysaneskies.PartlySaneSkies
import java.awt.Color
import java.text.DecimalFormat
import java.util.*
import java.util.regex.Pattern

object StringUtils {
    // public static String colorCodes(String text) {
    //     return text.replace("&", "§");
    // }

    fun String.removeColorCodes(): String {
        val textBuilder = StringBuilder(this)
        while (textBuilder.indexOf("§") != -1) {
            if (textBuilder.indexOf("§") == textBuilder.length - 1) {
                textBuilder.deleteCharAt(textBuilder.indexOf("§"))
                continue
            }
            textBuilder.deleteCharAt(textBuilder.indexOf("§") + 1)
            textBuilder.deleteCharAt(textBuilder.indexOf("§"))
        }
        return textBuilder.toString()
    }



    fun String.wrapText(charNumber: Int): String {
        val charArray = this.toCharArray()
        val words: MutableList<String> = ArrayList()
        val chars: MutableList<Char> = ArrayList()
        for (c in charArray) if (c == ' ') {
            words.add(charArrayToString(chars))
            chars.clear()
        } else {
            chars.add(c)
        }
        words.add(charArrayToString(chars))

        // ----------------------------------------
        var charsOnLine = 0
        val wrappedText = StringBuilder()
        val line: MutableList<String> = ArrayList()
        var wasPreviousCharFormatCode = false
        var previousFormatCode = ""
        var currentLineFormatCode = ""
        for (word in words) {
            charsOnLine += word.length
            if (charsOnLine >= charNumber) {
                line.add(word)
                val lineString = StringBuilder(previousFormatCode)
                for (wordOnLine in line) {
                    lineString.append(wordOnLine).append(" ")
                }
                wrappedText.append(lineString.toString()).append("\n")
                line.clear()
                charsOnLine = 0
                previousFormatCode = currentLineFormatCode
            } else {
                line.add(word)
            }
            for (c in word.toCharArray()) {
                if (wasPreviousCharFormatCode) {
                    currentLineFormatCode += c
                    wasPreviousCharFormatCode = false
                }
                if (c == '&') {
                    currentLineFormatCode += c
                    wasPreviousCharFormatCode = true
                }
            }
        }
        val lineString = StringBuilder(previousFormatCode)
        for (wordOnLine in line) {
            lineString.append(wordOnLine).append(" ")
        }
        wrappedText.append(lineString.toString())
        line.clear()
        return wrappedText.toString()
    }

    fun charArrayToString(chars: List<Char>): String {
        val string = StringBuilder()
        for (c in chars) string.append(c)
        return string.toString()
    }

    fun stripLeading(str: String): String {
        var str = str
        if (str == "") {
            return str
        }
        if (Character.isWhitespace(str[0])) {
            str = StringBuilder(str).replace(0, 1, "").toString()
            str = stripLeading(str)
        }
        return str
    }

    fun stripTrailing(str: String): String {
        var str = str
        if (str == "") {
            return str
        }
        if (Character.isWhitespace(str[str.length - 1])) {
            str = StringBuilder(str).replace(str.length - 1, str.length, "").toString()
            str = stripTrailing(str)
        }
        return str
    }

    fun Double.formatNumber(): String {
        val decimalFormat = DecimalFormat("#,###.00")

//        Creates a string with the number formatted with the above decimal format
        var formattedNum = decimalFormat.format(this)
        var hundredsPlaceFormat = ""
        when (PartlySaneSkies.config.hundredsPlaceFormat) {
            0 -> hundredsPlaceFormat = ","
            1 -> hundredsPlaceFormat = " "
            2 -> hundredsPlaceFormat = "."
        }
        var decimalPlaceFormat = ""
        when (PartlySaneSkies.config.decimalPlaceFormat) {
            0 -> decimalPlaceFormat = ","
            1 -> decimalPlaceFormat = "."
        }

//        Checks if the number starts with a ., if so removes it
        if (formattedNum[0] == '.') {
            formattedNum = formattedNum.replaceFirst(".".toRegex(), "")
        }

//       Checks if the number is 00
        if (formattedNum.equals("00", ignoreCase = true)) {
            formattedNum = "0"
        }
        var isDecimal = false
        for (ch in formattedNum.toCharArray()) {
            if (ch == '.') {
                isDecimal = true
                break
            }
        }
        if (isDecimal) {
            formattedNum = stripTrailingChars(formattedNum, "0")
        }
        if (formattedNum[formattedNum.length - 1] == '.') {
            formattedNum = formattedNum.substring(0, formattedNum.length - 1)
        }
        formattedNum = formattedNum.replace(",", "_")
        formattedNum = formattedNum.replace(".", decimalPlaceFormat)
        formattedNum = formattedNum.replace("_", hundredsPlaceFormat)
        return formattedNum
    }

    fun stripTrailingChars(str: String, chars: String): String {
        var str = str
        if (str == "") {
            return str
        }
        if (str.substring(str.length - chars.length).equals(chars, ignoreCase = true)) {
            str = str.substring(0, str.length - chars.length)
            str = stripTrailingChars(str, chars)
        }
        return str
    }

    // Returns a result from a given pattern and key
    /*
     * Example:
     * StringUtils.INSTANCE.recognisePattern("Wow! Su386", "Wow! {player}", "{player}");
     * Will return "Su386" as it is finding the pattern {player}
     */
    fun recognisePattern(input: String, pattern: String, key: String): String {
        var result = input

        // Gets finds the index where the key will start, because it will be the same across
        // both patterns
        val keyIndex = pattern.indexOf(key)
        if (keyIndex <= result.length) {
            result = result.substring(keyIndex)
        }


        // Gets the first few letters after the key in the pattern
        val patternEndKeyIndex = keyIndex + key.length
        val charsAfterKey: String

        // If the key is the last thing in the pattern, return the result
        charsAfterKey = if (patternEndKeyIndex == pattern.length) {
            return result
        } else if (patternEndKeyIndex + 4 <= pattern.length) {
            pattern.substring(patternEndKeyIndex, patternEndKeyIndex + 4)
        } else {
            pattern.substring(patternEndKeyIndex)
        }

        // Uses those characters to get the end of the string in the
        // input, not the pattern
        val inputEndKeyIndex = result.indexOf(charsAfterKey)
        if (inputEndKeyIndex == -1) {
            return ""
        }
        result = result.substring(0, inputEndKeyIndex)
        return result
    }

    fun isPattern(input: String, pattern: String, key: String): Boolean {
        // Gets finds the index where the key will start, because it will be the same across
        // both patterns
        val result = recognisePattern(input, pattern, key)
        val patternWithoutKey = replaceFirst(pattern, key, "")
        val inputWithoutKey = replaceFirst(input, result, "")
        return patternWithoutKey == inputWithoutKey
    }

    fun startsWithPattern(input: String, pattern: String, key: String): Boolean {
        val result = recognisePattern(input, pattern, key)
        val patternWithoutKey = replaceFirst(pattern, key, "")
        val inputWithoutKey = replaceFirst(input, result, "")
        if (inputWithoutKey.length < patternWithoutKey.length) {
            return false
        }
        val beginningOfInputWithoutKey = inputWithoutKey.substring(0, patternWithoutKey.length)
        return beginningOfInputWithoutKey == patternWithoutKey
    }

    fun replaceFirst(string: String, key: String, replacement: String): String {
        val index = string.indexOf(key)
        return if (index != -1) { // Make sure the search string was found
            val before = string.substring(0, index)
            val after = string.substring(index + key.length)
            before + replacement + after
        } else {
            string
        }
    }

    fun String.titleCase(): String {
        val str = this
        val titleCase = StringBuilder()
        var nextCharUpperCase = true
        for (i in 0 until str.length) {
            val ch = str.substring(i, i + 1)
            if (ch != " " && !nextCharUpperCase) {
                titleCase.append(ch.lowercase(Locale.getDefault()))
                continue
            }
            if (nextCharUpperCase) {
                titleCase.append(ch.uppercase(Locale.getDefault()))
                nextCharUpperCase = false
                continue
            }
            titleCase.append(ch.lowercase(Locale.getDefault()))
            nextCharUpperCase = true
        }
        return titleCase.toString()
    }

    fun String.parseAbbreviatedNumber(): Double {
        try {
            var num = this
            num = num.replace(" ", "")
            val digits = num.replace("[^\\d.]".toRegex(), "")
            var parsedNum = digits.toDouble()
            val pattern = Pattern.compile("[^\\d.]")
            while (num.length > 0 && pattern.matcher(num.substring(num.length - 1)).find()) {
                val str = num.substring(num.length - 1)
                when (str) {
                    "k" -> parsedNum *= 1000f
                    "m" -> parsedNum *= 1000000f
                    "b" -> parsedNum *= 1000000000f
                    "t" -> parsedNum *= 1000000000000f
                }
                num = num.substring(0, num.length - 1)
            }
            return parsedNum
        } catch (e: NumberFormatException) {
            return Double.NaN
        }
    }

    fun colorCodeToColor(colorCode: String): Color {
        val colorCodetoColor = HashMap<String, Color>()
        colorCodetoColor["§a"] = Color(85, 255, 85)
        colorCodetoColor["§b"] = Color(85, 255, 255)
        colorCodetoColor["§c"] = Color(255, 85, 85)
        colorCodetoColor["§d"] = Color(255, 85, 255)
        colorCodetoColor["§e"] = Color(255, 255, 85)
        colorCodetoColor["§f"] = Color(0, 0, 0)
        colorCodetoColor["§1"] = Color(0, 0, 170)
        colorCodetoColor["§2"] = Color(0, 170, 0)
        colorCodetoColor["§3"] = Color(0, 170, 170)
        colorCodetoColor["§4"] = Color(170, 0, 0)
        colorCodetoColor["§5"] = Color(170, 0, 170)
        colorCodetoColor["§6"] = Color(255, 170, 0)
        colorCodetoColor["§7"] = Color(170, 170, 170)
        colorCodetoColor["§8"] = Color(85, 85, 85)
        colorCodetoColor["§9"] = Color(85, 85, 255)
        colorCodetoColor["§0"] = Color(0, 0, 0)
        if (colorCodetoColor[colorCode] != null) {
            return colorCodetoColor[colorCode]!!
        }
        else return Color.white
    }
}
