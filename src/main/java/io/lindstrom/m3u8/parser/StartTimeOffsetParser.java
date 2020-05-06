package io.lindstrom.m3u8.parser;

import io.lindstrom.m3u8.model.StartTimeOffset;

import static io.lindstrom.m3u8.parser.Tags.*;

enum StartTimeOffsetParser implements AttributeParser<StartTimeOffset, StartTimeOffset.Builder> {
    TIME_OFFSET {
        @Override
        public void read(StartTimeOffset.Builder builder, String value) {
            builder.timeOffset(Double.parseDouble(value));
        }

        @Override
        public void write(AttributeListBuilder attributes, StartTimeOffset value) {
            attributes.add(key(), String.valueOf(value.timeOffset()));
        }
    },

    PRECISE {
        @Override
        public void read(StartTimeOffset.Builder builder, String value) throws PlaylistParserException {
            builder.precise(ParserUtils.yesOrNo(value));
        }

        @Override
        public void write(AttributeListBuilder attributes, StartTimeOffset value) {
            if (value.precise()) {
                attributes.add(name(), YES);
            }
        }
    };

    static TagParser<StartTimeOffset> parser() {
        return new DefaultTagParser<>(
                EXT_X_START,
                StartTimeOffsetParser.class,
                builder -> builder.build(),
                StartTimeOffset::builder
        );
    }
}
