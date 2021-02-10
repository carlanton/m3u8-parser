package io.lindstrom.m3u8.model;

import org.immutables.value.Value;

import java.util.List;
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

    /**
     * The playlist start time offset.
     * @return start time offset
     * @see <a href="https://tools.ietf.org/html/rfc8216#section-4.3.5.2" target="_blank">
     * RFC 8216 - 4.3.5.2.  EXT-X-START</a>
     */
    Optional<StartTimeOffset> startTimeOffset();


    /**
     * @return a list of variable definitions.
     * @see <a href="https://tools.ietf.org/html/draft-pantos-hls-rfc8216bis-07#section-4.4.2.3" target="_blank">
     * RFC 8216bis-07 - 4.4.2.3.  EXT-X-DEFINE</a>
     * */
    List<PlaylistVariable> variables();


    /**
     * @return a list of comments.
     * @see <a href="https://tools.ietf.org/html/rfc8216#section-4.1" target="_blank">
     * RFC 8216 - 4.1  non #EXT comments</a>
     * */
    List<String> comments();
}
