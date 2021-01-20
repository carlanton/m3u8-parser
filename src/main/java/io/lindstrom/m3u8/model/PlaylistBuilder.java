package io.lindstrom.m3u8.model;

public interface PlaylistBuilder<P extends Playlist> {
    P build();
}
