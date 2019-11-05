package io.lindstrom.m3u8.parser;

import io.lindstrom.m3u8.model.AdSmartMediaPlaylist;
import io.lindstrom.m3u8.model.StandardMediaPlaylist;
import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.nio.file.Paths;

public class MediaPlaylistParserWithCueTest {
    private final AdSmartMediaPlaylistParser parser = new AdSmartMediaPlaylistParser();

    @Test
    public void parse1() throws Exception {
        AdSmartMediaPlaylist standardMediaPlaylist = parser.readPlaylist(Paths.get("src/test/resources/media/cue/cue1.m3u8"));

        Assertions.assertThat(standardMediaPlaylist.mediaSegments()).isNotEmpty();
        Assertions.assertThat(standardMediaPlaylist.adSmart()).isNotNull();
        Assertions.assertThat(standardMediaPlaylist.adSmart().cueOut()).isNotNull();
        Assertions.assertThat(standardMediaPlaylist.adSmart().cueOut().cue()).isNotNull();
        Assertions.assertThat(standardMediaPlaylist.adSmart().cueOut().id()).isNotNull();

    }

    @Test
    public void parse2() throws Exception {
        AdSmartMediaPlaylist standardMediaPlaylist = parser.readPlaylist(Paths.get("src/test/resources/media/cue/cue2.m3u8"));

        Assertions.assertThat(standardMediaPlaylist.mediaSegments()).isNotEmpty();

        Assertions.assertThat(standardMediaPlaylist.adSmart()).isNotNull();
        Assertions.assertThat(standardMediaPlaylist.adSmart().cueOut()).isNotNull();
        Assertions.assertThat(standardMediaPlaylist.adSmart().cueOut().cue()).isNotNull();
        Assertions.assertThat(standardMediaPlaylist.adSmart().cueOut().id()).isNotNull();

        Assertions.assertThat(standardMediaPlaylist.adSmart().cueSpan()).isNotNull();
        Assertions.assertThat(standardMediaPlaylist.adSmart().cueSpan().get(0).id()).isNotNull();
        Assertions.assertThat(standardMediaPlaylist.adSmart().cueSpan().get(1).id()).isNotNull();
        Assertions.assertThat(standardMediaPlaylist.adSmart().cueSpan().get(0).timeFromSignal()).isNotNull();
        Assertions.assertThat(standardMediaPlaylist.adSmart().cueSpan().get(1).timeFromSignal()).isNotNull();

        Assertions.assertThat(standardMediaPlaylist.adSmart().cueIn()).isNotNull();
        Assertions.assertThat(standardMediaPlaylist.adSmart().cueIn().id()).isNotNull();
    }

    @Test
    public void parse3() throws Exception {
        AdSmartMediaPlaylist standardMediaPlaylist = parser.readPlaylist(Paths.get("src/test/resources/media/cue/cue3.m3u8"));

        Assertions.assertThat(standardMediaPlaylist.mediaSegments()).isNotEmpty();
        Assertions.assertThat(standardMediaPlaylist.adSmart().cueIn()).isNotNull();
        Assertions.assertThat(standardMediaPlaylist.adSmart().cueIn().id()).isNotNull();
    }

    @Test
    public void parse4() throws Exception {
        AdSmartMediaPlaylist standardMediaPlaylist = parser.readPlaylist(Paths.get("src/test/resources/media/cue/cue4.m3u8"));

        Assertions.assertThat(standardMediaPlaylist.mediaSegments()).isNotEmpty();
    }
}
