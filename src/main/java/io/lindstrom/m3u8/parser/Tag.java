package io.lindstrom.m3u8.parser;

import java.util.Iterator;

interface Tag<T, B> {
    void read(B builder, String attributes, Iterator<String> lineIterator) throws PlaylistParserException;
    void write(T playlist, TextBuilder textBuilder);
    String name();

    default String tag() {
        return "#" + name().replace("_", "-");
    }
}
