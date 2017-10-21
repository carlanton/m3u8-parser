package io.lindstrom.m3u8.parser;

import com.google.common.base.Splitter;
import io.lindstrom.m3u8.Tags;
import io.lindstrom.m3u8.model.IFramePlaylist;
import io.lindstrom.m3u8.util.AttributeListBuilder;

import java.util.Map;

import static io.lindstrom.m3u8.Tags.*;

public class IFramePlaylistParser implements Parser<IFramePlaylist> {
    private static final Splitter SPLITTER_COMMA = Splitter.on(',').trimResults();

    @Override
    public IFramePlaylist parse(Map<String, String> attributes) {
        IFramePlaylist.Builder builder = IFramePlaylist.builder();
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
                    builder.codecs(SPLITTER_COMMA.split(value));
                    break;
                case RESOLUTION:
                    builder.resolution(value);
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
        return builder.build();    }

    @Override
    public String write(IFramePlaylist iFramePlaylist) {
        AttributeListBuilder attributes = new AttributeListBuilder();

        attributes.addQuoted(Tags.URI, iFramePlaylist.uri());
        attributes.add(Tags.BANDWIDTH, String.valueOf(iFramePlaylist.bandwidth()));
        iFramePlaylist.averageBandwidth().ifPresent(value -> attributes.add(Tags.AVERAGE_BANDWIDTH, String.valueOf(value)));
        if (!iFramePlaylist.codecs().isEmpty()) {
            attributes.addQuoted(Tags.CODECS, String.join(",", iFramePlaylist.codecs()));
        }
        iFramePlaylist.resolution().ifPresent(value -> attributes.add(Tags.RESOLUTION, value));
        iFramePlaylist.hdcpLevel().ifPresent(value -> attributes.add(Tags.HDCP_LEVEL, value));
        iFramePlaylist.video().ifPresent(value -> attributes.addQuoted(Tags.VIDEO, value));

        return String.format("%s:%s\n", Tags.EXT_X_I_FRAME_STREAM_INF, attributes);
    }
}
