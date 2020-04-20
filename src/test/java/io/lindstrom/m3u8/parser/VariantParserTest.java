package io.lindstrom.m3u8.parser;

import io.lindstrom.m3u8.model.Resolution;
import io.lindstrom.m3u8.model.Variant;
import io.lindstrom.m3u8.model.VideoRange;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.assertEquals;

public class VariantParserTest {
    private final VariantParser parser = new VariantParser();
    private final String attributes = "BANDWIDTH=123456789,AVERAGE-BANDWIDTH=12345678,CODECS=\"a,b,c\",RESOLUTION=1024x768,FRAME-RATE=50.0,HDCP-LEVEL=0,AUDIO=\"audio\",VIDEO=\"video\",SUBTITLES=\"subtitles\",CLOSED-CAPTIONS=\"cc\",VIDEO-RANGE=SDR";
    private final Variant variant = Variant.builder()
            .uri("uri")
            .bandwidth(123456789)
            .averageBandwidth(12345678)
            .codecs(Arrays.asList("a", "b", "c"))
            .resolution(Resolution.of(1024, 768))
            .frameRate(50)
            .hdcpLevel("0")
            .videoRange(VideoRange.SDR)
            .audio("audio")
            .video("video")
            .subtitles("subtitles")
            .closedCaptions("cc")
            .build();
    @Test
    public void parseAttributes() throws Exception {
        assertEquals(variant, parser.parse(attributes, Collections.singletonMap(Tags.URI, "uri")));
    }

    @Test
    public void parseAttributesClosedCaptionsNone() throws Exception {

        Map<String, String> additionalAttributes = new HashMap<>();

        additionalAttributes.put(Tags.URI, "uri");
        additionalAttributes.put(Tags.CLOSED_CAPTIONS, "NONE");

        final Variant variantLocal = Variant.builder().from(variant)
                .closedCaptionsNone(true)
                .closedCaptions(Optional.empty())
                .build();

        assertEquals(variantLocal, parser.parse(attributes, additionalAttributes));
    }

    @Test
    public void write() throws Exception {
        StringBuilder stringBuilder = new StringBuilder();
        parser.write(variant, stringBuilder);
        assertEquals(Tags.EXT_X_STREAM_INF
                        + ":"
                        + attributes
                        + "\nuri\n",
                stringBuilder.toString());
    }

    @Test(expected = PlaylistParserException.class)
    public void invalidAttribute() throws Exception {
        parser.parseAttributes(Collections.singletonMap("INVALID", "value"));
    }

    @Test(expected = PlaylistParserException.class)
    public void invalidResolution() throws Exception {
        VariantParser.parseResolution("3");
    }

    @Test(expected = PlaylistParserException.class)
    public void invalidResolution2() throws Exception {
        VariantParser.parseResolution("axb");
    }

    @Test
    public void writeResolution() throws Exception {
        assertEquals("1024x768", VariantParser.writeResolution(Resolution.of(1024, 768)));
    }

    @Test
    public void readResolution() throws Exception {
        assertEquals(Resolution.of(1024, 768), VariantParser.parseResolution("1024x768"));
    }
}
