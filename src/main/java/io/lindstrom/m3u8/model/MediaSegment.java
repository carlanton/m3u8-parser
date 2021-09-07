package io.lindstrom.m3u8.model;

import org.immutables.value.Value;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Media Segment interface
 */
@Value.Immutable
public interface MediaSegment {
    double duration();

    Optional<String> title();

    String uri();

    Optional<ByteRange> byteRange();

    Optional<OffsetDateTime> programDateTime();

    Optional<DateRange> dateRange();

    Optional<SegmentMap> segmentMap();

    Optional<SegmentKey> segmentKey();

    @Value.Default
    default boolean discontinuity() {
        return false;
    }

    Optional<Double> cueOut();

    @Value.Default
    default boolean cueIn() {
        return false;
    }

    @Value.Default
    default boolean gap() {
        return false;
    }

    Optional<Long> bitrate();

    List<PartialSegment> partialSegments();

    static Builder builder() {
        return new Builder();
    }

    class Builder extends MediaSegmentBuilder {
    }
}
