package io.lindstrom.m3u8.parser;

import java.util.Arrays;
import java.util.List;

import static io.lindstrom.m3u8.parser.Tags.NO;
import static io.lindstrom.m3u8.parser.Tags.YES;

class ParserUtils {
    static List<String> split(String string, String delimiter) {
        return Arrays.asList(string.split(delimiter));
    }

    static boolean yesOrNo(String value) throws PlaylistParserException {
        switch (value) {
            case YES:
                return true;
            case NO:
                return false;
            default:
                throw new PlaylistParserException("Expected YES or NO, got " + value);
        }
    }
}
