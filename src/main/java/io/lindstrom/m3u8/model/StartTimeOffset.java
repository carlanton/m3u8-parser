package io.lindstrom.m3u8.model;

import org.immutables.value.Value;

/**
 * Start time offset (EXT-X-START)
 *
 * @see <a href="https://tools.ietf.org/html/rfc8216#section-4.3.5.2" target="_blank">
 * RFC 8216 - 4.3.5.2.  EXT-X-START</a>
 */
@Value.Immutable
public interface StartTimeOffset {
    /**
     * The start offset in seconds. Can be negative.
     * @return start time offset in seconds.
     */
    double timeOffset();

    @Value.Default
    default boolean precise() {
        return false;
    }

    static Builder builder() {
        return new Builder();
    }

    class Builder extends StartTimeOffsetBuilder {
    }

    static StartTimeOffset of(double timeOffset) {
        return of(timeOffset, false);
    }

    static StartTimeOffset of(double timeOffset, boolean precise) {
        return builder().timeOffset(timeOffset).precise(precise).build();
    }
}
