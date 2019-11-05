package io.lindstrom.m3u8.parser;

import io.lindstrom.m3u8.model.MediaSegment;
import io.lindstrom.m3u8.model.StandardMediaPlaylistBuilder;

public interface MediaPlaylistBuildersContainer<T> extends PlaylistBuildersContainer<T> {
    StandardMediaPlaylistBuilder playlistBuilder();

    MediaSegment.Builder segmentBuilder();

    void newSegmentBuilder(MediaSegment.Builder builder);

    default <C> C builderByType(Class<C> clazz){
        throw new IllegalArgumentException("Couldn't find desired builder");
    }
}
