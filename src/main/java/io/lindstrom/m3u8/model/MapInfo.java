package io.lindstrom.m3u8.model;

import org.immutables.value.Value;

import java.util.Optional;

@Value.Immutable
public interface MapInfo {
    String uri();

    Optional<ByteRange> byteRange();

    static Builder builder() {
        return new Builder();
    }

    class Builder extends MapInfoBuilder {
    }

    static MapInfo of(String uri) {
        return builder()
                .uri(uri)
                .build();
    }

    static MapInfo of(String uri, ByteRange byteRange) {
        return builder()
                .uri(uri)
                .byteRange(byteRange)
                .build();
    }
}
