package io.lindstrom.m3u8.parser;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

abstract class AbstractLineParser<T> {
    static final Pattern ATTRIBUTE_LIST_PATTERN = Pattern.compile("([A-Z0-9\\-]+)=(?:(?:\"([^\"]+)\")|([^,]+))");
    private final String tag;

    AbstractLineParser(String tag) {
        this.tag = tag;
    }

    void write(T value, StringBuilder stringBuilder) {
        stringBuilder.append(tag).append(':');
        stringBuilder.append(writeAttributes(value));
        stringBuilder.append('\n');
    }

    T parse(String attributes) throws PlaylistParserException {
        return parse(attributes, Collections.emptyMap());
    }

    T parse(String attributes, Map<String, String> moreAttributes) throws PlaylistParserException {
        if (attributes.isEmpty()) {
            return parseAttributes(Collections.emptyMap());
        } else {
            Map<String, String> attributeMap = parseAttributes(attributes);
            attributeMap.putAll(moreAttributes);
            return parseAttributes(attributeMap);
        }
    }

    abstract T parseAttributes(Map<String, String> attributes) throws PlaylistParserException;

    abstract String writeAttributes(T value);

    private Map<String, String> parseAttributes(String attributeList) {
        Matcher matcher = ATTRIBUTE_LIST_PATTERN.matcher(attributeList);
        Map<String, String> attributes = new HashMap<>();
        while (matcher.find()) {
            attributes.put(matcher.group(1), matcher.group(2) != null ? matcher.group(2) : matcher.group(3));
        }
        return attributes;
    }
}
