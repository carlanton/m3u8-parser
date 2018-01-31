package io.lindstrom.m3u8.parser;

import java.util.ArrayList;
import java.util.List;

import static io.lindstrom.m3u8.parser.Tags.NO;
import static io.lindstrom.m3u8.parser.Tags.YES;

class AttributeListBuilder {
    private final List<String> attributes = new ArrayList<>();

    public void add(String key, Enum<?> value) {
        add(key, value.toString());
    }

    public void add(String key, boolean value) {
        add(key, value ? YES : NO);
    }

    public void addQuoted(String key, Object value) {
        add(key, "\"" + value + "\"");
    }

    public void add(String key, String value) {
        attributes.add(key + "=" + value);
    }

    @Override
    public String toString() {
        return String.join(",", attributes);
    }
}
