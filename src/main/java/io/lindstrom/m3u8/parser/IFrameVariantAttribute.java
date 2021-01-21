package io.lindstrom.m3u8.parser;

import io.lindstrom.m3u8.model.IFrameVariant;

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
    }
}
