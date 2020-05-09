package io.lindstrom.m3u8.parser;

interface Tag<T, B> {
    void read(B builder, String attributes) throws PlaylistParserException;
    void write(T playlist, TextBuilder textBuilder);
    String name();

    default String tag() {
        return "#" + name().replace("_", "-");
    }
}
