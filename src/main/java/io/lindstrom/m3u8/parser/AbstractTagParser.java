package io.lindstrom.m3u8.parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class AbstractTagParser<T, B> implements TagParser<T> {
    static final Pattern ATTRIBUTE_LIST_PATTERN = Pattern.compile("([A-Z0-9\\-]+)=(?:(?:\"([^\"]+)\")|([^,]+))");
    private final String tag;

    AbstractTagParser(String tag) {
        this.tag = tag;
    }

    @Override
    public T parse(String attributeList) throws PlaylistParserException {
        return parse(attributeList, null);
    }

    public T parse(String attributeList, String uri) throws PlaylistParserException {
        B builder = newBuilder();
        Matcher matcher = ATTRIBUTE_LIST_PATTERN.matcher(attributeList);

        while (matcher.find()) {
            String attribute = matcher.group(1);
            String value = matcher.group(2) != null ? matcher.group(2) : matcher.group(3);
            onAttribute(attribute, value, builder);
        }

        if (uri != null) {
            onUri(uri, builder);
        }

        return build(builder);
    }

    void onUri(String uri, B builder) {
        // noop
    }

    abstract void onAttribute(String attribute, String value, B builder) throws PlaylistParserException;

    @Override
    public void write(T value, StringBuilder stringBuilder) {
        AttributeListBuilder attributeListBuilder = new AttributeListBuilder();
        write(value, attributeListBuilder);

        stringBuilder.append(tag).append(':');
        stringBuilder.append(attributeListBuilder.toString());
        stringBuilder.append('\n');

        writeUri(value, stringBuilder);
    }

    void writeUri(T value, StringBuilder stringBuilder) {
        // noop
    }

    abstract void write(T value, AttributeListBuilder attributeListBuilder);

    abstract B newBuilder();

    abstract T build(B builder);
}
