package io.lindstrom.m3u8.parser;

import java.util.Map;

public interface Parser<T> {
    T parse(String attributes);
    T parse(String attributes, Map<String, String> moreAttributes);
    void write(T value, StringBuilder stringBuilder);
}
