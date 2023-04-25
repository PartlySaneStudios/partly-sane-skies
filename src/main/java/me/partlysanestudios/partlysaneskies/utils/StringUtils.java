//
// Written by Su386.
// See LICENSE for copyright and license notices.
//


package me.partlysanestudios.partlysaneskies.utils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import me.partlysanestudios.partlysaneskies.PartlySaneSkies;

public class StringUtils {

    public static String colorCodes(String text) {
        return text.replace("&", "§");
    }

    public static String removeColorCodes(String text) {
        StringBuilder textBuilder = new StringBuilder(text);
        while (textBuilder.indexOf("§") != -1) {
            textBuilder.deleteCharAt(textBuilder.indexOf("§") + 1);
            textBuilder.deleteCharAt(textBuilder.indexOf("§"));
        }
        return textBuilder.toString();
    }

    public static String wrapText(String text, int charNumber) {
        char[] charArray = text.toCharArray();
        List<String> words = new ArrayList<String>();
        List<Character> chars = new ArrayList<Character>();
        for (char c : charArray) {
            if (c == ' ') {
                words.add(StringUtils.charArrayToString(chars));
                chars.clear();
            } else {
                chars.add(c);
            }
        }
        words.add(StringUtils.charArrayToString(chars));
    
        // ----------------------------------------
    
        int charsOnLine = 0;
        String wrappedText = "";
        List<String> line = new ArrayList<String>();
        boolean wasPreviousCharFormatCode = false;
        String previousFormatCode = "";
        String currentLineFormatCode = "";
        for (String word : words) {
            charsOnLine += word.length();
            if (charsOnLine >= charNumber) {
                line.add(word);
                String lineString = previousFormatCode;
                for (String wordOnLine : line) {
                    lineString += (wordOnLine + " ");
                }
                wrappedText += colorCodes(lineString) + "\n";
                line.clear();
                charsOnLine = 0;
                previousFormatCode = currentLineFormatCode;
            } else {
                line.add(word);
            }
            for (char c : word.toCharArray()) {
                if (wasPreviousCharFormatCode) {
                    currentLineFormatCode += c;
                    wasPreviousCharFormatCode = false;
                }
                if (c == '&') {
                    currentLineFormatCode += c;
                    wasPreviousCharFormatCode = true;
                }
            }
        }
        String lineString = previousFormatCode;
        for (String wordOnLine : line) {
            lineString += (wordOnLine + " ");
        }
        wrappedText += colorCodes(lineString);
        line.clear();
        return wrappedText;
    }

    public static String charArrayToString(List<Character> chars) {
        String string = "";
        for (char c : chars)
            string += c;
        return string;
    }

    public static String stripLeading(String str) {
        if (str.equals("")) {
            return str;
        }
        if (Character.isWhitespace(str.charAt(0))) {
            str = new StringBuilder(str).replace(0, 1, "").toString();
            str = stripLeading(str);
        }
    
        return str;
    }

    public static String stripTrailing(String str) {

        if (str.equals("")) {
            return str;
        }
        if (Character.isWhitespace(str.charAt(str.length() - 1))) {
            str = new StringBuilder(str).replace(str.length() - 1, str.length(), "").toString();
            str = stripTrailing(str);
        }
    
        return str;
    }

    public static String formatNumber(double num) {
    
        DecimalFormat decimalFormat = new DecimalFormat("#,###.00");

//        Creates a string with the number formatted with the above decimal format
        String formattedNum = decimalFormat.format(num);

        String hundredsPlaceFormat = "";
        switch (PartlySaneSkies.config.hundredsPlaceFormat) {
            case 0:
                hundredsPlaceFormat = ",";
                break;
    
            case 1:
                hundredsPlaceFormat = " ";
                break;
    
            case 2:
                hundredsPlaceFormat = ".";
                break;
        }
    
        String decimalPlaceFormat = "";
        switch (PartlySaneSkies.config.decimalPlaceFormat) {
            case 0:
                decimalPlaceFormat = ",";
                break;
    
            case 1:
                decimalPlaceFormat = ".";
                break;
        }

//        Checks if the number starts with a ., if so removes it
        if (formattedNum.charAt(0) == '.') {
            formattedNum = formattedNum.replaceFirst(".", "");
        }

//       Checks if the number is 00
        if (formattedNum.equalsIgnoreCase("00")) {
            formattedNum = "0";
        }

        boolean isDecimal = false;
        for (char ch : formattedNum.toCharArray()) {
            if (ch == '.') {
                isDecimal = true;
                break;
            }
        }
        if (isDecimal) {
            stripTrailingChars(formattedNum, "0");
        }

        if (formattedNum.charAt(formattedNum.length() - 1) == '.') {

            formattedNum = formattedNum.substring(0, formattedNum.length() - 1);
        }

        formattedNum = formattedNum.replace(",", "_");
        formattedNum = formattedNum.replace(".", decimalPlaceFormat);
        formattedNum = formattedNum.replace("_", hundredsPlaceFormat);

    
        return formattedNum;
    }


    public static String stripTrailingChars(String str, String chars) {
        if (str.equals("")) {
            return str;
        }

        if (str.substring(str.length() - chars.length()).equalsIgnoreCase(chars)) {
            str = str.substring(0, str.length() - chars.length());
        }

        return stripTrailingChars(str, chars);
    }

    // Returns a result from a given pattern and key
    /*
     * Example: 
     * StringUtils.recognisePattern("Wow! Su386", "Wow! {player}", "{player}");
     * Will return "Su386" as it is finding the pattern {player}
     */
    public static String recognisePattern(String input, String pattern, String key) {
        String result = input;

        // Gets finds the index where the key will start, because it will be the same accross
        // both patterns
        int keyIndex = pattern.indexOf(key);
        if (!(keyIndex > result.length())) {
            result = result.substring(keyIndex);
        }
       

        // Gets the first few letters after the key in the pattern
        int patternEndKeyIndex = keyIndex + key.length(); 
        String charsAfterKey;

        // If the key is the last thing in the pattern, return the result
        if (patternEndKeyIndex == pattern.length()) {
            return result;
        }
        // If the pattern has more than 3 characters after the pattern, 
        // gets the first 3 characters from the pattern to avoid index out of bounds
        else if (patternEndKeyIndex + 4 <= pattern.length()) {
            charsAfterKey = pattern.substring(patternEndKeyIndex, patternEndKeyIndex + 4);
        }
        // IF it has less than 3 characters after the pattern, the
        // the rest of the string is the characters after the pattern 
        else {
            charsAfterKey = pattern.substring(patternEndKeyIndex);
        }

        // Uses those characters to get the end of the string in the
        // input, not the pattern
        int inputEndKeyIndex = result.indexOf(charsAfterKey);
        if (inputEndKeyIndex >= result.length()) {
            return "";
        }
        if (inputEndKeyIndex == -1) {
            return "";
        }
        result = result.substring(0, inputEndKeyIndex);

        return result;
    }

    public static boolean isPattern(String input, String pattern, String key) {
        // Gets finds the index where the key will start, because it will be the same accross
        // both patterns
       
        String result = recognisePattern(input, pattern, key);
        String patternWithoutKey = replaceFirst(pattern, key, "");
        String inputWithoutKey = replaceFirst(input, result, "");
        return patternWithoutKey.equals(inputWithoutKey);
    }

    public static boolean startsWithPattern(String input, String pattern, String key) {
        String result = recognisePattern(input, pattern, key);
        String patternWithoutKey = replaceFirst(pattern, key, "");
        String inputWithoutKey = replaceFirst(input, result, "");
        if (inputWithoutKey.length() < patternWithoutKey.length()) {
            return false;
        }
        String beginningOfInputWithoutKey = inputWithoutKey.substring(0, patternWithoutKey.length());

        return beginningOfInputWithoutKey.equals(patternWithoutKey);
    }

    public static String replaceFirst(String string, String key, String replacement) {
        int index = string.indexOf(key);
        if (index != -1) { // Make sure the search string was found
            String before = string.substring(0, index);
            String after = string.substring(index + key.length());
            String newString = before + replacement + after;
            return newString;
        } else {
            return string;
        }
    }
}
