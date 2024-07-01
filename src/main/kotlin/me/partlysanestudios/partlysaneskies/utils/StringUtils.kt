//
// Written by Su386 and J10a1n15.
// See LICENSE for copyright and license notices.
//


package me.partlysanestudios.partlysaneskies.utils

import me.partlysanestudios.partlysaneskies.PartlySaneSkies
import java.awt.Color
import java.text.DecimalFormat
import java.util.*
import java.util.regex.Pattern

object StringUtils {
    /**
     * Removes all color codes from a string
     * @return The string without color codes
     */
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

    /**
     * Removes all color codes from a list of strings
     * @return The list of strings without color codes
     */
    fun List<String>.removeColorCodes() = this.map { it.removeColorCodes() }

    /**
     * Removes all color codes from a string
     * @return The string without color codes
     */
    fun String.removeResets() = this.replace("§r", "")

    /**
     * Wraps text to a given number of characters
     * @param charNumber The number of characters to wrap the text to
     * @return The wrapped text
     */
    fun String.wrapText(charNumber: Int): String {
        val charArray = this.toCharArray()
        val words: MutableList<String> = ArrayList()
        val chars: MutableList<Char> = ArrayList()

        for (c in charArray) if (c == ' ') {
            words.add(chars.joinToString(""))
            chars.clear()
        } else {
            chars.add(c)
        }

        words.add(chars.joinToString(""))

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

    fun String.pluralize(number: Number): String {
        return if (number == 1) {
            this
        } else {
            "${this}s"
        }
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

    fun Number.formatNumber(): String {
        val decimalFormat = DecimalFormat("#,###.00")

//        Creates a string with the number formatted with the above decimal format
        var formattedNum = decimalFormat.format(this)
        if (formattedNum.startsWith(".")) {
            formattedNum = "0$formattedNum"
        }
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

    /**
     * Returns a result from a given pattern and key
     *
     * For example, this will return "Su386"
     * ```kotlin
     * recognisePattern("Wow! Su386", "Wow! {player}", "{player}");
     * ```
     *
     * @param input The input to get the result from
     * @param pattern The pattern to use to get the result
     * @param key The key to use to get the result
     * @return The result
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

    fun Char.romanCharToInt(): Int {
        return when (this) {
            'I' -> 1
            'V' -> 5
            'X' -> 10
            'L' -> 50
            'C' -> 100
            'D' -> 500
            'M' -> 1000
            else -> throw IllegalArgumentException("Invalid Roman numeral character: $this")
        }
    }

    fun String.romanNumeralToInt(): Int {
        var total = 0
        var prevValue = 0

        for (char in this.reversed()) {
            val currentValue = char.romanCharToInt()

            if (currentValue < prevValue) {
                total -= currentValue
            } else {
                total += currentValue
            }

            prevValue = currentValue
        }

        return total
    }

    fun String.titleCase(): String {
        val titleCase = StringBuilder()
        var nextCharUpperCase = true
        for (i in this.indices) {
            val ch = this.substring(i, i + 1)
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
            while (num.isNotEmpty() && pattern.matcher(num.substring(num.length - 1)).find()) {
                val str = num.substring(num.length - 1)
                when (str) {
                    "k" -> parsedNum *= 1_000f
                    "m" -> parsedNum *= 1_000_000f
                    "b" -> parsedNum *= 1_000_000_000f
                    "t" -> parsedNum *= 1_000_000_000_000f
                }
                num = num.substring(0, num.length - 1)
            }
            return parsedNum
        } catch (e: NumberFormatException) {
            return Double.NaN
        }
    }

    fun String.colorCodeToColor(): Color {
        return when (this) {
            "§a" -> Color(85, 255, 85)
            "§b" -> Color(85, 255, 255)
            "§c" -> Color(255, 85, 85)
            "§d" -> Color(255, 85, 255)
            "§e" -> Color(255, 255, 85)
            "§f" -> Color(0, 0, 0)
            "§1" -> Color(0, 0, 170)
            "§2" -> Color(0, 170, 0)
            "§3" -> Color(0, 170, 170)
            "§4" -> Color(170, 0, 0)
            "§5" -> Color(170, 0, 170)
            "§6" -> Color(255, 170, 0)
            "§7" -> Color(170, 170, 170)
            "§8" -> Color(85, 85, 85)
            "§9" -> Color(85, 85, 255)
            "§0" -> Color(0, 0, 0)
            else -> Color.white
        }
    }

    fun List<String>.nextAfter(element: String): String? {
        val index = this.indexOf(element)
        return if (index in 0 until this.size - 1) {
            this[index + 1]
        } else {
            null
        }
    }

    fun Int.toRoman(): String {
        if (this <= 0 || this > 3999) {
            throw IllegalArgumentException("Number out of range (must be between 1 and 3999)")
        }

        var number = this
        val stringBuilder = buildString {
            fun appendRomanSymbols(value: Int, symbol: String) {
                while (number >= value) {
                    this.append(symbol)
                    number -= value
                }
            }

            appendRomanSymbols(1000, "M")
            appendRomanSymbols(900, "CM")
            appendRomanSymbols(500, "D")
            appendRomanSymbols(400, "CD")
            appendRomanSymbols(100, "C")
            appendRomanSymbols(90, "XC")
            appendRomanSymbols(50, "L")
            appendRomanSymbols(40, "XL")
            appendRomanSymbols(10, "X")
            appendRomanSymbols(9, "IX")
            appendRomanSymbols(5, "V")
            appendRomanSymbols(4, "IV")
            appendRomanSymbols(1, "I")
        }

        return stringBuilder
    }

}
