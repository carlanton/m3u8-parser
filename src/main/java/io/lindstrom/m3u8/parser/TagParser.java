package io.lindstrom.m3u8.parser;

interface TagParser<T> {
    T parse(String attributes) throws PlaylistParserException;

    void write(T value, StringBuilder stringBuilder);
}
