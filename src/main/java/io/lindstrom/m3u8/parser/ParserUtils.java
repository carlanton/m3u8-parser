package io.lindstrom.m3u8.parser;

import java.util.Arrays;
import java.util.List;

class ParserUtils {
    static List<String> split(String string, String delimiter) {
        return Arrays.asList(string.split(delimiter));
    }
}
