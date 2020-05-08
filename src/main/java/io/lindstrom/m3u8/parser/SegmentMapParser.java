package io.lindstrom.m3u8.parser;

import io.lindstrom.m3u8.model.SegmentMap;

enum SegmentMapParser implements AttributeMapper<SegmentMap, SegmentMap.Builder> {
    URI {
        @Override
        public void read(SegmentMap.Builder builder, String value) throws PlaylistParserException {
            builder.uri(value);
        }

        @Override
        public void write(AttributeListBuilder attributes, SegmentMap value) {
            attributes.addQuoted(name(), value.uri());
        }
    },

    BYTERANGE {
        @Override
        public void read(SegmentMap.Builder builder, String value) throws PlaylistParserException {
            builder.byteRange(byteRangeParser.parse(value));
        }

        @Override
        public void write(AttributeListBuilder attributes, SegmentMap value) {
            value.byteRange().map(byteRangeParser::writeAttributes).ifPresent(v ->
                    attributes.addQuoted(name(), v));
        }
    };

    private static final ByteRangeParser byteRangeParser = new ByteRangeParser();
}
