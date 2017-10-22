package io.lindstrom.m3u8.model;

import org.immutables.value.Value;

import java.util.Optional;

@Value.Immutable
public interface ByteRange {
    long subRangeOffset();
    Optional<Long> offset();

    static Builder builder() {
        return new Builder();
    }

    class Builder extends ByteRangeBuilder {}

    static ByteRange of(int subRangeOffset) {
        return builder()
                .subRangeOffset(subRangeOffset)
                .build();
    }

    static ByteRange of(int subRangeOffset, long offset) {
        return builder()
                .subRangeOffset(subRangeOffset)
                .offset(offset)
                .build();
    }
}
