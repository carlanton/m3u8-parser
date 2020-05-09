package io.lindstrom.m3u8.parser;

import java.util.EnumSet;
import java.util.List;
import java.util.Optional;

import static io.lindstrom.m3u8.parser.Tags.NO;
import static io.lindstrom.m3u8.parser.Tags.YES;

public class TextBuilder {
    private final StringBuilder stringBuilder;

    private int currentAttributeCount = 0;

    public TextBuilder(StringBuilder stringBuilder) {
        this.stringBuilder = stringBuilder;
    }

    public <X, M extends Enum<M> & Attribute<X, ?>> TextBuilder add(String tag,
                                                                    List<X> values,
                                                                    Class<M> mapperClass) {

        values.forEach(x -> add(tag, x, mapperClass));

        return this;
    }

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    public <X, M extends Enum<M> & Attribute<X, ?>> TextBuilder add(String tag,
                                                                    Optional<X> value,
                                                                    Class<M> mapperClass) {

        value.ifPresent(x -> add(tag, x, mapperClass));

        return this;
    }

    public <X, M extends Enum<M> & Attribute<X, ?>> TextBuilder add(String tag,
                                                                    X value,
                                                                    Class<M> mapperClass) {



        stringBuilder.append(tag).append(':');
        currentAttributeCount = 0;
        EnumSet.allOf(mapperClass).forEach(attribute -> attribute.write(value, this));
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

    public void addRaw(String string) {
        stringBuilder.append(string);
    }
}
