package io.lindstrom.m3u8.model;

import org.immutables.value.Value;

import java.util.Optional;

/**
 * Common playlist interface
 *
 * @see MasterPlaylist
 * @see MediaPlaylist
 */
public interface Playlist {
    /**
     * The playlist compatibility version.
     *
     * @return playlist version
     * @see <a href="https://tools.ietf.org/html/rfc8216#section-4.3.1.2" target="_blank">
     * RFC 8216 - 4.3.1.2.  EXT-X-VERSION</a>
     */
    Optional<Integer> version();

    /**
     * Returns true if the tag EXT-X-INDEPENDENT-SEGMENTS is present in the playlist.
     *
     * @return true if present
     * @see <a href="https://tools.ietf.org/html/rfc8216#section-4.3.5.1" target="_blank">
     * RFC 8216 - 4.3.5.1.  EXT-X-INDEPENDENT-SEGMENTS</a>
     */
    @Value.Default
    default boolean independentSegments() {
        return false;
    }
}
