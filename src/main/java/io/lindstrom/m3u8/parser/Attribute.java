package io.lindstrom.m3u8.parser;

public interface Attribute<T, B> {
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
