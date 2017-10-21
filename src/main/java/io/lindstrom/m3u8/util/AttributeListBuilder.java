package io.lindstrom.m3u8.util;

import java.util.ArrayList;
import java.util.List;

public class AttributeListBuilder {
    private final List<String> attributes = new ArrayList<>();

    public void add(String key, Enum<?> value) {
        attributes.add(String.format("%s=%s", key, value));
    }

    public void add(String key, String value) {
        attributes.add(String.format("%s=%s", key, value));
    }

    public void add(String key, boolean value) {
        attributes.add(String.format("%s=%s", key, value ? "YES" : "NO"));
    }

    public void addQuoted(String key, Object value) {
        attributes.add(String.format("%s=\"%s\"", key, value));
    }

    @Override
    public String toString() {
        return String.join(",", attributes);
    }
}