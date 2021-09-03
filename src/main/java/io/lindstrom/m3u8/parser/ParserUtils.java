package io.lindstrom.m3u8.parser;

import io.lindstrom.m3u8.model.ByteRange;
import io.lindstrom.m3u8.model.Channels;
import io.lindstrom.m3u8.model.Resolution;

import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
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
    static final String CLIENT_ATTRIBUTE = "CLIENT-ATTRIBUTE";
    static final Pattern ATTRIBUTE_LIST_PATTERN = Pattern.compile("([A-Z0-9\\-]+)=(?:(?:\"([^\"]+)\")|([^,]+))");

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

    static Channels parseChannels(String attributes) throws PlaylistParserException {
        Channels.Builder builder = Channels.builder();
        String[] parameters = attributes.split("/");
        try {
            builder.count(Integer.parseInt(parameters[0]));
        } catch (NumberFormatException e) {
            throw new PlaylistParserException("Invalid channels: " + attributes);
        }
        if (parameters.length > 1) {
            builder.objectCodingIdentifiers(split(parameters[1], ","));
        }
        return builder.build();
    }

    static String writeChannels(Channels channels) {
        String attributes = String.valueOf(channels.count());
        if (!channels.objectCodingIdentifiers().isEmpty()) {
            attributes += "/" + String.join(",", channels.objectCodingIdentifiers());
        }
        return attributes;
    }

    static <T> Map<String, T> toMap(T[] values, Function<T, String> keyMapper) {
        Map<String, T> map = new LinkedHashMap<>(values.length);
        for (T tag : values) {
            map.put(keyMapper.apply(tag), tag);
        }
        return map;
    }

    static <B, T extends Attribute<?, B>> void readAttributes(
            Map<String, T> attributeMap, String attributes, B builder, ParsingMode parsingMode) throws PlaylistParserException {

        Matcher matcher = ATTRIBUTE_LIST_PATTERN.matcher(attributes);

        while (matcher.find()) {
            String key = matcher.group(1);
            String value = matcher.group(2) != null ? matcher.group(2) : matcher.group(3);
            boolean clientAttribute = key.startsWith("X-");

            T attribute = attributeMap.get(clientAttribute ? CLIENT_ATTRIBUTE : key);

            if (attribute != null) {
                if (clientAttribute) {
                    attribute.read(builder, key, value);
                } else {
                    attribute.read(builder, value);
                }
            } else if (parsingMode.failOnUnknownAttributes()) {
                throw new PlaylistParserException("Unknown attribute: " + key);
            }
        }
    }
}
