package io.lindstrom.m3u8.parser;

import java.util.EnumSet;
import java.util.List;

import static io.lindstrom.m3u8.parser.Tags.NO;
import static io.lindstrom.m3u8.parser.Tags.YES;

public class TextBuilder {
    private final StringBuilder stringBuilder;

    private int currentAttributeCount = 0;

    public TextBuilder(StringBuilder stringBuilder) {
        this.stringBuilder = stringBuilder;
    }

    public TextBuilder() {
        this.stringBuilder = new StringBuilder();
    }

    public <X, M extends Enum<M> & Attribute<X, ?>> void add(String tag,
                                                                    List<X> values,
                                                                    Class<M> mapperClass) {

        values.forEach(value -> add(tag, value, mapperClass));

    }

    public <X, M extends Enum<M> & Attribute<X, ?>> void add(String tag, X value, Class<M> mapperClass) {



        stringBuilder.append(tag).append(':');
        addAttributes(value, mapperClass);
        stringBuilder.append('\n');
    }

    public <X, M extends Enum<M> & Attribute<X, ?>> TextBuilder addAttributes(X value, Class<M> mapperClass) {
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

    public TextBuilder add(long l) {
        stringBuilder.append(l);
        return this;
    }

    public TextBuilder add(int i) {
        stringBuilder.append(i);
        return this;
    }

    public TextBuilder add(Object object) {
        stringBuilder.append(object);
        return this;
    }

    // Tag helpers
    void addTag(String tag) {
        stringBuilder.append(tag).append('\n');
    }

    void addTag(String tag, int attribute) {
        stringBuilder.append(tag).append(":").append(attribute).append('\n');
    }

    void addTag(String tag, long attribute) {
        stringBuilder.append(tag).append(":").append(attribute).append('\n');
    }

    void addTag(String tag, String attribute) {
        stringBuilder.append(tag).append(":").append(attribute).append('\n');
    }



    // attribute
    public void add(String key, Enum<?> value) {
        add(key, value.toString());
    }

    public void add(String key, boolean value) {
        add(key, value ? YES : NO);
    }

    public void addQuoted(String key, Object value) {
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
