package io.lindstrom.m3u8.parser;

import io.lindstrom.m3u8.model.SegmentMap;

import static io.lindstrom.m3u8.parser.Tags.*;

class SegmentMapParser extends AbstractTagParser<SegmentMap, SegmentMap.Builder> {
    private final ByteRangeParser byteRangeParser;

    SegmentMapParser(ByteRangeParser byteRangeParser) {
        super(EXT_X_MAP);
        this.byteRangeParser = byteRangeParser;
    }

    @Override
    void onAttribute(String attribute, String value, SegmentMap.Builder builder) throws PlaylistParserException {
        switch (attribute) {
            case URI:
                builder.uri(value);
                break;
            case BYTERANGE:
                builder.byteRange(byteRangeParser.parse(value));
                break;
            default:
                throw new PlaylistParserException("Unknown attribute: " + attribute);
        }
    }

    @Override
    void write(SegmentMap segmentMap, AttributeListBuilder attributes) {
        attributes.addQuoted(URI, segmentMap.uri());
        segmentMap.byteRange().map(byteRangeParser::writeAttributes).ifPresent(value ->
                attributes.addQuoted(BYTERANGE, value));
    }

    @Override
    SegmentMap.Builder newBuilder() {
        return SegmentMap.builder();
    }

    @Override
    SegmentMap build(SegmentMap.Builder builder) {
        return builder.build();
    }
}
