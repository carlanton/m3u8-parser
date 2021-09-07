package io.lindstrom.m3u8.parser;

import io.lindstrom.m3u8.model.PreloadHint;
import io.lindstrom.m3u8.model.PreloadHintType;

import java.util.Map;

public enum PreloadHintAttribute implements Attribute<PreloadHint, PreloadHint.Builder> {

    TYPE {
        @Override
        public void read(PreloadHint.Builder builder, String value) throws PlaylistParserException {
            builder.type(PreloadHintType.valueOf(value));
        }

        @Override
        public void write(PreloadHint value, TextBuilder textBuilder) {
            textBuilder.add(name(), value.type().name());
        }
    },

    URI {
        @Override
        public void read(PreloadHint.Builder builder, String value) throws PlaylistParserException {
            builder.uri(value);
        }

        @Override
        public void write(PreloadHint value, TextBuilder textBuilder) {
            textBuilder.addQuoted(name(), value.uri());
        }
    },

    BYTERANGE_START {
        @Override
        public void read(PreloadHint.Builder builder, String value) throws PlaylistParserException {
            builder.byteRangeStart(Long.parseLong(value));
        }

        @Override
        public void write(PreloadHint value, TextBuilder textBuilder) {
            value.byteRangeStart().ifPresent(v -> textBuilder.add(key(), Long.toString(v)));
        }
    },

    BYTERANGE_LENGTH {
        @Override
        public void read(PreloadHint.Builder builder, String value) throws PlaylistParserException {
            builder.byteRangeLength(Long.parseLong(value));
        }

        @Override
        public void write(PreloadHint value, TextBuilder textBuilder) {
            value.byteRangeLength().ifPresent(v -> textBuilder.add(key(), Long.toString(v)));
        }
    };

    final static Map<String, PreloadHintAttribute> attributeMap = ParserUtils.toMap(values(), Attribute::key);

    static PreloadHint parse(String attributes, ParsingMode parsingMode) throws PlaylistParserException {
        PreloadHint.Builder builder = PreloadHint.builder();
        ParserUtils.readAttributes(attributeMap, attributes, builder, parsingMode);
        return builder.build();
    }

}
