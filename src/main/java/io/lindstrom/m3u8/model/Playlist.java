package io.lindstrom.m3u8.model;

import org.immutables.value.Value;

public interface Playlist {
    int version();

    @Value.Default
    default boolean independentSegments() {
        return false;
    }
}
