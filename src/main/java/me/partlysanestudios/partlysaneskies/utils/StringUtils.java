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
        String formattedNum = decimalFormat.format(num);
    
        if (formattedNum.charAt(0) == '.') {
            formattedNum = formattedNum.replaceFirst(".", "");
        }
    
        if (formattedNum.endsWith(".00")) {
            formattedNum = formattedNum.replace(".00", "");
        }
    
        if (formattedNum.equalsIgnoreCase("00")) {
            formattedNum = "0";
        }
    
        formattedNum = formattedNum.replace(",", "_");
        formattedNum = formattedNum.replace(".", decimalPlaceFormat);
        formattedNum = formattedNum.replace("_", hundredsPlaceFormat);
    
        
    
        return formattedNum;
    }
    
}
