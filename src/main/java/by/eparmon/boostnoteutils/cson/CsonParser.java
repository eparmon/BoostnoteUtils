package by.eparmon.boostnoteutils.cson;

import by.eparmon.boostnoteutils.file.FileHelper;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CsonParser {

    public static Map<String, String> parseCsonFile(File file) {
        Map<String, String> map = new HashMap<>();
        List<String> lines = FileHelper.getLines(file);
        String key = null;
        StringBuilder value = new StringBuilder();
        for (String line : lines) {
            if (line.startsWith("# ")) // comment
                continue;
            if (Character.isAlphabetic(line.charAt(0))) { // new key-value pair
                if (key != null) { // store the previous key-value pair
                    map.put(key, value.toString().replace("\"", ""));
                    value.setLength(0);
                }
                int separator = line.indexOf(": ");
                key = line.substring(0, separator);
                if (line.charAt(separator + 2) != '[') {
                    value.append(line.charAt(separator + 2));
                }
                value.append(line.substring(separator + 3));
            } else if (line.startsWith("  ")) { // part of the value
                value.append('\n').append(line.substring(2));
            }
        }
        if (key != null) {
            map.put(key, value.toString().replace("\"", ""));
        }
        return map;
    }
}
