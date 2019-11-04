package io.lindstrom.m3u8.parser;

import io.lindstrom.m3u8.model.MediaSegment;
import io.lindstrom.m3u8.model.StandardMediaPlaylist;

public interface MediaPlaylistBuilder<T> extends PlaylistBuilder<T> {
    StandardMediaPlaylist.Builder playlistBuilder();

    MediaSegment.Builder segmentBuilder();

    void newSegmentBuilder(MediaSegment.Builder builder);
}
