package io.lindstrom.m3u8.parser;

import io.lindstrom.m3u8.model.KeyMethod;
import io.lindstrom.m3u8.model.SegmentKey;

import static io.lindstrom.m3u8.parser.Tags.*;

class SegmentKeyParser extends AbstractTagParser<SegmentKey, SegmentKey.Builder> {
    SegmentKeyParser() {
        super(EXT_X_KEY);
    }

    SegmentKeyParser(String tag) {
        super(tag);
    }

    @Override
    void onAttribute(String attribute, String value, SegmentKey.Builder builder) throws PlaylistParserException {
        switch (attribute) {
            case METHOD:
                builder.method(KeyMethod.parse(value));
                break;
            case URI:
                builder.uri(value);
                break;
            case IV:
                builder.iv(value);
                break;
            case KEYFORMAT:
                builder.keyFormat(value);
                break;
            case KEYFORMATVERSIONS:
                builder.keyFormatVersions(value);
                break;
            default:
                throw new PlaylistParserException("Unknown attribute: " + attribute);
        }
    }

    @Override
    void write(SegmentKey segmentKey, AttributeListBuilder attributes) {
        attributes.add(METHOD, segmentKey.method());
        segmentKey.uri().ifPresent(uri -> attributes.addQuoted(URI, uri));
        segmentKey.iv().ifPresent(iv -> attributes.add(IV, iv));
        segmentKey.keyFormat().ifPresent(keyFormat -> attributes.addQuoted(KEYFORMAT, keyFormat));
        segmentKey.keyFormatVersions().ifPresent(value -> attributes.addQuoted(KEYFORMATVERSIONS, value));
    }

    @Override
    SegmentKey.Builder newBuilder() {
        return SegmentKey.builder();
    }

    @Override
    SegmentKey build(SegmentKey.Builder builder) {
        return builder.build();
    }
}
