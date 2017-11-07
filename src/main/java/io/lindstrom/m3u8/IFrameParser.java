package io.lindstrom.m3u8;

import io.lindstrom.m3u8.model.IFrameVariant;
import io.lindstrom.m3u8.util.AttributeListBuilder;
import io.lindstrom.m3u8.util.ParserUtils;

import java.util.Map;

import static io.lindstrom.m3u8.Tags.*;

class IFrameParser extends AbstractLineParser<IFrameVariant> {
    IFrameParser() {
        super(EXT_X_I_FRAME_STREAM_INF);
    }

    @Override
    protected IFrameVariant parseAttributes(Map<String, String> attributes) {
        IFrameVariant.Builder builder = IFrameVariant.builder();
        for (Map.Entry<String, String> entry : attributes.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            switch (key) {
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
                default:
                    throw new RuntimeException("Unknown key " + key);
            }
        }
        return builder.build();
    }

    @Override
    protected String writeAttributes(IFrameVariant iFramePlaylist) {
        AttributeListBuilder attributes = new AttributeListBuilder();

        attributes.addQuoted(Tags.URI, iFramePlaylist.uri());
        attributes.add(Tags.BANDWIDTH, String.valueOf(iFramePlaylist.bandwidth()));
        iFramePlaylist.averageBandwidth().ifPresent(value -> attributes.add(Tags.AVERAGE_BANDWIDTH, String.valueOf(value)));
        if (!iFramePlaylist.codecs().isEmpty()) {
            attributes.addQuoted(Tags.CODECS, String.join(",", iFramePlaylist.codecs()));
        }
        iFramePlaylist.resolution().ifPresent(value -> attributes.add(Tags.RESOLUTION, VariantParser.writeResolution(value)));
        iFramePlaylist.hdcpLevel().ifPresent(value -> attributes.add(Tags.HDCP_LEVEL, value));
        iFramePlaylist.video().ifPresent(value -> attributes.addQuoted(Tags.VIDEO, value));

        return attributes.toString();
    }
}
