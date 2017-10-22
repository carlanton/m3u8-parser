package io.lindstrom.m3u8.util;

import java.util.Arrays;
import java.util.List;

public class ParserUtils {
    public static List<String> split(String string, String delimiter) {
        return Arrays.asList(string.split(delimiter));
    }
}
