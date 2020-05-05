package io.lindstrom.m3u8.parser;

import io.lindstrom.m3u8.model.StartTimeOffset;

import static io.lindstrom.m3u8.parser.Tags.*;

class StartTimeOffsetParser extends AbstractTagParser<StartTimeOffset, StartTimeOffset.Builder> {
    StartTimeOffsetParser() {
        super(EXT_X_START);
    }

    @Override
    void onAttribute(String attribute, String value, StartTimeOffset.Builder builder) throws PlaylistParserException {
        switch (attribute) {
            case TIME_OFFSET:
                builder.timeOffset(Double.parseDouble(value));
                break;

            case PRECISE:
                builder.precise(ParserUtils.yesOrNo(value));
                break;

            default:
                throw new PlaylistParserException("Unknown attribute " + attribute);
        }
    }

    @Override
    void write(StartTimeOffset startTimeOffset, AttributeListBuilder attributes) {
        attributes.add(TIME_OFFSET, String.valueOf(startTimeOffset.timeOffset()));
        if (startTimeOffset.precise()) {
            attributes.add(PRECISE, YES);
        }
    }

    @Override
    StartTimeOffset.Builder newBuilder() {
        return StartTimeOffset.builder();
    }

    @Override
    StartTimeOffset build(StartTimeOffset.Builder builder) {
        return builder.build();
    }
}
