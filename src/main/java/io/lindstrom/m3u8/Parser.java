package io.lindstrom.m3u8;

import java.util.Map;

interface Parser<T> {
    T parse(String attributes) throws PlaylistParserException;

    T parse(String attributes, Map<String, String> moreAttributes) throws PlaylistParserException;

    void write(T value, StringBuilder stringBuilder);
}
