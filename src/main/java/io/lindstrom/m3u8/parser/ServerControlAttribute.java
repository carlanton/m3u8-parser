package io.lindstrom.m3u8.parser;

import io.lindstrom.m3u8.model.ServerControl;

import java.util.Map;

public enum ServerControlAttribute implements Attribute<ServerControl, ServerControl.Builder> {
    CAN_SKIP_UNTIL {
        @Override
        public void read(ServerControl.Builder builder, String value) {
            builder.canSkipUntil(Double.parseDouble(value));
        }

        @Override
        public void write(ServerControl value, TextBuilder textBuilder) {
            value.canSkipUntil().ifPresent(v -> textBuilder.add(key(), v));
        }
    },

    CAN_SKIP_DATERANGES {
        @Override
        public void read(ServerControl.Builder builder, String value) throws PlaylistParserException {
            builder.canSkipDateRanges(ParserUtils.yesOrNo(value));
        }

        @Override
        public void write(ServerControl value, TextBuilder textBuilder) {
            value.canSkipDateRanges().ifPresent(v -> textBuilder.add(key(), v));
        }
    },

    HOLD_BACK {
        @Override
        public void read(ServerControl.Builder builder, String value) {
            builder.holdBack(Double.parseDouble(value));
        }

        @Override
        public void write(ServerControl value, TextBuilder textBuilder) {
            value.holdBack().ifPresent(v -> textBuilder.add(key(), v));
        }
    },

    PART_HOLD_BACK {
        @Override
        public void read(ServerControl.Builder builder, String value) {
            builder.partHoldBack(Double.parseDouble(value));
        }

        @Override
        public void write(ServerControl value, TextBuilder textBuilder) {
            value.partHoldBack().ifPresent(v -> textBuilder.add(key(), v));
        }
    },

    CAN_BLOCK_RELOAD {
        @Override
        public void read(ServerControl.Builder builder, String value) throws PlaylistParserException {
            builder.canBlockReload(ParserUtils.yesOrNo(value));
        }

        @Override
        public void write(ServerControl value, TextBuilder textBuilder) {
            if (value.canBlockReload()) {
                textBuilder.add(key(), ParserUtils.YES);
            }
        }
    };

    final static Map<String, ServerControlAttribute> attributeMap = ParserUtils.toMap(values(), Attribute::key);

    static ServerControl parse(String attributes, ParsingMode parsingMode) throws PlaylistParserException {
        ServerControl.Builder builder = ServerControl.builder();
        ParserUtils.readAttributes(attributeMap, attributes, builder, parsingMode);
        return builder.build();
    }
}
