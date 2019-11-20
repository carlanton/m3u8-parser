package io.lindstrom.m3u8.parser;

import io.lindstrom.m3u8.model.StartTimeOffset;

import java.util.Map;

import static io.lindstrom.m3u8.parser.Tags.*;

class StartTimeOffsetParser extends AbstractLineParser<StartTimeOffset> {
    StartTimeOffsetParser() {
        super(EXT_X_START);
    }

    @Override
    StartTimeOffset parseAttributes(Map<String, String> attributes) throws PlaylistParserException {
        StartTimeOffset.Builder builder = StartTimeOffset.builder();
        for (Map.Entry<String, String> entry : attributes.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            switch (key) {
                case TIME_OFFSET:
                    builder.timeOffset(Double.parseDouble(value));
                    break;

                case PRECISE:
                    builder.precise(ParserUtils.yesOrNo(value));
                    break;

                default:
                    throw new PlaylistParserException("Unknown key " + key);
            }
        }
        return builder.build();
    }

    @Override
    String writeAttributes(StartTimeOffset startTimeOffset) {
        AttributeListBuilder attributes = new AttributeListBuilder();

        attributes.add(TIME_OFFSET, String.valueOf(startTimeOffset.timeOffset()));
        if (startTimeOffset.precise()) {
            attributes.add(PRECISE, YES);
        }

        return attributes.toString();
    }
}
