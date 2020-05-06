package io.lindstrom.m3u8.parser;

import io.lindstrom.m3u8.model.AlternativeRendition;
import io.lindstrom.m3u8.model.MediaType;

import static io.lindstrom.m3u8.parser.Tags.EXT_X_MEDIA;

enum AlternativeRenditionParser implements AttributeParser<AlternativeRendition, AlternativeRendition.Builder> {
    TYPE {
        @Override
        public void read(AlternativeRendition.Builder builder, String value) {
            builder.type(MediaType.parse(value));
        }

        @Override
        public void write(AttributeListBuilder attributes, AlternativeRendition value) {
            attributes.add(key(), value.type());
        }
    },

    URI {
        @Override
        public void read(AlternativeRendition.Builder builder, String value) {
            builder.uri(value);
        }

        @Override
        public void write(AttributeListBuilder attributes, AlternativeRendition value) {
            value.uri().ifPresent(uri -> attributes.addQuoted(key(), uri));
        }
    },

    GROUP_ID {
        @Override
        public void read(AlternativeRendition.Builder builder, String value) {
            builder.groupId(value);
        }

        @Override
        public void write(AttributeListBuilder attributes, AlternativeRendition value) {
            attributes.addQuoted(key(), value.groupId());
        }
    },

    LANGUAGE {
        @Override
        public void read(AlternativeRendition.Builder builder, String value) {
            builder.language(value);
        }

        @Override
        public void write(AttributeListBuilder attributes, AlternativeRendition value) {
            value.language().ifPresent(v -> attributes.addQuoted(key(), v));
        }
    },

    ASSOC_LANGUAGE {
        @Override
        public void read(AlternativeRendition.Builder builder, String value) {
            builder.assocLanguage(value);
        }

        @Override
        public void write(AttributeListBuilder attributes, AlternativeRendition value) {
            value.assocLanguage().ifPresent(v -> attributes.addQuoted(key(), v));
        }
    },

    NAME {
        @Override
        public void read(AlternativeRendition.Builder builder, String value) {
            builder.name(value);
        }

        @Override
        public void write(AttributeListBuilder attributes, AlternativeRendition value) {
            attributes.addQuoted(name(), value.name());
        }
    },

    DEFAULT {
        @Override
        public void read(AlternativeRendition.Builder builder, String value) throws PlaylistParserException {
            builder.defaultRendition(ParserUtils.yesOrNo(value));
        }

        @Override
        public void write(AttributeListBuilder attributes, AlternativeRendition value) {
            value.defaultRendition().ifPresent(v -> attributes.add(name(), v));
        }
    },

    AUTOSELECT {
        @Override
        public void read(AlternativeRendition.Builder builder, String value) throws PlaylistParserException {
            builder.autoSelect(ParserUtils.yesOrNo(value));
        }

        @Override
        public void write(AttributeListBuilder attributes, AlternativeRendition value) {
            value.autoSelect().ifPresent(v -> attributes.add(name(), v));
        }
    },

    FORCED {
        @Override
        public void read(AlternativeRendition.Builder builder, String value) throws PlaylistParserException {
            builder.forced(ParserUtils.yesOrNo(value));
        }

        @Override
        public void write(AttributeListBuilder attributes, AlternativeRendition value) {
            value.forced().ifPresent(v -> attributes.add(name(), v));

        }
    },

    INSTREAM_ID {
        @Override
        public void read(AlternativeRendition.Builder builder, String value) {
            builder.inStreamId(value);
        }

        @Override
        public void write(AttributeListBuilder attributes, AlternativeRendition value) {
            value.inStreamId().ifPresent(v -> attributes.addQuoted(key(), v));

        }
    },

    CHARACTERISTICS {
        @Override
        public void read(AlternativeRendition.Builder builder, String value) {
            builder.characteristics(ParserUtils.split(value, ","));
        }

        @Override
        public void write(AttributeListBuilder attributes, AlternativeRendition value) {
            if (!value.characteristics().isEmpty()) {
                attributes.addQuoted(name(), String.join(",", value.characteristics()));
            }
        }
    },

    CHANNELS {
        @Override
        public void read(AlternativeRendition.Builder builder, String value) {
            builder.channels(ParserUtils.split(value, "/"));
        }

        @Override
        public void write(AttributeListBuilder attributes, AlternativeRendition value) {
            if (!value.channels().isEmpty()) {
                attributes.addQuoted(name(), String.join("/", value.channels()));
            }
        }
    };

    static TagParser<AlternativeRendition> parser() {
        return new DefaultTagParser<>(
                EXT_X_MEDIA,
                AlternativeRenditionParser.class,
                builder -> builder.build(),
                AlternativeRendition::builder
        );
    }
}
