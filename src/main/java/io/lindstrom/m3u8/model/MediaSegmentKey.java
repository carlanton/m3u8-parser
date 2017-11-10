package io.lindstrom.m3u8.model;

import org.immutables.value.Value;

import java.util.Optional;

@Value.Immutable
public interface MediaSegmentKey {
    KeyMethod method();

    Optional<String> uri();

    Optional<String> iv();

    Optional<String> keyFormat();

    Optional<String> keyFormatVersions();

    static Builder builder() {
        return new Builder();
    }

    class Builder extends MediaSegmentKeyBuilder {}
}
