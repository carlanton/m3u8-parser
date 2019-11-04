package io.lindstrom.m3u8.model;

import org.immutables.value.Value;

@Value.Immutable
public interface AdSmartMediaPlaylist extends MediaPlaylist {
    CueOut cueOut();

    static AdSmartMediaPlaylist.Builder builder() {
        return new AdSmartMediaPlaylist.Builder();
    }

    class Builder extends AdSmartMediaPlaylistBuilder {

    }
}
