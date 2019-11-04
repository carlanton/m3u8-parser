package io.lindstrom.m3u8.parser;

import org.junit.Test;

import java.nio.file.Paths;

public class MediaPlaylistParserIgnoreUnrecognisedTest {
    private final MediaPlaylistParser parser = new MediaPlaylistParser();

    {
        parser.addOptionalTagsSupport(TagsSupport.IGNORE_NON_STANDARD_TAGS);
    }

    @Test
    public void parse() throws Exception {
        parser.readPlaylist(Paths.get("src/test/resources/media/cue/cue1.m3u8"));
    }

}
