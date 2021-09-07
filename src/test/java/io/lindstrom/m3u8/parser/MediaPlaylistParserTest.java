package io.lindstrom.m3u8.parser;

import io.lindstrom.m3u8.model.MediaPlaylist;
import io.lindstrom.m3u8.model.MediaSegment;
import org.junit.Test;

import java.nio.file.Paths;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

public class MediaPlaylistParserTest {
    private final MediaPlaylistParser parser = new MediaPlaylistParser();

    @Test
    public void parse1() throws Exception {
        parser.readPlaylist(Paths.get("src/test/resources/media/apple-media.m3u8"));
    }

    @Test
    public void parse2() throws Exception {
        parser.readPlaylist(Paths.get("src/test/resources/media/iframe.m3u8"));
    }

    @Test
    public void parse3() throws Exception {
        parser.readPlaylist(Paths.get("src/test/resources/media/mp4.m3u8"));
    }

    @Test
    public void parse4() throws Exception {
        parser.readPlaylist(Paths.get("src/test/resources/media/mp4-iframe.m3u8"));
    }

    @Test
    public void parse5() throws Exception {
        parser.readPlaylist(Paths.get("src/test/resources/media/variant.m3u8"));
    }

    @Test
    public void extInfDurationFormat() throws Exception {
        String actual = parser.writePlaylistAsString(parser.readPlaylist(Paths.get("src/test/resources/media/issue-17.m3u8")));
        String expected = "#EXTM3U\n" +
                "#EXT-X-VERSION:3\n" +
                "#EXT-X-TARGETDURATION:53\n" +
                "#EXT-X-MEDIA-SEQUENCE:0\n" +
                "#EXTINF:0.96,\n" +
                "5UkiTad9_3021444571_copy7.ts\n" +
                "#EXTINF:0.000011,\n" +
                "5UkiTad9_3021444571_copy8.ts\n";

        assertEquals(actual, expected);
    }

    @Test
    public void programDateTimeFormat() throws PlaylistParserException {
        assertEquals(Optional.of(OffsetDateTime.of(2017, 10, 21, 15, 0, 10, 157000000, ZoneOffset.UTC)),
                parser.readPlaylist(String.join("\n",
                        "#EXTM3U",
                        "#EXT-X-TARGETDURATION:10",
                        "#EXT-X-PROGRAM-DATE-TIME:2017-10-21T15:00:10.157Z",
                        "#EXTINF:10",
                        "1.ts")).mediaSegments().get(0).programDateTime());

        assertEquals(Optional.of(OffsetDateTime.of(2022, 3, 17, 23, 0, 0, 0, ZoneOffset.UTC)),
                parser.readPlaylist(String.join("\n",
                        "#EXTM3U",
                        "#EXT-X-TARGETDURATION:10",
                        "#EXT-X-PROGRAM-DATE-TIME:2022-03-17T23:00:00.000+0000",
                        "#EXTINF:10",
                        "1.ts")).mediaSegments().get(0).programDateTime());

        assertEquals(Optional.of(OffsetDateTime.of(2020, 1, 1, 15, 0, 0, 0, ZoneOffset.ofHoursMinutes(5, 30))),
                parser.readPlaylist(String.join("\n",
                        "#EXTM3U",
                        "#EXT-X-TARGETDURATION:10",
                        "#EXT-X-PROGRAM-DATE-TIME:2020-01-01T15:00:00.000+05:30",
                        "#EXTINF:10",
                        "1.ts")).mediaSegments().get(0).programDateTime());
    }

    @Test
    public void parseCueOut() throws Exception {
        // Second segment has #EXT-X-CUE-OUT duration of 19 seconds, done in multiple representations
        checkCueOut("src/test/resources/media/media-playlist-with-date-range-and-cue-out-cue-in.m3u8", 1, 19.0);
        checkCueOut("src/test/resources/media/media-playlist-with-date-range-and-cue-out-cue-in_2.m3u8", 1 , 19.0);
        checkCueOut("src/test/resources/media/media-playlist-with-date-range-and-cue-out-cue-in_3.m3u8", 1 , 19.0);
        checkCueOut("src/test/resources/media/media-playlist-with-date-range-and-cue-out-cue-in_4.m3u8", 1, 19.0);
        checkCueOut("src/test/resources/media/media-playlist-with-date-range-and-cue-out-cue-in_5.m3u8", 1, 19.0);
    }

    private void checkCueOut(String path, int segNum, double expectedCueOut) throws Exception {
        MediaPlaylist playlist = parser.readPlaylist(Paths.get(path));
        List<MediaSegment> segments = playlist.mediaSegments();
        for (int i = 0; i < segments.size(); i++) {
            MediaSegment segment = segments.get(i);
            if (i == segNum) {
                assertTrue(segment.cueOut().isPresent());
                assertEquals(expectedCueOut, segment.cueOut().get(), 0);
            } else {
                assertFalse(segment.cueOut().isPresent());
            }
        }
    }

}
