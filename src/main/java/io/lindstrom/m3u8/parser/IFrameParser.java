package io.lindstrom.m3u8.parser;

import io.lindstrom.m3u8.model.IFrameVariant;

import static io.lindstrom.m3u8.parser.Tags.*;

class IFrameParser extends AbstractTagParser<IFrameVariant, IFrameVariant.Builder> {
    IFrameParser() {
        super(EXT_X_I_FRAME_STREAM_INF);
    }

    @Override
    void onAttribute(String attribute, String value, IFrameVariant.Builder builder) throws PlaylistParserException {
        switch (attribute) {
            case URI:
                builder.uri(value);
                break;
            case BANDWIDTH:
                builder.bandwidth(Long.parseLong(value));
                break;
            case AVERAGE_BANDWIDTH:
                builder.averageBandwidth(Long.parseLong(value));
                break;
            case CODECS:
                builder.codecs(ParserUtils.split(value, ","));
                break;
            case RESOLUTION:
                builder.resolution(VariantParser.parseResolution(value));
                break;
            case HDCP_LEVEL:
                builder.hdcpLevel(value);
                break;
            case VIDEO:
                builder.video(value);
                break;
            case PROGRAM_ID:
                builder.programId(Integer.parseInt(value));
                break;
            case VIDEO_RANGE:
                builder.videoRange(value);
                break;
            default:
                throw new PlaylistParserException("Unknown attribute " + attribute);
        }
    }

    @Override
    void write(IFrameVariant iFramePlaylist, AttributeListBuilder attributes) {
        attributes.addQuoted(Tags.URI, iFramePlaylist.uri());
        attributes.add(Tags.BANDWIDTH, String.valueOf(iFramePlaylist.bandwidth()));
        iFramePlaylist.averageBandwidth().ifPresent(value -> attributes.add(Tags.AVERAGE_BANDWIDTH, String.valueOf(value)));
        if (!iFramePlaylist.codecs().isEmpty()) {
            attributes.addQuoted(Tags.CODECS, String.join(",", iFramePlaylist.codecs()));
        }
        iFramePlaylist.resolution().ifPresent(value -> attributes.add(Tags.RESOLUTION, VariantParser.writeResolution(value)));
        iFramePlaylist.hdcpLevel().ifPresent(value -> attributes.add(Tags.HDCP_LEVEL, value));
        iFramePlaylist.video().ifPresent(value -> attributes.addQuoted(Tags.VIDEO, value));
        iFramePlaylist.programId().ifPresent(value -> attributes.add(Tags.PROGRAM_ID, Integer.toString(value)));
        iFramePlaylist.videoRange().ifPresent(value -> attributes.add(VIDEO_RANGE, value));
    }

    @Override
    IFrameVariant.Builder newBuilder() {
        return IFrameVariant.builder();
    }

    @Override
    IFrameVariant build(IFrameVariant.Builder builder) {
        return builder.build();
    }
}
