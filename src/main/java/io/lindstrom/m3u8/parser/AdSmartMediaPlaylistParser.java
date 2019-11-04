package io.lindstrom.m3u8.parser;

import io.lindstrom.m3u8.model.AdSmartMediaPlaylist;
import io.lindstrom.m3u8.model.MediaSegment;
import io.lindstrom.m3u8.model.StandardMediaPlaylist;

public class AdSmartMediaPlaylistParser extends AbstractMediaPlaylistParser<StandardMediaPlaylist> {

    AdSmartMediaPlaylistParser() {

        super(() -> new MediaPlaylistParser.Builder());
        addOptionalTagsSupport(new CueTagsSupport());
    }

    /**
     * Wrapper class for playlist and segment builders
     */
    static class Builder implements MediaPlaylistCreator<AdSmartMediaPlaylist> {
        private final AdSmartMediaPlaylist.Builder playlistBuilder = AdSmartMediaPlaylist.builder();
        private final StandardMediaPlaylist.Builder playlistB = StandardMediaPlaylist.builder();
        private MediaSegment.Builder segmentBuilder = MediaSegment.builder();

        @Override
        public AdSmartMediaPlaylist create() {
            return playlistBuilder.build();
        }


        @Override
        public StandardMediaPlaylist.Builder playlistBuilder() {
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
