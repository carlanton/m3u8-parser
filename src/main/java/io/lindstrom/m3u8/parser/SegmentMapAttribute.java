package io.lindstrom.m3u8.parser;

import io.lindstrom.m3u8.model.SegmentMap;

import java.util.Map;

/*
 * #EXT-X-MAP:<attribute-list>
 */
enum SegmentMapAttribute implements Attribute<SegmentMap, SegmentMap.Builder> {
    URI {
        @Override
        public void read(SegmentMap.Builder builder, String value) {
            builder.uri(value);
        }

        @Override
        public void write(SegmentMap value, TextBuilder textBuilder) {
            textBuilder.addQuoted(name(), value.uri());
        }
    },

    BYTERANGE {
        @Override
        public void read(SegmentMap.Builder builder, String value) throws PlaylistParserException {
            builder.byteRange(ParserUtils.parseByteRange(value));
        }

        @Override
        public void write(SegmentMap value, TextBuilder textBuilder) {
            value.byteRange().map(ParserUtils::writeByteRange).ifPresent(v ->
                    textBuilder.addQuoted(name(), v));
        }
    };

    final static Map<String, SegmentMapAttribute> attributeMap = ParserUtils.toMap(values(), Attribute::key);

    static SegmentMap parse(String attributes, ParsingMode parsingMode) throws PlaylistParserException {
        SegmentMap.Builder builder = SegmentMap.builder();
        ParserUtils.readAttributes(attributeMap, attributes, builder, parsingMode);
        return builder.build();
    }
}
