package io.lindstrom.m3u8.model;

import org.immutables.value.Value;

import java.time.OffsetDateTime;
import java.util.Optional;

@Value.Immutable
public interface MediaSegment {
    double duration();
    Optional<String> title();
    String uri();
    Optional<ByteRange> byteRange();
    Optional<OffsetDateTime> programDateTime();
    Optional<MapInfo> mapInfo();

    static Builder builder() {
        return new Builder();
    }

    class Builder extends MediaSegmentBuilder {}
}
