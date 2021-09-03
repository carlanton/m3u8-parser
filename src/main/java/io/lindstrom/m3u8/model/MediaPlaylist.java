package io.lindstrom.m3u8.model;

import org.immutables.value.Value;

import java.util.List;
import java.util.Optional;

/**
 * Media Playlist interface
 */
@Value.Immutable
public interface MediaPlaylist extends Playlist {
    int targetDuration();

    @Value.Default
    default long mediaSequence() {
        return 0;
    }

    @Value.Default
    default long discontinuitySequence() {
        return 0;
    }

    @Value.Default
    default boolean ongoing() {
        return true;
    }

    Optional<Boolean> allowCache();

    Optional<PlaylistType> playlistType();

    @Value.Default
    default boolean iFramesOnly() {
        return false;
    }

    List<MediaSegment> mediaSegments();

    Optional<ServerControl> serverControl();

    Optional<PartialSegmentInformation> partialSegmentInformation();

    List<PartialSegment> partialSegments();

    Optional<Skip> skip();

    Optional<PreloadHint> preloadHint();

    List<RenditionReport> renditionReports();

    static Builder builder() {
        return new Builder();
    }

    class Builder extends MediaPlaylistBuilder {
    }
}
