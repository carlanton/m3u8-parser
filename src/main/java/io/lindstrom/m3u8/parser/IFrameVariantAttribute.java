package io.lindstrom.m3u8.parser;

import io.lindstrom.m3u8.model.IFrameVariant;

import java.util.Map;

/*
 * #EXT-X-I-FRAME-STREAM-INF:<attribute-list>
 */
enum IFrameVariantAttribute implements Attribute<IFrameVariant, IFrameVariant.Builder> {
    URI {
        @Override
        public void read(IFrameVariant.Builder builder, String value) {
            builder.uri(value);
        }

        @Override
        public void write(IFrameVariant value, TextBuilder textBuilder) {
            textBuilder.addQuoted(name(), value.uri());
        }
    },

    BANDWIDTH {
        @Override
        public void read(IFrameVariant.Builder builder, String value) {
            builder.bandwidth(Long.parseLong(value));
        }

        @Override
        public void write(IFrameVariant value, TextBuilder textBuilder) {
            textBuilder.add(name(), String.valueOf(value.bandwidth()));
        }
    },

    AVERAGE_BANDWIDTH {
        @Override
        public void read(IFrameVariant.Builder builder, String value) {
            builder.averageBandwidth(Long.parseLong(value));
        }

        @Override
        public void write(IFrameVariant value, TextBuilder textBuilder) {
            value.averageBandwidth().ifPresent(v -> textBuilder.add(key(), String.valueOf(v)));
        }
    },

    SCORE {
        @Override
        public void read(IFrameVariant.Builder builder, String value) throws PlaylistParserException {
            builder.score(Double.parseDouble(value));
        }

        @Override
        public void write(IFrameVariant value, TextBuilder textBuilder) {
            value.score().ifPresent(v -> textBuilder.add(key(),v));
        }
    },

    CODECS {
        @Override
        public void read(IFrameVariant.Builder builder, String value) {
            builder.codecs(ParserUtils.split(value, ","));
        }

        @Override
        public void write(IFrameVariant value, TextBuilder textBuilder) {
            if (!value.codecs().isEmpty()) {
                textBuilder.addQuoted(name(), String.join(",", value.codecs()));
            }
        }
    },

    RESOLUTION {
        @Override
        public void read(IFrameVariant.Builder builder, String value) throws PlaylistParserException {
            builder.resolution(ParserUtils.parseResolution(value));
        }

        @Override
        public void write(IFrameVariant value, TextBuilder textBuilder) {
            value.resolution().ifPresent(v -> textBuilder.add(key(), ParserUtils.writeResolution(v)));
        }
    },

    HDCP_LEVEL {
        @Override
        public void read(IFrameVariant.Builder builder, String value) {
            builder.hdcpLevel(value);
        }

        @Override
        public void write(IFrameVariant value, TextBuilder textBuilder) {
            value.hdcpLevel().ifPresent(v -> textBuilder.add(key(), v));
        }
    },

    ALLOWED_CPC {
        @Override
        public void read(IFrameVariant.Builder builder, String value) throws PlaylistParserException {
            builder.allowedCpc(ParserUtils.split(value,","));
        }

        @Override
        public void write(IFrameVariant value, TextBuilder textBuilder) {
            if (!value.allowedCpc().isEmpty()) {
                textBuilder.addQuoted(key(), String.join(",", value.allowedCpc()));
            }
        }
    },

    STABLE_VARIANT_ID {
        @Override
        public void read(IFrameVariant.Builder builder, String value) throws PlaylistParserException {
            builder.stableVariantId(value);
        }

        @Override
        public void write(IFrameVariant value, TextBuilder textBuilder) {
            value.stableVariantId().ifPresent(v -> textBuilder.addQuoted(key(), v));
        }
    },

    VIDEO {
        @Override
        public void read(IFrameVariant.Builder builder, String value) {
            builder.video(value);
        }

        @Override
        public void write(IFrameVariant value, TextBuilder textBuilder) {
            value.video().ifPresent(v -> textBuilder.addQuoted(name(), v));
        }
    },

    PROGRAM_ID {
        @Override
        public void read(IFrameVariant.Builder builder, String value) {
            builder.programId(Integer.parseInt(value));
        }

        @Override
        public void write(IFrameVariant value, TextBuilder textBuilder) {
            value.programId().ifPresent(v -> textBuilder.add(key(), Integer.toString(v)));
        }
    },

    VIDEO_RANGE {
        @Override
        public void read(IFrameVariant.Builder builder, String value) {
            builder.videoRange(value);
        }

        @Override
        public void write(IFrameVariant value, TextBuilder textBuilder) {
            value.videoRange().ifPresent(v -> textBuilder.add(key(), v));
        }
    },

    NAME {
        @Override
        public void read(IFrameVariant.Builder builder, String value) throws PlaylistParserException {
            builder.name(value);
        }

        @Override
        public void write(IFrameVariant value, TextBuilder textBuilder) {
            value.name().ifPresent(v -> textBuilder.addQuoted(key(), v));
        }
    },

    LANGUAGE {
        @Override
        public void read(IFrameVariant.Builder builder, String value) throws PlaylistParserException {
            builder.language(value);
        }

        @Override
        public void write(IFrameVariant value, TextBuilder textBuilder) {
            value.language().ifPresent(v -> textBuilder.addQuoted(key(), v));
        }
    };

    final static Map<String, IFrameVariantAttribute> attributeMap = ParserUtils.toMap(values(), Attribute::key);

    static IFrameVariant parse(String attributes, ParsingMode parsingMode) throws PlaylistParserException {
        IFrameVariant.Builder builder = IFrameVariant.builder();
        ParserUtils.readAttributes(attributeMap, attributes, builder, parsingMode);
        return builder.build();
    }
}
