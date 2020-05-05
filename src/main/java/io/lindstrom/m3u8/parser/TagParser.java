package io.lindstrom.m3u8.parser;

interface TagParser<T> {
    T parse(String attributes) throws PlaylistParserException;

    default T parse(String attributes, String uri) throws PlaylistParserException {
        return parse(attributes);
    }

    void write(T value, StringBuilder stringBuilder);
}
