package io.lindstrom.m3u8.parser;

import java.util.EnumSet;

public class TextBuilder {
    private final StringBuilder stringBuilder;

    public TextBuilder(StringBuilder stringBuilder) {
        this.stringBuilder = stringBuilder;
    }

    public <X, M extends Enum<M> & AttributeMapper<X, ?>> TextBuilder add(String tag,
                                                                          X value,
                                                                          Class<M> mapperClass) {

        AttributeListBuilder attributes = new AttributeListBuilder();
        EnumSet.allOf(mapperClass).forEach(attribute -> attribute.write(attributes, value));

        stringBuilder.append(tag).append(':');
        stringBuilder.append(attributes.toString());
        stringBuilder.append('\n');

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

    public StringBuilder stringBuilder() {
        return stringBuilder;
    }
}
