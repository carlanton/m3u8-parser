package io.lindstrom.m3u8.parser;

import io.lindstrom.m3u8.model.SessionData;

import static io.lindstrom.m3u8.parser.Tags.EXT_X_SESSION_DATA;

enum SessionDataParser implements AttributeParser<SessionData, SessionData.Builder> {
    DATA_ID {
        @Override
        public void read(SessionData.Builder builder, String value) {
            builder.dataId(value);
        }

        @Override
        public void write(AttributeListBuilder attributes, SessionData value) {
            attributes.addQuoted(key(), value.dataId());
        }
    },

    VALUE {
        @Override
        public void read(SessionData.Builder builder, String value) {
            builder.value(value);
        }

        @Override
        public void write(AttributeListBuilder attributes, SessionData value) {
            value.value().ifPresent(v -> attributes.addQuoted(name(), v));
        }
    },

    URI {
        @Override
        public void read(SessionData.Builder builder, String value) {
            builder.uri(value);
        }

        @Override
        public void write(AttributeListBuilder attributes, SessionData value) {
            value.uri().ifPresent(v -> attributes.addQuoted(name(), v));
        }
    },

    LANGUAGE {
        @Override
        public void read(SessionData.Builder builder, String value) {
            builder.language(value);
        }

        @Override
        public void write(AttributeListBuilder attributes, SessionData value) {
            value.language().ifPresent(v -> attributes.addQuoted(name(), v));
        }
    };

    static TagParser<SessionData> parser() {
        return new DefaultTagParser<>(
                EXT_X_SESSION_DATA, SessionDataParser.class,
                builder -> builder.build(),
                SessionData::builder
        );
    }
}
