package io.lindstrom.m3u8.parser;

import io.lindstrom.m3u8.model.KeyMethod;
import io.lindstrom.m3u8.model.SegmentKey;

import java.util.Map;

/*
 * #EXT-X-KEY:<attribute-list>
 */
public enum SegmentKeyAttribute implements Attribute<SegmentKey, SegmentKey.Builder> {
    METHOD {
        @Override
        public void read(SegmentKey.Builder builder, String value) {
            builder.method(KeyMethod.parse(value));
        }

        @Override
        public void write(SegmentKey segmentKey, TextBuilder textBuilder) {
            textBuilder.add(name(), segmentKey.method());
        }
    },

    URI {
        @Override
        public void read(SegmentKey.Builder builder, String value) {
            builder.uri(value);
        }

        @Override
        public void write(SegmentKey segmentKey, TextBuilder textBuilder) {
            segmentKey.uri().ifPresent(uri -> textBuilder.addQuoted(name(), uri));
        }
    },

    IV {
        @Override
        public void read(SegmentKey.Builder builder, String value) {
            builder.iv(value);
        }

        @Override
        public void write(SegmentKey segmentKey, TextBuilder textBuilder) {
            segmentKey.iv().ifPresent(iv -> textBuilder.add(name(), iv));
        }
    },

    KEYFORMAT {
        @Override
        public void read(SegmentKey.Builder builder, String value) {
            builder.keyFormat(value);
        }

        @Override
        public void write(SegmentKey segmentKey, TextBuilder textBuilder) {
            segmentKey.keyFormat().ifPresent(keyFormat -> textBuilder.addQuoted(name(), keyFormat));
        }
    },

    KEYFORMATVERSIONS {
        @Override
        public void read(SegmentKey.Builder builder, String value) {
            builder.keyFormatVersions(value);
        }

        @Override
        public void write(SegmentKey segmentKey, TextBuilder textBuilder) {
            segmentKey.keyFormatVersions().ifPresent(v -> textBuilder.addQuoted(name(), v));
        }
    };

    final static Map<String, SegmentKeyAttribute> attributeMap = ParserUtils.toMap(values(), Attribute::key);

    static SegmentKey parse(String attributes, ParsingMode parsingMode) throws PlaylistParserException {
        SegmentKey.Builder builder = SegmentKey.builder();
        ParserUtils.readAttributes(attributeMap, attributes, builder, parsingMode);
        return builder.build();
    }
}
