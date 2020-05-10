package io.lindstrom.m3u8.parser;

import io.lindstrom.m3u8.model.StartTimeOffset;

import static io.lindstrom.m3u8.parser.Tags.YES;

enum StartTimeOffsetAttribute implements Attribute<StartTimeOffset, StartTimeOffset.Builder> {
    TIME_OFFSET {
        @Override
        public void read(StartTimeOffset.Builder builder, String value) {
            builder.timeOffset(Double.parseDouble(value));
        }

        @Override
        public void write(StartTimeOffset value, TextBuilder textBuilder) {
            textBuilder.add(key(), String.valueOf(value.timeOffset()));
        }
    },

    PRECISE {
        @Override
        public void read(StartTimeOffset.Builder builder, String value) throws PlaylistParserException {
            builder.precise(ParserUtils.yesOrNo(value));
        }

        @Override
        public void write(StartTimeOffset value, TextBuilder textBuilder) {
            if (value.precise()) {
                textBuilder.add(name(), YES);
            }
        }
    };

    static StartTimeOffset parse(String attributes) throws PlaylistParserException {
        StartTimeOffset.Builder builder = StartTimeOffset.builder();
        AbstractPlaylistParser.readAttributes(StartTimeOffsetAttribute.class, attributes, builder);
        return builder.build();
    }
}
