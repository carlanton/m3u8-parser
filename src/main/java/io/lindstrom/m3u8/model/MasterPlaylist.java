package io.lindstrom.m3u8.model;

import org.immutables.value.Value;

import java.util.List;
import java.util.Optional;

/**
 * Master playlist interface
 */
@Value.Immutable
public interface MasterPlaylist extends Playlist {

    /**
     * A list of alternate renditions related to this playlist.
     *
     * @return list of alternate renditions
     * @see <a href="https://tools.ietf.org/html/rfc8216#section-4.3.4.1" target="_blank">
     * RFC 8216 - 4.3.4.1.  EXT-X-MEDIA</a>
     */
    List<AlternativeRendition> alternativeRenditions();

    /**
     * A list of Variant Streams, each of which describes a different version of the same content.
     *
     * @return list of variant streams
     * @see <a href="https://tools.ietf.org/html/rfc8216#section-4.3.4.2" target="_blank">
     * RFC 8216 - 4.3.4.2.  EXT-X-STREAM-INF</a>
     */
    List<Variant> variants();

    /**
     * A list of I-frame variant streams related to this playlist.
     *
     * @return list of I-frame variant streams
     * @see <a href="https://tools.ietf.org/html/rfc8216#section-4.3.4.3" target="_blank">
     * RFC 8216 - 4.3.3.6.  EXT-X-I-FRAMES-ONLY</a>
     */
    List<IFrameVariant> iFrameVariants();

    /**
     * A list of arbitrary session data.
     *
     * @return list of session data
     * @see <a href="https://tools.ietf.org/html/rfc8216#section-4.3.4.4" target="_blank">
     * RFC 8216 - 4.3.4.4.  EXT-X-SESSION-DATA</a>
     */
    List<SessionData> sessionData();

    /**
     * A list of encryption keys used in media playlists.
     *
     * @return list of encryption keys
     * @see <a href="https://tools.ietf.org/html/rfc8216#section-4.3.4.5" target="_blank">
     * RFC 8216 - 4.3.4.5.  EXT-X-SESSION-KEY</a>
     */
    List<SegmentKey> sessionKeys();

    /**
     * The content steering tag
     *
     * @return the content steering tag, if present.
     * @see <a href="https://datatracker.ietf.org/doc/html/draft-pantos-hls-rfc8216bis#section-4.4.6.6" target="_blank">
     * draft-pantos-hls-rfc8216bis-13 - 4.4.6.6. EXT-X-CONTENT-STEERING</a>
     */
    Optional<ContentSteering> contentSteering();

    static Builder builder() {
        return new Builder();
    }

    class Builder extends MasterPlaylistBuilder {
    }
}