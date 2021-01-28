package io.lindstrom.m3u8.parser;

import io.lindstrom.m3u8.model.PlaylistVariable;

import java.util.Map;

/*
 * #EXT-X-DEFINE:<attribute-list>
 */
public enum PlaylistVariableAttribute implements Attribute<PlaylistVariable, PlaylistVariable.Builder> {
    NAME {
        @Override
        public void read(PlaylistVariable.Builder builder, String value) {
            builder.name(name());
        }

        @Override
        public void write(PlaylistVariable value, TextBuilder textBuilder) {
            value.name().ifPresent(v -> textBuilder.addQuoted(name(), v));
        }
    },

    VALUE {
        @Override
        public void read(PlaylistVariable.Builder builder, String value) {
            builder.value(value);
        }

        @Override
        public void write(PlaylistVariable value, TextBuilder textBuilder) {
            value.value().ifPresent(v -> textBuilder.addQuoted(name(), v));
        }
    },

    IMPORT {
        @Override
        public void read(PlaylistVariable.Builder builder, String value) {
            builder.importAttribute(value);
        }

        @Override
        public void write(PlaylistVariable value, TextBuilder textBuilder) {
            value.importAttribute().ifPresent(v -> textBuilder.addQuoted(name(), v));
        }
    };

    final static Map<String, PlaylistVariableAttribute> attributeMap = ParserUtils.toMap(values(), Attribute::key);

    static PlaylistVariable parse(String attributes, ParsingMode parsingMode) throws PlaylistParserException {
        PlaylistVariable.Builder builder = PlaylistVariable.builder();
        ParserUtils.readAttributes(attributeMap, attributes, builder, parsingMode);
        return builder.build();
    }
}
