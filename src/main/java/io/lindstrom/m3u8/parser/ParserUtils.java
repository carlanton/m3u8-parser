package io.lindstrom.m3u8.parser;

import io.lindstrom.m3u8.model.Resolution;

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

    static Resolution parseResolution(String string) throws PlaylistParserException {
        String[] fields = string.split("x");
        Resolution resolution;
        try {
            resolution = Resolution.of(Integer.parseInt(fields[0]), Integer.parseInt(fields[1]));
        } catch (NumberFormatException | IndexOutOfBoundsException e) {
            throw new PlaylistParserException("Invalid resolution: " + string);
        }
        return resolution;
    }

    static String writeResolution(Resolution resolution) {
        return resolution.width() + "x" + resolution.height();
    }
}
