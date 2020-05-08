package io.lindstrom.m3u8.parser;

import io.lindstrom.m3u8.model.Variant;

enum VariantParser implements Attribute<Variant, Variant.Builder> {
    BANDWIDTH {
        @Override
        public void read(Variant.Builder builder, String value) {
            builder.bandwidth(Long.parseLong(value));
        }

        @Override
        public void write(AttributeListBuilder attributes, Variant value) {
            attributes.add(name(), String.valueOf(value.bandwidth()));
        }
    },

    AVERAGE_BANDWIDTH {
        @Override
        public void read(Variant.Builder builder, String value) {
            builder.averageBandwidth(Long.parseLong(value));
        }

        @Override
        public void write(AttributeListBuilder attributes, Variant value) {
            value.averageBandwidth().ifPresent(v -> attributes.add(key(), String.valueOf(v)));
        }
    },

    CODECS {
        @Override
        public void read(Variant.Builder builder, String value) {
            builder.codecs(ParserUtils.split(value, ","));
        }

        @Override
        public void write(AttributeListBuilder attributes, Variant value) {
            if (!value.codecs().isEmpty()) {
                attributes.addQuoted(name(), String.join(",", value.codecs()));
            }
        }
    },

    RESOLUTION {
        @Override
        public void read(Variant.Builder builder, String value) throws PlaylistParserException {
            builder.resolution(ParserUtils.parseResolution(value));
        }

        @Override
        public void write(AttributeListBuilder attributes, Variant value) {
            value.resolution().ifPresent(v -> attributes.add(name(), ParserUtils.writeResolution(v)));
        }
    },

    FRAME_RATE {
        @Override
        public void read(Variant.Builder builder, String value) {
            builder.frameRate(Double.parseDouble(value));

        }

        @Override
        public void write(AttributeListBuilder attributes, Variant value) {
            value.frameRate().ifPresent(v -> attributes.add(key(), Double.toString(v)));
        }
    },

    HDCP_LEVEL {
        @Override
        public void read(Variant.Builder builder, String value) throws PlaylistParserException {
            builder.hdcpLevel(value);
        }

        @Override
        public void write(AttributeListBuilder attributes, Variant value) {
            value.hdcpLevel().ifPresent(v -> attributes.add(key(), v));
        }
    },

    AUDIO {
        @Override
        public void read(Variant.Builder builder, String value) throws PlaylistParserException {
            builder.audio(value);
        }

        @Override
        public void write(AttributeListBuilder attributes, Variant value) {
            value.audio().ifPresent(v -> attributes.addQuoted(name(), v));
        }
    },

    VIDEO {
        @Override
        public void read(Variant.Builder builder, String value) throws PlaylistParserException {
            builder.video(value);
        }

        @Override
        public void write(AttributeListBuilder attributes, Variant value) {
            value.video().ifPresent(v -> attributes.addQuoted(name(), v));
        }
    },

    SUBTITLES {
        @Override
        public void read(Variant.Builder builder, String value) throws PlaylistParserException {
            builder.subtitles(value);
        }

        @Override
        public void write(AttributeListBuilder attributes, Variant value) {
            value.subtitles().ifPresent(v -> attributes.addQuoted(name(), v));
        }
    },

    CLOSED_CAPTIONS {
        private static final String NONE = "NONE";

        @Override
        public void read(Variant.Builder builder, String value) throws PlaylistParserException {
            if (value.equals(NONE)) {
                builder.closedCaptionsNone(true);
            } else {
                builder.closedCaptions(value);
            }
        }

        @Override
        public void write(AttributeListBuilder attributes, Variant value) {
            if (value.closedCaptionsNone().orElse(false)) {
                attributes.add(key(), NONE);
            } else {
                value.closedCaptions().ifPresent(v -> attributes.addQuoted(key(), v));
            }
        }
    },

    PROGRAM_ID {
        @Override
        public void read(Variant.Builder builder, String value) throws PlaylistParserException {
            builder.programId(Integer.parseInt(value));
        }

        @Override
        public void write(AttributeListBuilder attributes, Variant value) {
            value.programId().ifPresent(v -> attributes.add(key(), Integer.toString(v)));
        }
    },

    VIDEO_RANGE {
        @Override
        public void read(Variant.Builder builder, String value) throws PlaylistParserException {
            builder.videoRange(value);
        }

        @Override
        public void write(AttributeListBuilder attributes, Variant value) {
            value.videoRange().ifPresent(v -> attributes.add(key(), v));
        }
    },

    URI {
        @Override
        public void read(Variant.Builder builder, String value) {
            builder.uri(value);
        }

        @Override
        public void write(AttributeListBuilder attributes, Variant value) {
            attributes.addRaw("\n" + value.uri());
        }
    }
}
