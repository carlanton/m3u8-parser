package io.lindstrom.m3u8.parser;

import io.lindstrom.m3u8.model.*;
import org.junit.Test;

import java.nio.file.Paths;

import static org.junit.Assert.assertEquals;

public class MasterPlaylistParserTest {
    private final MasterPlaylistParser parser = new MasterPlaylistParser();

    @Test
    public void masterParser() throws Exception {
        parser.readPlaylist(Paths.get("src/test/resources/master/master.m3u8"));
    }

    @Test
    public void masterParser2() throws Exception {
        parser.readPlaylist(Paths.get("src/test/resources/master/apple-master.m3u8"));
    }

    @Test
    public void custom() throws Exception {
        MasterPlaylist playlist = MasterPlaylist.builder()
                .version(4)
                .independentSegments(true)
                .addAlternativeRenditions(AlternativeRendition.builder()
                        .type(MediaType.AUDIO)
                        .name("Default audio")
                        .groupId("AUDIO")
                        .build())
                .addVariables(PlaylistVariable.of("auth", "xyz"))
                .addVariants(
                        Variant.builder()
                                .addCodecs("avc1.4d401f", "mp4a.40.2")
                                .bandwidth(900000)
                                .uri("v0.m3u8")
                                .build(),
                        Variant.builder()
                                .addCodecs("avc1.4d401f", "mp4a.40.2")
                                .bandwidth(900000)
                                .closedCaptionsNone(true)
                                .uri("v1.m3u8")
                                .resolution(1280, 720)
                                .build())
                .build();

        String expected = "#EXTM3U\n" +
                "#EXT-X-VERSION:4\n" +
                "#EXT-X-INDEPENDENT-SEGMENTS\n" +
                "#EXT-X-DEFINE:NAME=\"auth\",VALUE=\"xyz\"\n" +
                "#EXT-X-MEDIA:TYPE=AUDIO,GROUP-ID=\"AUDIO\",NAME=\"Default audio\"\n" +
                "#EXT-X-STREAM-INF:BANDWIDTH=900000,CODECS=\"avc1.4d401f,mp4a.40.2\"\n" +
                "v0.m3u8\n" +
                "#EXT-X-STREAM-INF:BANDWIDTH=900000,CODECS=\"avc1.4d401f,mp4a.40.2\",RESOLUTION=1280x720,CLOSED-CAPTIONS=NONE\n" +
                "v1.m3u8\n";

        assertEquals(expected, parser.writePlaylistAsString(playlist));
    }
}
