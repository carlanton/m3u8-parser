package io.lindstrom.m3u8.model;

import org.immutables.value.Value;

import java.util.Optional;

/**
 * Byte Range (EXT-X-BYTERANGE)
 *
 * @see <a href="https://tools.ietf.org/html/rfc8216#section-4.3.2.2" target="_blank">
 * RFC 8216 - 4.3.2.2.  EXT-X-BYTERANGE</a>
 */
@Value.Immutable
public interface ByteRange {

    /**
     * @return byte range length
     */
    long length();

    /**
     * @return byte range offset
     */
    Optional<Long> offset();

    static Builder builder() {
        return new Builder();
    }

    class Builder extends ByteRangeBuilder {
    }

    static ByteRange of(long length) {
        return builder()
                .length(length)
                .build();
    }

    static ByteRange of(long length, long offset) {
        return builder()
                .length(length)
                .offset(offset)
                .build();
    }
}
