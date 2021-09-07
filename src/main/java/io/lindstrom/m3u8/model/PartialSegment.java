package io.lindstrom.m3u8.model;

import org.immutables.value.Value;

import java.util.Optional;

@Value.Immutable
public interface PartialSegment {

    String uri();

    double duration();

    @Value.Default
    default boolean independent() {
        return false;
    }

    Optional<ByteRange> byterange();

    @Value.Default
    default boolean gap() {
        return false;
    }

    static PartialSegment.Builder builder() {
        return new PartialSegment.Builder();
    }

    class Builder extends PartialSegmentBuilder {
    }
}
