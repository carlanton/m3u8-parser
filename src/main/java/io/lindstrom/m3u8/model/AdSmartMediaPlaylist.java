package io.lindstrom.m3u8.model;

import org.immutables.value.Value;

@Value.Immutable
public interface AdSmartMediaPlaylist extends MediaPlaylist {
    AdSmart adSmart();

    static AdSmartMediaPlaylistBuilder builder() {
        return new AdSmartMediaPlaylistBuilder();
    }

    abstract class Builder implements MediaPlaylistBuilder {
    }

}
