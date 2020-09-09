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

    private final static Map<String, PlaylistVariableAttribute> values = ParserUtils.attributeMap(values());

    static PlaylistVariable parse(String attributes) throws PlaylistParserException {
        PlaylistVariable.Builder builder = PlaylistVariable.builder();
        ParserUtils.readAttributes(values, attributes, builder);
        return builder.build();
    }
}
