package io.lindstrom.m3u8.parser;

import io.lindstrom.m3u8.model.KeyMethod;
import io.lindstrom.m3u8.model.SegmentKey;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static io.lindstrom.m3u8.parser.Tags.*;

class SegmentKeyParser extends AbstractLineParser<SegmentKey> {
    private static final Pattern ATTRIBUTE_LIST_PATTERN = Pattern.compile("([A-Z0-9\\-]+)=(?:(?:\"([^\"]+)\"))");

    SegmentKeyParser() {
        super(EXT_X_KEY);
    }

    @Override
    SegmentKey parseAttributes(Map<String, String> attributes) throws PlaylistParserException {
        SegmentKey.Builder builder = SegmentKey.builder();
        builder.method(KeyMethod.parse(attributes.get(METHOD)));

        if (attributes.containsKey(URI)) {
            builder.uri(attributes.get(URI));
        }
        if (attributes.containsKey(IV)) {
            builder.iv(attributes.get(IV));
        }
        if (attributes.containsKey(KEYFORMAT)) {
            builder.keyFormat(attributes.get(KEYFORMAT));
        }
        if (attributes.containsKey(KEYFORMATVERSIONS)) {
            builder.keyFormatVersions(attributes.get(KEYFORMATVERSIONS));
        }

        return builder.build();
    }

    @Override
    String writeAttributes(SegmentKey segmentKey) {
        AttributeListBuilder attributes = new AttributeListBuilder();
        attributes.add(METHOD, segmentKey.method());
        segmentKey.uri().ifPresent(uri -> attributes.addQuoted(URI, uri));
        segmentKey.iv().ifPresent(iv -> attributes.add(IV, iv));
        segmentKey.keyFormat().ifPresent(keyFormat -> attributes.addQuoted(KEYFORMAT, keyFormat));
        segmentKey.keyFormatVersions().ifPresent(value -> attributes.addQuoted(KEYFORMATVERSIONS, value));
        return attributes.toString();
    }

    @Override
    Map<String, String> parseAttributes(String attributeList) {
        Matcher matcher = ATTRIBUTE_LIST_PATTERN.matcher(attributeList);
        Map<String, String> attributes = new HashMap<>();
        while (matcher.find()) {
            attributes.put(matcher.group(1), matcher.group(2) != null ? matcher.group(2) : matcher.group(3));
        }
        return attributes;
    }
}
