package io.lindstrom.m3u8.parser;

public interface Attribute<T, B> {
    void read(B builder, String value) throws PlaylistParserException;
    void write(AttributeListBuilder attributes, T value);
    String name();
    default String key() {
        return name().replace("_", "-");
    }

    default void read(B builder, String key, String value) {
        // no-op
    }
}
