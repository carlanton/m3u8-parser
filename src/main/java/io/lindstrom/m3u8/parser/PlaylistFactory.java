package io.lindstrom.m3u8.parser;

import io.lindstrom.m3u8.model.Playlist;

public interface PlaylistFactory<T extends Playlist> {

    T create();
}
