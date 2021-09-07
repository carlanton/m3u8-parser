package io.lindstrom.m3u8.parser;

import io.lindstrom.m3u8.model.PartialSegment;

import java.util.Map;

public enum PartialSegmentAttribute implements Attribute<PartialSegment, PartialSegment.Builder> {
    URI {
        @Override
        public void read(PartialSegment.Builder builder, String value) throws PlaylistParserException {
            builder.uri(value);
        }

        @Override
        public void write(PartialSegment value, TextBuilder textBuilder) {
            textBuilder.addQuoted(name(), value.uri());
        }
    },
    DURATION {
        @Override
        public void read(PartialSegment.Builder builder, String value) throws PlaylistParserException {
            builder.duration(Double.parseDouble(value));
        }

        @Override
        public void write(PartialSegment value, TextBuilder textBuilder) {
            textBuilder.add(name(), value.duration());
        }
    },
    INDEPENDENT {
        @Override
        public void read(PartialSegment.Builder builder, String value) throws PlaylistParserException {
            builder.independent(ParserUtils.yesOrNo(value));
        }

        @Override
        public void write(PartialSegment value, TextBuilder textBuilder) {
            if (value.independent()) {
                textBuilder.add(name(), true);
            }
        }
    },
    BYTERANGE {
        @Override
        public void read(PartialSegment.Builder builder, String value) throws PlaylistParserException {
            builder.byterange(ParserUtils.parseByteRange(value));
        }

        @Override
        public void write(PartialSegment value, TextBuilder textBuilder) {
            value.byterange().ifPresent(v -> textBuilder.add(name(), ParserUtils.writeByteRange(v)));
        }
    },
    GAP {
        @Override
        public void read(PartialSegment.Builder builder, String value) throws PlaylistParserException {
            builder.gap(ParserUtils.yesOrNo(value));
        }

        @Override
        public void write(PartialSegment value, TextBuilder textBuilder) {
            if (value.gap()) {
                textBuilder.add(name(), true);
            }
        }
    };

    final static Map<String, PartialSegmentAttribute> attributeMap = ParserUtils.toMap(values(), Attribute::key);

    static PartialSegment parse(String attributes, ParsingMode parsingMode) throws PlaylistParserException {
        PartialSegment.Builder builder = PartialSegment.builder();
        ParserUtils.readAttributes(attributeMap, attributes, builder, parsingMode);
        return builder.build();
    }
}
