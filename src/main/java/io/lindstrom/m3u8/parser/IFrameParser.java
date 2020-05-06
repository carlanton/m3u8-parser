package io.lindstrom.m3u8.parser;

import io.lindstrom.m3u8.model.IFrameVariant;

enum IFrameParser implements AttributeParser<IFrameVariant, IFrameVariant.Builder> {
    URI {
        @Override
        public void read(IFrameVariant.Builder builder, String value) {
            builder.uri(value);
        }

        @Override
        public void write(AttributeListBuilder attributes, IFrameVariant value) {
            attributes.addQuoted(name(), value.uri());
        }
    },

    BANDWIDTH {
        @Override
        public void read(IFrameVariant.Builder builder, String value) {
            builder.bandwidth(Long.parseLong(value));
        }

        @Override
        public void write(AttributeListBuilder attributes, IFrameVariant value) {
            attributes.add(name(), String.valueOf(value.bandwidth()));
        }
    },

    AVERAGE_BANDWIDTH {
        @Override
        public void read(IFrameVariant.Builder builder, String value) {
            builder.averageBandwidth(Long.parseLong(value));
        }

        @Override
        public void write(AttributeListBuilder attributes, IFrameVariant value) {
            value.averageBandwidth().ifPresent(v -> attributes.add(key(), String.valueOf(v)));
        }
    },

    CODECS {
        @Override
        public void read(IFrameVariant.Builder builder, String value) {
            builder.codecs(ParserUtils.split(value, ","));
        }

        @Override
        public void write(AttributeListBuilder attributes, IFrameVariant value) {
            if (!value.codecs().isEmpty()) {
                attributes.addQuoted(name(), String.join(",", value.codecs()));
            }
        }
    },

    RESOLUTION {
        @Override
        public void read(IFrameVariant.Builder builder, String value) throws PlaylistParserException {
            builder.resolution(ParserUtils.parseResolution(value));
        }

        @Override
        public void write(AttributeListBuilder attributes, IFrameVariant value) {
            value.resolution().ifPresent(v -> attributes.add(key(), ParserUtils.writeResolution(v)));
        }
    },

    HDCP_LEVEL {
        @Override
        public void read(IFrameVariant.Builder builder, String value) {
            builder.hdcpLevel(value);
        }

        @Override
        public void write(AttributeListBuilder attributes, IFrameVariant value) {
            value.hdcpLevel().ifPresent(v -> attributes.add(key(), v));
        }
    },

    VIDEO {
        @Override
        public void read(IFrameVariant.Builder builder, String value) {
            builder.video(value);
        }

        @Override
        public void write(AttributeListBuilder attributes, IFrameVariant value) {
            value.video().ifPresent(v -> attributes.addQuoted(name(), v));
        }
    },

    PROGRAM_ID {
        @Override
        public void read(IFrameVariant.Builder builder, String value) {
            builder.programId(Integer.parseInt(value));
        }

        @Override
        public void write(AttributeListBuilder attributes, IFrameVariant value) {
            value.programId().ifPresent(v -> attributes.add(key(), Integer.toString(v)));
        }
    },

    VIDEO_RANGE {
        @Override
        public void read(IFrameVariant.Builder builder, String value) {
            builder.videoRange(value);
        }

        @Override
        public void write(AttributeListBuilder attributes, IFrameVariant value) {
            value.videoRange().ifPresent(v -> attributes.add(key(), v));
        }
    };

    static TagParser<IFrameVariant> parser() {
        return new DefaultTagParser<>(
                Tags.EXT_X_I_FRAME_STREAM_INF,
                IFrameParser.class,
                builder -> builder.build(),
                IFrameVariant::builder
        );
    }
}
