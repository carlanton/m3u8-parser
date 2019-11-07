package io.lindstrom.m3u8.parser;

import io.lindstrom.m3u8.model.*;
import io.lindstrom.m3u8.model.StandardMediaPlaylistBuilder;
import io.lindstrom.m3u8.model.AdSmartMediaPlaylistBuilder;

public class AdSmartMediaPlaylistParser extends AbstractMediaPlaylistParser<AdSmartMediaPlaylist> {

    public AdSmartMediaPlaylistParser() {

        super(() -> new Builder());
        addOptionalTagsSupport(new CueTagsSupport());
    }

    /**
     * Wrapper class for playlist and segment builders
     */
    static class Builder implements MediaPlaylistCreator<AdSmartMediaPlaylist> {
        private final AdSmartBuilder adSmartBuilder = AdSmart.builder();
        private final AdSmartMediaPlaylistBuilder playlistBuilder = AdSmartMediaPlaylist.builder();
        private final StandardMediaPlaylistBuilder playlistB = StandardMediaPlaylist.builder();
        private MediaSegment.Builder segmentBuilder = MediaSegment.builder();

        @Override
        public AdSmartMediaPlaylist create() {

            return playlistBuilder
                    .from(playlistB.build())
                    .adSmart(adSmartBuilder.build())
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

        @Override
        public <T> T builderByType(Class<T> clazz){
            if(clazz == AdSmartBuilder.class ){
                return (T) AdSmartBuilder.class.cast(adSmartBuilder);
            }
            throw new IllegalArgumentException("Couldn't find desired builder");
        }
    }
}
