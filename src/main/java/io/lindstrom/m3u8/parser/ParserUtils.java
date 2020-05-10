package io.lindstrom.m3u8.parser;

import io.lindstrom.m3u8.model.ByteRange;
import io.lindstrom.m3u8.model.Resolution;

import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class ParserUtils {
    static final DateTimeFormatter FORMATTER = new DateTimeFormatterBuilder()
            .append(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
            .optionalStart().appendOffset("+HH:MM", "+00:00").optionalEnd()
            .optionalStart().appendOffset("+HHMM", "+0000").optionalEnd()
            .optionalStart().appendOffset("+HH", "Z").optionalEnd()
            .toFormatter();

    static final String YES = "YES";
    static final String NO = "NO";

    private static final Pattern BYTE_RANGE_PATTERN = Pattern.compile("(\\d+)(?:@(\\d+))?");

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

    static ByteRange parseByteRange(String attributes) throws PlaylistParserException {
        Matcher matcher = BYTE_RANGE_PATTERN.matcher(attributes);
        if (!matcher.matches()) {
            throw new PlaylistParserException("Invalid byte range " + attributes);
        }
        ByteRange.Builder byteRange = ByteRange.builder();
        byteRange.length(Long.parseLong(matcher.group(1)));
        if (matcher.group(2) != null) {
            byteRange.offset(Long.parseLong(matcher.group(2)));
        }
        return byteRange.build();
    }

    static String writeByteRange(ByteRange byteRange) {
        return byteRange.length() + byteRange.offset().map(offset -> "@" + offset).orElse("");
    }
}
