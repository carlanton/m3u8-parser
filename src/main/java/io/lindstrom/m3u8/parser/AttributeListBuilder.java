package io.lindstrom.m3u8.parser;

import static io.lindstrom.m3u8.parser.Tags.NO;
import static io.lindstrom.m3u8.parser.Tags.YES;

class AttributeListBuilder {
    private final StringBuilder stringBuilder = new StringBuilder();

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
        if (stringBuilder.length() > 0) {
            stringBuilder.append(",");
        }
        stringBuilder.append(key).append("=").append(value);
    }

    public void addRaw(String string) {
        stringBuilder.append(string);
    }

    @Override
    public String toString() {
        return stringBuilder.toString();
    }
}
