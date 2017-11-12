package io.lindstrom.m3u8.model;

import org.immutables.value.Value;

import java.util.Optional;

/**
 * Segment Key (EXT-X-KEY)
 *
 * @see <a href="https://tools.ietf.org/html/rfc8216#section-4.3.2.4" target="_blank">
 * RFC 8216 - 4.3.2.4.  EXT-X-KEY</a>
 */
@Value.Immutable
public interface SegmentKey {
    KeyMethod method();

    Optional<String> uri();

    Optional<String> iv();

    Optional<String> keyFormat();

    Optional<String> keyFormatVersions();

    static Builder builder() {
        return new Builder();
    }

    class Builder extends SegmentKeyBuilder {}
}
