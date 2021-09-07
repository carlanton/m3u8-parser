package io.lindstrom.m3u8.parser;

import io.lindstrom.m3u8.model.Variant;

import java.util.Map;

/*
 * #EXT-X-STREAM-INF:<attribute-list>
 */
enum VariantAttribute implements Attribute<Variant, Variant.Builder> {
    BANDWIDTH {
        @Override
        public void read(Variant.Builder builder, String value) {
            builder.bandwidth(Long.parseLong(value));
        }

        @Override
        public void write(Variant value, TextBuilder textBuilder) {
            textBuilder.add(name(), String.valueOf(value.bandwidth()));
        }
    },

    AVERAGE_BANDWIDTH {
        @Override
        public void read(Variant.Builder builder, String value) {
            builder.averageBandwidth(Long.parseLong(value));
        }

        @Override
        public void write(Variant value, TextBuilder textBuilder) {
            value.averageBandwidth().ifPresent(v -> textBuilder.add(key(), String.valueOf(v)));
        }
    },

    SCORE {
        @Override
        public void read(Variant.Builder builder, String value) throws PlaylistParserException {
            builder.score(Double.parseDouble(value));
        }

        @Override
        public void write(Variant value, TextBuilder textBuilder) {
            value.score().ifPresent(v -> textBuilder.add(key(), v));
        }
    },

    CODECS {
        @Override
        public void read(Variant.Builder builder, String value) {
            builder.codecs(ParserUtils.split(value, ","));
        }

        @Override
        public void write(Variant value, TextBuilder textBuilder) {
            if (!value.codecs().isEmpty()) {
                textBuilder.addQuoted(name(), String.join(",", value.codecs()));
            }
        }
    },

    RESOLUTION {
        @Override
        public void read(Variant.Builder builder, String value) throws PlaylistParserException {
            builder.resolution(ParserUtils.parseResolution(value));
        }

        @Override
        public void write(Variant value, TextBuilder textBuilder) {
            value.resolution().ifPresent(v -> textBuilder.add(name(), ParserUtils.writeResolution(v)));
        }
    },

    FRAME_RATE {
        @Override
        public void read(Variant.Builder builder, String value) {
            builder.frameRate(Double.parseDouble(value));

        }

        @Override
        public void write(Variant value, TextBuilder textBuilder) {
            value.frameRate().ifPresent(v -> textBuilder.add(key(), Double.toString(v)));
        }
    },

    HDCP_LEVEL {
        @Override
        public void read(Variant.Builder builder, String value) {
            builder.hdcpLevel(value);
        }

        @Override
        public void write(Variant value, TextBuilder textBuilder) {
            value.hdcpLevel().ifPresent(v -> textBuilder.add(key(), v));
        }
    },

    ALLOWED_CPC {
        @Override
        public void read(Variant.Builder builder, String value) throws PlaylistParserException {
            builder.allowedCpc(ParserUtils.split(value, ","));
        }

        @Override
        public void write(Variant value, TextBuilder textBuilder) {
            if (!value.allowedCpc().isEmpty()) {
                textBuilder.addQuoted(key(), String.join(",", value.allowedCpc()));
            }
        }
    },

    STABLE_VARIANT_ID {
        @Override
        public void read(Variant.Builder builder, String value) throws PlaylistParserException {
            builder.stableVariantId(value);
        }

        @Override
        public void write(Variant value, TextBuilder textBuilder) {
            value.stableVariantId().ifPresent(v -> textBuilder.addQuoted(key(), v));
        }
    },

    AUDIO {
        @Override
        public void read(Variant.Builder builder, String value) {
            builder.audio(value);
        }

        @Override
        public void write(Variant value, TextBuilder textBuilder) {
            value.audio().ifPresent(v -> textBuilder.addQuoted(name(), v));
        }
    },

    VIDEO {
        @Override
        public void read(Variant.Builder builder, String value) {
            builder.video(value);
        }

        @Override
        public void write(Variant value, TextBuilder textBuilder) {
            value.video().ifPresent(v -> textBuilder.addQuoted(name(), v));
        }
    },

    SUBTITLES {
        @Override
        public void read(Variant.Builder builder, String value) {
            builder.subtitles(value);
        }

        @Override
        public void write(Variant value, TextBuilder textBuilder) {
            value.subtitles().ifPresent(v -> textBuilder.addQuoted(name(), v));
        }
    },

    CLOSED_CAPTIONS {
        private static final String NONE = "NONE";

        @Override
        public void read(Variant.Builder builder, String value) {
            if (value.equals(NONE)) {
                builder.closedCaptionsNone(true);
            } else {
                builder.closedCaptions(value);
            }
        }

        @Override
        public void write(Variant value, TextBuilder textBuilder) {
            if (value.closedCaptionsNone().orElse(false)) {
                textBuilder.add(key(), NONE);
            } else {
                value.closedCaptions().ifPresent(v -> textBuilder.addQuoted(key(), v));
            }
        }
    },

    PROGRAM_ID {
        @Override
        public void read(Variant.Builder builder, String value) {
            builder.programId(Integer.parseInt(value));
        }

        @Override
        public void write(Variant value, TextBuilder textBuilder) {
            value.programId().ifPresent(v -> textBuilder.add(key(), Integer.toString(v)));
        }
    },

    VIDEO_RANGE {
        @Override
        public void read(Variant.Builder builder, String value) {
            builder.videoRange(value);
        }

        @Override
        public void write(Variant value, TextBuilder textBuilder) {
            value.videoRange().ifPresent(v -> textBuilder.add(key(), v));
        }
    };

    final static Map<String, VariantAttribute> attributeMap = ParserUtils.toMap(values(), Attribute::key);

    static Variant parse(String attributes, String uri, ParsingMode parsingMode) throws PlaylistParserException {
        Variant.Builder builder = Variant.builder();
        ParserUtils.readAttributes(attributeMap, attributes, builder, parsingMode);
        builder.uri(uri);
        return builder.build();
    }
}
