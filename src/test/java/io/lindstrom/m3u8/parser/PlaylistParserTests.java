package io.lindstrom.m3u8.parser;

import io.lindstrom.m3u8.model.*;
import io.lindstrom.m3u8.parser.MasterPlaylistParser;
import io.lindstrom.m3u8.parser.MediaPlaylistParser;
import io.lindstrom.m3u8.parser.PlaylistParserException;
import org.junit.Test;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;


public class PlaylistParserTests {
    private final Path resources = Paths.get("src/test/resources/");
    private final MasterPlaylistParser masterPlaylistParser = new MasterPlaylistParser();
    private final MediaPlaylistParser mediaPlaylistParser = new MediaPlaylistParser();

    @Test
    public void liveMediaPlaylist() throws Exception {
        MediaPlaylist playlist = mediaPlaylistParser.readPlaylist(resources.resolve("open-m3u8/liveMediaPlaylist.m3u8"));
        assertTrue(playlist.version().isPresent());
        assertEquals(Integer.valueOf(3), playlist.version().get());
        assertEquals(8, playlist.targetDuration());
        assertEquals(2680, playlist.mediaSequence());
        assertTrue(playlist.ongoing());
        assertThat(playlist.mediaSegments(), is(Arrays.asList(
                MediaSegment.builder()
                        .duration(7.975)
                        .uri("https://priv.example.com/fileSequence2680.ts")
                        .build(),
                MediaSegment.builder()
                        .duration(7.941)
                        .uri("https://priv.example.com/fileSequence2681.ts")
                        .build(),
                MediaSegment.builder()
                        .duration(7.975)
                        .uri("https://priv.example.com/fileSequence2682.ts")
                        .build())));
    }

    @Test
    public void masterPlaylist() throws Exception {
        MasterPlaylist playlist = masterPlaylistParser.readPlaylist(resources.resolve("open-m3u8/masterPlaylist.m3u8"));
        assertFalse(playlist.version().isPresent());
        assertThat(playlist.variants(), is(Arrays.asList(
                Variant.builder()
                        .bandwidth(1280000)
                        .averageBandwidth(1000000)
                        .uri("http://example.com/low.m3u8")
                        .build(),
                Variant.builder()
                        .bandwidth(2560000)
                        .averageBandwidth(2000000)
                        .uri("http://example.com/mid.m3u8")
                        .build(),
                Variant.builder()
                        .bandwidth(7680000)
                        .averageBandwidth(6000000)
                        .uri("http://example.com/hi.m3u8")
                        .build(),
                Variant.builder()
                        .bandwidth(65000)
                        .addCodecs("mp4a.40.5")
                        .uri("http://example.com/audio-only.m3u8")
                        .build())));
    }

    @Test
    public void masterPlaylistWithAlternativeAudio() throws Exception {
        MasterPlaylist playlist = masterPlaylistParser.readPlaylist(resources.resolve("open-m3u8/masterPlaylistWithAlternativeAudio.m3u8"));
        assertFalse(playlist.version().isPresent());
        assertThat(playlist.alternativeRenditions(), is(Arrays.asList(
                AlternativeRendition.builder()
                        .type(MediaType.AUDIO)
                        .groupId("aac")
                        .name("English")
                        .defaultRendition(true)
                        .autoSelect(true)
                        .language("en")
                        .uri("main/english-audio.m3u8")
                        .build(),
                AlternativeRendition.builder()
                        .type(MediaType.AUDIO)
                        .groupId("aac")
                        .name("Deutsch")
                        .defaultRendition(false)
                        .autoSelect(true)
                        .language("de")
                        .uri("main/german-audio.m3u8")
                        .build(),
                AlternativeRendition.builder()
                        .type(MediaType.AUDIO)
                        .groupId("aac")
                        .name("Commentary")
                        .defaultRendition(false)
                        .autoSelect(false)
                        .language("en")
                        .uri("commentary/audio-only.m3u8")
                        .build())));

        assertThat(playlist.variants(), is(Arrays.asList(
                Variant.builder()
                        .bandwidth(1280000)
                        .addCodecs("...")
                        .audio("aac")
                        .uri("low/video-only.m3u8")
                        .build(),
                Variant.builder()
                        .bandwidth(2560000)
                        .addCodecs("...")
                        .audio("aac")
                        .uri("mid/video-only.m3u8")
                        .build(),
                Variant.builder()
                        .bandwidth(7680000)
                        .addCodecs("...")
                        .audio("aac")
                        .uri("hi/video-only.m3u8")
                        .build(),
                Variant.builder()
                        .bandwidth(65000)
                        .addCodecs("mp4a.40.5")
                        .audio("aac")
                        .uri("main/english-audio.m3u8")
                        .build())));
    }

    @Test
    public void masterPlaylistWithAlternativeVideo() throws Exception {
        MasterPlaylist playlist = masterPlaylistParser.readPlaylist(resources.resolve("open-m3u8/masterPlaylistWithAlternativeVideo.m3u8"));
        assertFalse(playlist.version().isPresent());

        List<AlternativeRendition> lowVideo = Arrays.asList(
                AlternativeRendition.builder()
                        .type(MediaType.VIDEO)
                        .groupId("low")
                        .name("Main")
                        .defaultRendition(true)
                        .uri("low/main/audio-video.m3u8")
                        .build(),
                AlternativeRendition.builder()
                        .type(MediaType.VIDEO)
                        .groupId("low")
                        .name("Centerfield")
                        .defaultRendition(false)
                        .uri("low/centerfield/audio-video.m3u8")
                        .build(),
                AlternativeRendition.builder()
                        .type(MediaType.VIDEO)
                        .groupId("low")
                        .name("Dugout")
                        .defaultRendition(false)
                        .uri("low/dugout/audio-video.m3u8")
                        .build());

        List<AlternativeRendition> midVideo = Arrays.asList(
                AlternativeRendition.builder()
                        .type(MediaType.VIDEO)
                        .groupId("mid")
                        .name("Main")
                        .defaultRendition(true)
                        .uri("mid/main/audio-video.m3u8")
                        .build(),
                AlternativeRendition.builder()
                        .type(MediaType.VIDEO)
                        .groupId("mid")
                        .name("Centerfield")
                        .defaultRendition(false)
                        .uri("mid/centerfield/audio-video.m3u8")
                        .build(),
                AlternativeRendition.builder()
                        .type(MediaType.VIDEO)
                        .groupId("mid")
                        .name("Dugout")
                        .defaultRendition(false)
                        .uri("mid/dugout/audio-video.m3u8")
                        .build());

        List<AlternativeRendition> hiVideo = Arrays.asList(
                AlternativeRendition.builder()
                        .type(MediaType.VIDEO)
                        .groupId("hi")
                        .name("Main")
                        .defaultRendition(true)
                        .uri("hi/main/audio-video.m3u8")
                        .build(),
                AlternativeRendition.builder()
                        .type(MediaType.VIDEO)
                        .groupId("hi")
                        .name("Centerfield")
                        .defaultRendition(false)
                        .uri("hi/centerfield/audio-video.m3u8")
                        .build(),
                AlternativeRendition.builder()
                        .type(MediaType.VIDEO)
                        .groupId("hi")
                        .name("Dugout")
                        .defaultRendition(false)
                        .uri("hi/dugout/audio-video.m3u8")
                        .build());

        List<AlternativeRendition> allVideo = new ArrayList<>();
        allVideo.addAll(lowVideo);
        allVideo.addAll(midVideo);
        allVideo.addAll(hiVideo);
        assertThat(playlist.alternativeRenditions(), is(allVideo));

        assertThat(playlist.variants(), is(Arrays.asList(
                Variant.builder()
                        .bandwidth(1280000)
                        .addCodecs("...")
                        .video("low")
                        .uri("low/main/audio-video.m3u8")
                        .build(),
                Variant.builder()
                        .bandwidth(2560000)
                        .addCodecs("...")
                        .video("mid")
                        .uri("mid/main/audio-video.m3u8")
                        .build(),
                Variant.builder()
                        .bandwidth(7680000)
                        .addCodecs("...")
                        .video("hi")
                        .uri("hi/main/audio-video.m3u8")
                        .build(),
                Variant.builder()
                        .bandwidth(65000)
                        .addCodecs("mp4a.40.5")
                        .uri("main/audio-only.m3u8")
                        .build())));
    }

    @Test
    public void masterPlaylistWithIFrames() throws Exception {
        MasterPlaylist playlist = masterPlaylistParser.readPlaylist(resources.resolve("open-m3u8/masterPlaylistWithIFrames.m3u8"));
        assertFalse(playlist.version().isPresent());

        assertThat(playlist.variants(), is(Arrays.asList(
                Variant.builder()
                        .bandwidth(1280000)
                        .uri("low/audio-video.m3u8")
                        .build(),
                Variant.builder()
                        .bandwidth(2560000)
                        .uri("mid/audio-video.m3u8")
                        .build(),
                Variant.builder()
                        .bandwidth(7680000)
                        .uri("hi/audio-video.m3u8")
                        .build(),
                Variant.builder()
                        .bandwidth(65000)
                        .addCodecs("mp4a.40.5")
                        .uri("audio-only.m3u8")
                        .build())));

        assertThat(playlist.iFrameVariants(), is(Arrays.asList(
               IFrameVariant.builder()
                       .bandwidth(86000)
                       .uri("low/iframe.m3u8")
                       .build(),
                IFrameVariant.builder()
                        .bandwidth(150000)
                        .uri("mid/iframe.m3u8")
                        .build(),
                IFrameVariant.builder()
                        .bandwidth(550000)
                        .uri("hi/iframe.m3u8")
                        .build())));
    }

    @Test
    public void negativeDurationMediaPlaylist() throws Exception {
        MediaPlaylist playlist = mediaPlaylistParser.readPlaylist(resources.resolve("open-m3u8/negativeDurationMediaPlaylist.m3u8"));
        assertFalse(playlist.version().isPresent());
        assertEquals(-1, playlist.targetDuration());
        assertThat(playlist.mediaSegments(), is(Arrays.asList(
                MediaSegment.builder()
                        .duration(-1)
                        .title("TOP 100")
                        .uri("http://radio.promodj.com:8000/top100-192")
                        .build(),
                MediaSegment.builder()
                        .duration(-1)
                        .title("Channel 5")
                        .uri("http://radio.promodj.com:8000/channel5-192")
                        .build(),
                MediaSegment.builder()
                        .duration(-1)
                        .title("Klubb")
                        .uri("http://radio.promodj.com:8000/klubb-192")
                        .build())));
    }

    @Test
    public void playlistWithEncryptedMediaSegments() throws Exception {
        MediaPlaylist playlist = mediaPlaylistParser.readPlaylist(resources.resolve("open-m3u8/playlistWithEncryptedMediaSegments.m3u8"));
        assertTrue((playlist.version().isPresent()));
        assertEquals(Integer.valueOf(3), playlist.version().get());
        assertEquals(7794, playlist.mediaSequence());
        assertEquals(15, playlist.targetDuration());
        assertThat(playlist.mediaSegments(), is(Arrays.asList(
                MediaSegment.builder()
                        .segmentKey(SegmentKey.builder()
                                .method(KeyMethod.AES_128)
                                .uri("https://priv.example.com/key.php?r=52")
                                .build())
                        .duration(2.833)
                        .uri("http://media.example.com/fileSequence52-A.ts")
                        .build(),
                MediaSegment.builder()
                        .duration(15.0)
                        .uri("http://media.example.com/fileSequence52-B.ts")
                        .build(),
                MediaSegment.builder()
                        .duration(13.333)
                        .uri("http://media.example.com/fileSequence52-C.ts")
                        .build(),
                MediaSegment.builder()
                        .segmentKey(SegmentKey.builder()
                                .method(KeyMethod.AES_128)
                                .uri("https://priv.example.com/key.php?r=53")
                                .build())
                        .duration(15.0)
                        .uri("http://media.example.com/fileSequence53-A.ts")
                        .build())));
    }

    @Test
    public void simpleMediaPlaylist() throws Exception {
        MediaPlaylist playlist = mediaPlaylistParser.readPlaylist(resources.resolve("open-m3u8/simpleMediaPlaylist.m3u8"));
        assertTrue(playlist.version().isPresent());
        assertEquals(Integer.valueOf(4), playlist.version().get());
        assertEquals(10, playlist.targetDuration());
        assertFalse(playlist.ongoing());
        assertThat(playlist.mediaSegments(), is(Arrays.asList(
                MediaSegment.builder()
                        .duration(9.009)
                        .uri("http://media.example.com/first.ts")
                        .build(),
                MediaSegment.builder()
                        .duration(9.009)
                        .uri("http://media.example.com/second.ts")
                        .build(),
                MediaSegment.builder()
                        .duration(3.003)
                        .uri("http://media.example.com/third.ts")
                        .build())));
    }

    @Test(expected = PlaylistParserException.class)
    public void twoMediaPlaylists() throws Exception {
        // Note: this is supported in open-m3u8, but not here.
        mediaPlaylistParser.readPlaylist(resources.resolve("open-m3u8/twoMediaPlaylists.m3u8"));
    }

    @Test
    public void withDiscontinuity() throws Exception {
        MediaPlaylist playlist = mediaPlaylistParser.readPlaylist(resources.resolve("open-m3u8/withDiscontinuity.m3u8"));

        assertTrue(playlist.version().isPresent());
        assertEquals(Integer.valueOf(3), playlist.version().get());
        assertEquals(10, playlist.targetDuration());
        assertEquals(0, playlist.mediaSequence());
        assertFalse(playlist.ongoing());
        assertThat(playlist.mediaSegments(), is(Arrays.asList(
                MediaSegment.builder()
                        .duration(10)
                        .title("Advertisement")
                        .uri("http://myserver/api/data/params?pubid=e4ac7b50-0422-4938-be6a-b0f49c50ebfc&chname=sample12.mp4&profid=1&slotid=1&pcrpts=0&ssid=<1234567890123456>&breakid=1&contenturl=")
                        .build(),
                MediaSegment.builder()
                        .duration(10)
                        .title("Advertisement")
                        .uri("http://myserver/api/data/params?pubid=e4ac7b50-0422-4938-be6a-b0f49c50ebfc&chname=sample12.mp4&profid=1&slotid=2&pcrpts=0&ssid=<1234567890123456>&breakid=1&contenturl=")
                        .build(),
                MediaSegment.builder()
                        .duration(10)
                        .title("Advertisement")
                        .uri("http://myserver/api/data/params?pubid=e4ac7b50-0422-4938-be6a-b0f49c50ebfc&chname=sample12.mp4&profid=1&slotid=3&pcrpts=0&ssid=<1234567890123456>&breakid=1&contenturl=")
                        .build(),
                MediaSegment.builder()
                        .duration(10)
                        .discontinuity(true)
                        .uri("http://contentserver/vod/sample.mp4/media_w1013470664_0.ts")
                        .build(),
                MediaSegment.builder()
                        .duration(10)
                        .uri("http://contentserver/vod/sample.mp4/media_w1013470664_1.ts")
                        .build())));
    }
}
