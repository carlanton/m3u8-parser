package io.lindstrom.m3u8.parser;

import com.google.common.base.Splitter;
import io.lindstrom.m3u8.Tags;
import io.lindstrom.m3u8.model.VariantStream;
import io.lindstrom.m3u8.util.AttributeListBuilder;

import java.util.Map;

import static io.lindstrom.m3u8.Tags.*;

public class VariantStreamParser implements Parser<VariantStream> {
    private static final Splitter SPLITTER_COMMA = Splitter.on(',').trimResults();

    @Override
    public VariantStream parse(Map<String, String> attributes) {
        VariantStream.Builder builder = VariantStream.builder();
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
                case FRAME_RATE:
                    builder.frameRate(Double.parseDouble(value));
                    break;
                case HDCP_LEVEL:
                    builder.hdcpLevel(value);
                    break;
                case AUDIO:
                    builder.audio(value);
                    break;
                case VIDEO:
                    builder.video(value);
                    break;
                case SUBTITLES:
                    builder.subtitles(value);
                    break;
                case CLOSED_CAPTIONS:
                    builder.closedCaptions(value);
                    break;
                default:
                    throw new RuntimeException("Unknown key " + key);
            }
        }
        return builder.build();
    }

    @Override
    public String write(VariantStream variantStream) {
        AttributeListBuilder attributes = new AttributeListBuilder();

        attributes.add(Tags.BANDWIDTH, String.valueOf(variantStream.bandwidth()));
        variantStream.averageBandwidth().ifPresent(value -> attributes.add(Tags.AVERAGE_BANDWIDTH, String.valueOf(value)));
        if (!variantStream.codecs().isEmpty()) {
            attributes.addQuoted(Tags.CODECS, String.join(",", variantStream.codecs()));
        }
        variantStream.resolution().ifPresent(value -> attributes.add(Tags.RESOLUTION, value));
        variantStream.frameRate().ifPresent(value -> attributes.add(Tags.FRAME_RATE, String.valueOf(value)));
        variantStream.hdcpLevel().ifPresent(value -> attributes.add(Tags.HDCP_LEVEL, value));
        variantStream.audio().ifPresent(value -> attributes.addQuoted(Tags.AUDIO, value));
        variantStream.video().ifPresent(value -> attributes.addQuoted(Tags.VIDEO, value));
        variantStream.subtitles().ifPresent(value -> attributes.addQuoted(Tags.SUBTITLES, value));
        variantStream.closedCaptions().ifPresent(value -> attributes.addQuoted(Tags.CLOSED_CAPTIONS, value));

        return String.format("%s:%s\n%s\n", Tags.EXT_X_STREAM_INF, attributes, variantStream.uri());
    }
}
