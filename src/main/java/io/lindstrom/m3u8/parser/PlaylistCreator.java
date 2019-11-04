package io.lindstrom.m3u8.parser;

import io.lindstrom.m3u8.model.Playlist;

public interface PlaylistCreator<T extends Playlist> extends PlaylistBuildersContainer<T>, PlaylistFactory<T> {
}
