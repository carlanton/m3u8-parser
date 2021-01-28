package io.lindstrom.m3u8.parser;

import io.lindstrom.m3u8.model.SessionData;

import java.util.Map;

/*
 * #EXT-X-SESSION-DATA:<attribute-list>
 */
enum SessionDataAttribute implements Attribute<SessionData, SessionData.Builder> {
    DATA_ID {
        @Override
        public void read(SessionData.Builder builder, String value) {
            builder.dataId(value);
        }

        @Override
        public void write(SessionData value, TextBuilder textBuilder) {
            textBuilder.addQuoted(key(), value.dataId());
        }
    },

    VALUE {
        @Override
        public void read(SessionData.Builder builder, String value) {
            builder.value(value);
        }

        @Override
        public void write(SessionData value, TextBuilder textBuilder) {
            value.value().ifPresent(v -> textBuilder.addQuoted(name(), v));
        }
    },

    URI {
        @Override
        public void read(SessionData.Builder builder, String value) {
            builder.uri(value);
        }

        @Override
        public void write(SessionData value, TextBuilder textBuilder) {
            value.uri().ifPresent(v -> textBuilder.addQuoted(name(), v));
        }
    },

    LANGUAGE {
        @Override
        public void read(SessionData.Builder builder, String value) {
            builder.language(value);
        }

        @Override
        public void write(SessionData value, TextBuilder textBuilder) {
            value.language().ifPresent(v -> textBuilder.addQuoted(name(), v));
        }
    };

    final static Map<String, SessionDataAttribute> attributeMap = ParserUtils.toMap(values(), Attribute::key);

    static SessionData parse(String attributes, ParsingMode parsingMode) throws PlaylistParserException {
        SessionData.Builder builder = SessionData.builder();
        ParserUtils.readAttributes(attributeMap, attributes, builder, parsingMode);
        return builder.build();
    }
}
