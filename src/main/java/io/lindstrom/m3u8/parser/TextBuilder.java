package io.lindstrom.m3u8.parser;

import java.util.EnumSet;
import java.util.List;

import static io.lindstrom.m3u8.parser.ParserUtils.NO;
import static io.lindstrom.m3u8.parser.ParserUtils.YES;

class TextBuilder {
    private final StringBuilder stringBuilder;

    private int currentAttributeCount = 0;

    TextBuilder(StringBuilder stringBuilder) {
        this.stringBuilder = stringBuilder;
    }

    TextBuilder() {
        this.stringBuilder = new StringBuilder();
    }

    <T, M extends Enum<M> & Attribute<T, ?>> void addTag(String tag, List<T> values, Class<M> mapperClass) {
        values.forEach(value -> addTag(tag, value, mapperClass));
    }

    <T, M extends Enum<M> & Attribute<T, ?>> void addTag(String tag, T value, Class<M> mapperClass) {
        stringBuilder.append('#').append(tag).append(':');
        addAttributes(value, mapperClass);
        stringBuilder.append('\n');
    }

    <T, M extends Enum<M> & Attribute<T, ?>> TextBuilder addAttributes(T value, Class<M> mapperClass) {
        currentAttributeCount = 0;
        EnumSet.allOf(mapperClass).forEach(attribute -> attribute.write(value, this));
        return this;
    }

    public TextBuilder add(String text) {
        stringBuilder.append(text);
        return this;
    }


    public TextBuilder add(char ch) {
        stringBuilder.append(ch);
        return this;
    }

    public TextBuilder add(double value) {
        stringBuilder.append(value);
        return this;
    }

    // Tag helpers
    void addTag(String tag) {
        stringBuilder.append('#').append(tag).append('\n');
    }

    void addTag(String tag, int attribute) {
        stringBuilder.append('#').append(tag).append(":").append(attribute).append('\n');
    }

    void addTag(String tag, long attribute) {
        stringBuilder.append('#').append(tag).append(":").append(attribute).append('\n');
    }

    void addTag(String tag, String attribute) {
        stringBuilder.append('#').append(tag).append(":").append(attribute).append('\n');
    }



    // attribute
    public void add(String key, Enum<?> value) {
        add(key, value.toString());
    }

    public void add(String key, boolean value) {
        add(key, value ? YES : NO);
    }

    public void addQuoted(String key, String value) {
        add(key, "\"" + value + "\"");
    }

    public void add(String key, String value) {
        if (currentAttributeCount > 0) {
            stringBuilder.append(",");
        }
        stringBuilder.append(key).append("=").append(value);
        currentAttributeCount++;
    }

    @Override
    public String toString() {
        return stringBuilder.toString();
    }
}
