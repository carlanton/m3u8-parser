package io.lindstrom.m3u8;

import io.lindstrom.m3u8.model.Resolution;
import io.lindstrom.m3u8.model.Variant;
import io.lindstrom.m3u8.util.AttributeListBuilder;
import io.lindstrom.m3u8.util.ParserUtils;

import java.util.Map;

import static io.lindstrom.m3u8.Tags.*;

class VariantParser extends AbstractLineParser<Variant> {
    VariantParser() {
        super(EXT_X_STREAM_INF);
    }

    @Override
    protected Variant parseAttributes(Map<String, String> attributes) {
        Variant.Builder builder = Variant.builder();
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
                    builder.resolution(parseResolution(value));
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
    public void write(Variant value, StringBuilder stringBuilder) {
        super.write(value, stringBuilder);
        stringBuilder.append(value.uri()).append('\n');
    }

    @Override
    protected String writeAttributes(Variant variantStream) {
        AttributeListBuilder attributes = new AttributeListBuilder();

        attributes.add(Tags.BANDWIDTH, String.valueOf(variantStream.bandwidth()));
        variantStream.averageBandwidth().ifPresent(value -> attributes.add(Tags.AVERAGE_BANDWIDTH, String.valueOf(value)));
        if (!variantStream.codecs().isEmpty()) {
            attributes.addQuoted(Tags.CODECS, String.join(",", variantStream.codecs()));
        }
        variantStream.resolution().ifPresent(value -> attributes.add(Tags.RESOLUTION, writeResolution(value)));
        variantStream.frameRate().ifPresent(value -> attributes.add(Tags.FRAME_RATE, String.format("%.3f", value)));
        variantStream.hdcpLevel().ifPresent(value -> attributes.add(Tags.HDCP_LEVEL, value));
        variantStream.audio().ifPresent(value -> attributes.addQuoted(Tags.AUDIO, value));
        variantStream.video().ifPresent(value -> attributes.addQuoted(Tags.VIDEO, value));
        variantStream.subtitles().ifPresent(value -> attributes.addQuoted(Tags.SUBTITLES, value));
        variantStream.closedCaptions().ifPresent(value -> attributes.addQuoted(Tags.CLOSED_CAPTIONS, value));

        return attributes.toString();
    }

    static Resolution parseResolution(String string) {
        String[] fields = string.split("x");
        return Resolution.of(Integer.parseInt(fields[0]), Integer.parseInt(fields[1]));
    }

    static String writeResolution(Resolution resolution) {
        return String.format("%dx%d", resolution.width(), resolution.height());
    }
}
