package io.lindstrom.m3u8.model;

import org.immutables.value.Value;

import java.util.List;

@Value.Immutable
public interface MasterPlaylist {
    int version();

    @Value.Default
    default boolean independentSegments() {
        return false;
    }

    List<AlternativeRendition> alternativeRenditions();
    List<VariantStream> variantStreams();
    List<IFramePlaylist> iFramePlaylists();

    static Builder builder() {
        return new Builder();
    }

   class Builder extends MasterPlaylistBuilder {}
}
