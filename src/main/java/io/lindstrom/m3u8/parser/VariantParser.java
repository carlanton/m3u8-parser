package io.lindstrom.m3u8.parser;

import io.lindstrom.m3u8.model.Resolution;
import io.lindstrom.m3u8.model.Variant;

import static io.lindstrom.m3u8.parser.Tags.*;

class VariantParser extends AbstractTagParser<Variant, Variant.Builder> {
    private static final String NONE = "NONE";

    VariantParser() {
        super(EXT_X_STREAM_INF);
    }

    @Override
    void onAttribute(String attribute, String value, Variant.Builder builder) throws PlaylistParserException {
        switch (attribute) {
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
                if (value.equals(NONE)) {
                    builder.closedCaptionsNone(true);
                } else {
                    builder.closedCaptions(value);
                }
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
    void onUri(String uri, Variant.Builder builder) {
        builder.uri(uri);
    }

    @Override
    void writeUri(Variant value, StringBuilder stringBuilder) {
        stringBuilder.append(value.uri()).append("\n");
    }

    @Override
    Variant.Builder newBuilder() {
        return Variant.builder();
    }

    @Override
    Variant build(Variant.Builder builder) {
        return builder.build();
    }

    @Override
    void write(Variant variant, AttributeListBuilder attributes) {
        attributes.add(Tags.BANDWIDTH, String.valueOf(variant.bandwidth()));
        variant.averageBandwidth().ifPresent(value -> attributes.add(Tags.AVERAGE_BANDWIDTH, String.valueOf(value)));
        if (!variant.codecs().isEmpty()) {
            attributes.addQuoted(Tags.CODECS, String.join(",", variant.codecs()));
        }
        variant.resolution().ifPresent(value -> attributes.add(Tags.RESOLUTION, writeResolution(value)));
        variant.frameRate().ifPresent(value -> attributes.add(Tags.FRAME_RATE, Double.toString(value)));
        variant.hdcpLevel().ifPresent(value -> attributes.add(Tags.HDCP_LEVEL, value));
        variant.audio().ifPresent(value -> attributes.addQuoted(Tags.AUDIO, value));
        variant.video().ifPresent(value -> attributes.addQuoted(Tags.VIDEO, value));
        variant.subtitles().ifPresent(value -> attributes.addQuoted(Tags.SUBTITLES, value));

        if (variant.closedCaptionsNone().orElse(false)) {
            attributes.add(Tags.CLOSED_CAPTIONS, NONE);
        } else {
            variant.closedCaptions().ifPresent(value -> attributes.addQuoted(Tags.CLOSED_CAPTIONS, value));
        }

        variant.programId().ifPresent(value -> attributes.add(Tags.PROGRAM_ID, Integer.toString(value)));
        variant.videoRange().ifPresent(value -> attributes.add(VIDEO_RANGE, value));
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
        return resolution.width() + "x" + resolution.height();
    }
}
