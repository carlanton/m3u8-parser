package io.lindstrom.m3u8.parser;

import io.lindstrom.m3u8.model.IBuilder;

public interface Attribute<T, B extends IBuilder<T>> {
    void read(B builder, String value) throws PlaylistParserException;
    void write(T value, TextBuilder textBuilder);
    String name();

    default String key() {
        String name = name();
        return name.contains("_") ? name.replace("_", "-") : name;
    }

    default void read(B builder, String key, String value) {
        // no-op
    }
}
