package io.lindstrom.m3u8;

import io.lindstrom.m3u8.model.Resolution;
import io.lindstrom.m3u8.model.Variant;

import java.util.Map;

import static io.lindstrom.m3u8.Tags.*;

class VariantParser extends AbstractLineParser<Variant> {
    VariantParser() {
        super(EXT_X_STREAM_INF);
    }

    @Override
    protected Variant parseAttributes(Map<String, String> attributes) throws PlaylistParserException {
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
                    throw new PlaylistParserException("Unknown key " + key);
            }
        }
        return builder.build();
    }

    @Override
    public void write(Variant variant, StringBuilder stringBuilder) {
        super.write(variant, stringBuilder);
        stringBuilder.append(variant.uri()).append('\n');
    }

    @Override
    protected String writeAttributes(Variant variant) {
        AttributeListBuilder attributes = new AttributeListBuilder();

        attributes.add(Tags.BANDWIDTH, String.valueOf(variant.bandwidth()));
        variant.averageBandwidth().ifPresent(value -> attributes.add(Tags.AVERAGE_BANDWIDTH, String.valueOf(value)));
        if (!variant.codecs().isEmpty()) {
            attributes.addQuoted(Tags.CODECS, String.join(",", variant.codecs()));
        }
        variant.resolution().ifPresent(value -> attributes.add(Tags.RESOLUTION, writeResolution(value)));
        variant.frameRate().ifPresent(value -> attributes.add(Tags.FRAME_RATE, String.format("%.3f", value)));
        variant.hdcpLevel().ifPresent(value -> attributes.add(Tags.HDCP_LEVEL, value));
        variant.audio().ifPresent(value -> attributes.addQuoted(Tags.AUDIO, value));
        variant.video().ifPresent(value -> attributes.addQuoted(Tags.VIDEO, value));
        variant.subtitles().ifPresent(value -> attributes.addQuoted(Tags.SUBTITLES, value));
        variant.closedCaptions().ifPresent(value -> attributes.addQuoted(Tags.CLOSED_CAPTIONS, value));

        return attributes.toString();
    }

    static Resolution parseResolution(String string) throws PlaylistParserException {
        String[] fields = string.split("x");
        Resolution resolution;
        try {
            resolution = Resolution.of(Integer.parseInt(fields[0]), Integer.parseInt(fields[1]));
        } catch (NumberFormatException | IndexOutOfBoundsException e) {
            throw new PlaylistParserException("Invalid resolution: " + string);
        }
        return resolution;
    }

    static String writeResolution(Resolution resolution) {
        return String.format("%dx%d", resolution.width(), resolution.height());
    }
}
