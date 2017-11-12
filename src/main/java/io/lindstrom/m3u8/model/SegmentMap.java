package io.lindstrom.m3u8.model;

import org.immutables.value.Value;

import java.util.Optional;

/**
 * Segment map (EXT-X-MAP)
 *
 * @see <a href="https://tools.ietf.org/html/rfc8216#section-4.3.2.5" target="_blank">
 * RFC 8216 - 4.3.2.5.  EXT-X-MAP</a>
 */
@Value.Immutable
public interface SegmentMap {
    String uri();

    Optional<ByteRange> byteRange();

    static Builder builder() {
        return new Builder();
    }

    class Builder extends SegmentMapBuilder {
    }

    static SegmentMap of(String uri) {
        return builder()
                .uri(uri)
                .build();
    }

    static SegmentMap of(String uri, ByteRange byteRange) {
        return builder()
                .uri(uri)
                .byteRange(byteRange)
                .build();
    }
}
