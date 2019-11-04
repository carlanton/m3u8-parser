package io.lindstrom.m3u8.model;

import org.immutables.value.Value;

@Value.Immutable
public interface AdSmartMediaPlaylist extends MediaPlaylist {
    CueOut cueOut();

    static AdSmartMediaPlaylistBuilder builder() {
        return new AdSmartMediaPlaylistBuilder();
    }

    abstract class Builder implements MediaPlaylistBuilder {
    }

}
