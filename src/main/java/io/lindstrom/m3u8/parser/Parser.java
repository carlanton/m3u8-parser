package io.lindstrom.m3u8.parser;

import java.util.Map;

public interface Parser<T> {
    T parse(Map<String, String> attributes);
    String write(T value);
}
