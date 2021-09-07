package io.lindstrom.m3u8.model;

import org.immutables.value.Value;

import java.util.List;
import java.util.Optional;

/**
 * Alternative Renditions (EXT-X-MEDIA)
 *
 * @see <a href="https://tools.ietf.org/html/rfc8216#section-4.3.4.1">
 * RFC 8216 - 4.3.4.1.  EXT-X-MEDIA</a>
 * @see <a href="https://tools.ietf.org/html/rfc8216#section-4.3.4.2.1">
 * RFC 8216 - 4.3.4.2.1.  Alternative Renditions</a>
 */
@Value.Immutable
public interface AlternativeRendition {
    /**
     * @return rendition attribute TYPE
     */
    MediaType type();

    /**
     * Location of the media playlist file.
     *
     * @return rendition attribute URI
     */
    Optional<String> uri();

    /**
     * Specify which group to which this rendition belongs to.
     *
     * @return rendition attribute GROUP-ID
     */
    String groupId();

    /**
     * The primary language used in this rendition.
     *
     * @return rendition attribute LANGUAGE
     * @see <a href="https://tools.ietf.org/html/rfc5646" target="_blank">
     * RFC 5646 - Tags for Identifying Languages</a>
     */
    Optional<String> language();

    /**
     * Identifies a language that is associated with this rendition.
     *
     * @return rendition attribute ASSOC-LANGUAGE
     * @see <a href="https://tools.ietf.org/html/rfc5646" target="_blank">
     * RFC 5646 - Tags for Identifying Languages</a>
     */
    Optional<String> assocLanguage();

    /**
     * Human-readable description of this rendition.
     *
     * @return rendition attribute NAME
     */
    String name();

    /**
     * @return rendition attribute STABLE-RENDITION-ID
     */
    Optional<String> stableRenditionId();

    /**
     * @return rendition attribute DEFAULT
     */
    Optional<Boolean> defaultRendition();

    /**
     * @return rendition attribute AUTOSELECT
     */
    Optional<Boolean> autoSelect();

    /**
     * @return rendition attribute FORCED
     */
    Optional<Boolean> forced();

    /**
     * @return rendition attribute INSTREAM-ID
     */
    Optional<String> inStreamId();

    /**
     * @return rendition attributes CHARACTERISTICS
     */
    List<String> characteristics();

    /**
     * @return rendition attributes CHANNELS
     */
    Optional<Channels> channels();

    static Builder builder() {
        return new Builder();
    }

    class Builder extends AlternativeRenditionBuilder {
    }
}
