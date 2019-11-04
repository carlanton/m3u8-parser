package io.lindstrom.m3u8.model;

import org.immutables.value.Value;

@Value.Immutable
public interface StandardMediaPlaylist extends MediaPlaylist {
    static StandardMediaPlaylistBuilder builder() {
        return new StandardMediaPlaylistBuilder();
    }

    abstract class Builder implements MediaPlaylistBuilder {
    }

}
