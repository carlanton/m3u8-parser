package io.lindstrom.m3u8.parser;

import io.lindstrom.m3u8.model.AdSmartMediaPlaylist;
import io.lindstrom.m3u8.model.CueOut;
import io.lindstrom.m3u8.model.MediaSegment;
import io.lindstrom.m3u8.model.StandardMediaPlaylist;
import io.lindstrom.m3u8.model.StandardMediaPlaylistBuilder;
import io.lindstrom.m3u8.model.AdSmartMediaPlaylistBuilder;

public class AdSmartMediaPlaylistParser extends AbstractMediaPlaylistParser<AdSmartMediaPlaylist> {

    AdSmartMediaPlaylistParser() {

        super(() -> new Builder());
        addOptionalTagsSupport(new CueTagsSupport());
    }

    /**
     * Wrapper class for playlist and segment builders
     */
    static class Builder implements MediaPlaylistCreator<AdSmartMediaPlaylist> {
        private final AdSmartMediaPlaylistBuilder playlistBuilder = AdSmartMediaPlaylist.builder();
        private final StandardMediaPlaylistBuilder playlistB = StandardMediaPlaylist.builder();
        private MediaSegment.Builder segmentBuilder = MediaSegment.builder();

        @Override
        public AdSmartMediaPlaylist create() {

            return AdSmartMediaPlaylist.builder()
                    .from(playlistB.build())
                    .cueOut(CueOut.builder()
                            .duration(10.0D)
                            .id("123")
                            .cue("asudigewid")
                            .build())
                    .build();
        }


        @Override
        public StandardMediaPlaylistBuilder playlistBuilder() {
            return playlistB;
        }

        @Override
        public MediaSegment.Builder segmentBuilder() {
            return segmentBuilder;
        }

        @Override
        public void newSegmentBuilder(MediaSegment.Builder builder) {
            this.segmentBuilder = builder;
        }
    }
}
