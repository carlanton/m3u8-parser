package io.lindstrom.m3u8.parser;

import io.lindstrom.m3u8.model.SegmentMap;

import java.util.Map;

import static io.lindstrom.m3u8.parser.Tags.*;

class SegmentMapParser extends AbstractLineParser<SegmentMap> {
    private final ByteRangeParser byteRangeParser;

    SegmentMapParser(ByteRangeParser byteRangeParser) {
        super(EXT_X_MAP);
        this.byteRangeParser = byteRangeParser;
    }

    @Override
    SegmentMap parseAttributes(Map<String, String> attributes) throws PlaylistParserException {
        SegmentMap.Builder builder = SegmentMap.builder()
                .uri(attributes.get(URI));

        if (attributes.containsKey(BYTERANGE)) {
            builder.byteRange(byteRangeParser.parse(attributes.get(BYTERANGE)));
        }

        return builder.build();
    }

    @Override
    String writeAttributes(SegmentMap segmentMap) {
        AttributeListBuilder attributes = new AttributeListBuilder();
        attributes.addQuoted(URI, segmentMap.uri());
        segmentMap.byteRange().map(byteRangeParser::writeAttributes).ifPresent(value ->
                attributes.addQuoted(BYTERANGE, value));
        return attributes.toString();
    }
}
