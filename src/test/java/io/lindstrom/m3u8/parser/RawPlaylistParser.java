package io.lindstrom.m3u8.parser;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;

public class RawPlaylistParser extends AbstractPlaylistParser<RawPlaylist, RawPlaylist.Builder> {
    @Override
    RawPlaylist.Builder newBuilder() {
        return new RawPlaylist.Builder();
    }

    @Override
    void onTag(RawPlaylist.Builder builder, String name, String attributeList, Iterator<String> lineIterator) {
        Matcher matcher = ParserUtils.ATTRIBUTE_LIST_PATTERN.matcher(attributeList);
        List<RawAttribute> attributes = new ArrayList<>();
        while (matcher.find()) {
            boolean hasQuotes = matcher.group(2) != null;
            String value = hasQuotes ? matcher.group(2) : matcher.group(3);
            attributes.add(new RawAttribute(matcher.group(1), value, hasQuotes));
        }

        attributes.sort(Comparator.comparing(attribute -> attribute.name));

        if (!attributes.isEmpty()) {
            builder.addTag(name, attributes);
        }
    }

    @Override
    void onComment(RawPlaylist.Builder builder, String value) {
        // ignore
    }

    @Override
    void onURI(RawPlaylist.Builder builder, String uri) {
        // ignore
    }

    @Override
    RawPlaylist build(RawPlaylist.Builder builder) {
        return builder.build();
    }

    @Override
    void write(RawPlaylist playlist, TextBuilder textBuilder) {
        throw new UnsupportedOperationException("not implemented");
    }
}
