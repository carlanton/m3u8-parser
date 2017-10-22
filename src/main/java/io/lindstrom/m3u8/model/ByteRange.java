package io.lindstrom.m3u8.model;

import org.immutables.value.Value;

import java.util.Optional;

@Value.Immutable
public interface ByteRange {
    long length();
    Optional<Long> offset();

    static Builder builder() {
        return new Builder();
    }

    class Builder extends ByteRangeBuilder {}

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
