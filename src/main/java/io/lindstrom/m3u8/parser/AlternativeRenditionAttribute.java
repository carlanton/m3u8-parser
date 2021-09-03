package io.lindstrom.m3u8.parser;

import io.lindstrom.m3u8.model.AlternativeRendition;
import io.lindstrom.m3u8.model.MediaType;

import java.util.Map;

/*
 * #EXT-X-MEDIA:<attribute-list>
 */
enum AlternativeRenditionAttribute implements Attribute<AlternativeRendition, AlternativeRendition.Builder> {
    TYPE {
        @Override
        public void read(AlternativeRendition.Builder builder, String value) {
            builder.type(MediaType.parse(value));
        }

        @Override
        public void write(AlternativeRendition value, TextBuilder textBuilder) {
            textBuilder.add(key(), value.type());
        }
    },

    URI {
        @Override
        public void read(AlternativeRendition.Builder builder, String value) {
            builder.uri(value);
        }

        @Override
        public void write(AlternativeRendition value, TextBuilder textBuilder) {
            value.uri().ifPresent(uri -> textBuilder.addQuoted(key(), uri));
        }
    },

    GROUP_ID {
        @Override
        public void read(AlternativeRendition.Builder builder, String value) {
            builder.groupId(value);
        }

        @Override
        public void write(AlternativeRendition value, TextBuilder textBuilder) {
            textBuilder.addQuoted(key(), value.groupId());
        }
    },

    LANGUAGE {
        @Override
        public void read(AlternativeRendition.Builder builder, String value) {
            builder.language(value);
        }

        @Override
        public void write(AlternativeRendition value, TextBuilder textBuilder) {
            value.language().ifPresent(v -> textBuilder.addQuoted(key(), v));
        }
    },

    ASSOC_LANGUAGE {
        @Override
        public void read(AlternativeRendition.Builder builder, String value) {
            builder.assocLanguage(value);
        }

        @Override
        public void write(AlternativeRendition value, TextBuilder textBuilder) {
            value.assocLanguage().ifPresent(v -> textBuilder.addQuoted(key(), v));
        }
    },

    NAME {
        @Override
        public void read(AlternativeRendition.Builder builder, String value) {
            builder.name(value);
        }

        @Override
        public void write(AlternativeRendition value, TextBuilder textBuilder) {
            textBuilder.addQuoted(name(), value.name());
        }
    },

    STABLE_RENDITION_ID {
        @Override
        public void read(AlternativeRendition.Builder builder, String value) throws PlaylistParserException {
            builder.stableRenditionId(value);
        }

        @Override
        public void write(AlternativeRendition value, TextBuilder textBuilder) {
            value.stableRenditionId().ifPresent(v -> textBuilder.addQuoted(key(), v));
        }
    },

    DEFAULT {
        @Override
        public void read(AlternativeRendition.Builder builder, String value) throws PlaylistParserException {
            builder.defaultRendition(ParserUtils.yesOrNo(value));
        }

        @Override
        public void write(AlternativeRendition value, TextBuilder textBuilder) {
            value.defaultRendition().ifPresent(v -> textBuilder.add(name(), v));
        }
    },

    AUTOSELECT {
        @Override
        public void read(AlternativeRendition.Builder builder, String value) throws PlaylistParserException {
            builder.autoSelect(ParserUtils.yesOrNo(value));
        }

        @Override
        public void write(AlternativeRendition value, TextBuilder textBuilder) {
            value.autoSelect().ifPresent(v -> textBuilder.add(name(), v));
        }
    },

    FORCED {
        @Override
        public void read(AlternativeRendition.Builder builder, String value) throws PlaylistParserException {
            builder.forced(ParserUtils.yesOrNo(value));
        }

        @Override
        public void write(AlternativeRendition value, TextBuilder textBuilder) {
            value.forced().ifPresent(v -> textBuilder.add(name(), v));

        }
    },

    INSTREAM_ID {
        @Override
        public void read(AlternativeRendition.Builder builder, String value) {
            builder.inStreamId(value);
        }

        @Override
        public void write(AlternativeRendition value, TextBuilder textBuilder) {
            value.inStreamId().ifPresent(v -> textBuilder.addQuoted(key(), v));

        }
    },

    CHARACTERISTICS {
        @Override
        public void read(AlternativeRendition.Builder builder, String value) {
            builder.characteristics(ParserUtils.split(value, ","));
        }

        @Override
        public void write(AlternativeRendition value, TextBuilder textBuilder) {
            if (!value.characteristics().isEmpty()) {
                textBuilder.addQuoted(name(), String.join(",", value.characteristics()));
            }
        }
    },

    CHANNELS {
        @Override
        public void read(AlternativeRendition.Builder builder, String value) throws PlaylistParserException {
            builder.channels(ParserUtils.parseChannels(value));
        }

        @Override
        public void write(AlternativeRendition value, TextBuilder textBuilder) {
            value.channels().ifPresent(v -> textBuilder.addQuoted(name(), ParserUtils.writeChannels(v)));
        }
    };

    final static Map<String, AlternativeRenditionAttribute> attributeMap = ParserUtils.toMap(values(), Attribute::key);

    static AlternativeRendition parse(String attributes, ParsingMode parsingMode) throws PlaylistParserException {
        AlternativeRendition.Builder builder = AlternativeRendition.builder();
        ParserUtils.readAttributes(attributeMap, attributes, builder, parsingMode);
        return builder.build();
    }
}
