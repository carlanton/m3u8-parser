package io.lindstrom.m3u8.parser;

import java.util.EnumSet;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class DefaultTagParser<T, B, P extends Enum<P> & AttributeParser<T, B>> implements TagParser<T> {
    static final Pattern ATTRIBUTE_LIST_PATTERN = Pattern.compile("([A-Z0-9\\-]+)=(?:(?:\"([^\"]+)\")|([^,]+))");
    private final String tag;
    private final Class<P> enumClass;
    private final Function<B, T> buildFunction;
    private final Supplier<B> newBuilder;

    public DefaultTagParser(String tag, Class<P> enumClass, Function<B, T> buildFunction, Supplier<B> newBuilder) {
        this.tag = tag;
        this.enumClass = enumClass;
        this.buildFunction = buildFunction;
        this.newBuilder = newBuilder;
    }

    @Override
    public T parse(String attributes) throws PlaylistParserException {
        return parse(attributes, null);
    }

    @Override
    public T parse(String attributeList, String uri) throws PlaylistParserException {
        B builder = newBuilder.get();
        Matcher matcher = ATTRIBUTE_LIST_PATTERN.matcher(attributeList);

        while (matcher.find()) {
            String attribute = matcher.group(1);
            String value = matcher.group(2) != null ? matcher.group(2) : matcher.group(3);

            try {
                if (attribute.startsWith("X-")) {
                    Enum.valueOf(enumClass, "CUSTOM").read(builder, attribute, value);
                } else {
                    Enum.valueOf(enumClass, attribute.replace("-", "_")).read(builder, value);
                }
            } catch (IllegalArgumentException e) {
                throw new PlaylistParserException("Unknown attribute: " + attribute);
            }
        }

        if (uri != null) {
            Enum.valueOf(enumClass, "URI").read(builder, uri);
        }

        return buildFunction.apply(builder);
    }

    @Override
    public void write(T value, StringBuilder stringBuilder) {
        AttributeListBuilder attributes = new AttributeListBuilder();
        EnumSet.allOf(enumClass).forEach(attribute -> attribute.write(attributes, value));

        stringBuilder.append(tag).append(':');
        stringBuilder.append(attributes.toString());
        stringBuilder.append('\n');
    }
}
