package io.lindstrom.m3u8.parser;

import io.lindstrom.m3u8.model.MediaPlaylist;

public interface MediaPlaylistCreator<M extends MediaPlaylist> extends PlaylistCreator<M>, MediaPlaylistBuildersContainer<M> {
}
