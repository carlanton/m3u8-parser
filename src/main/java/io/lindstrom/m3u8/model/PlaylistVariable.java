package io.lindstrom.m3u8.model;

import org.immutables.value.Value;

import java.util.Optional;

@Value.Immutable
public interface PlaylistVariable {
    Optional<String> name();
    Optional<String> value();
    Optional<String> importAttribute();

    static Builder builder() {
        return new Builder();
    }

    class Builder extends PlaylistVariableBuilder { }

    static PlaylistVariable of(String name, String value) {
        return PlaylistVariable.builder()
                .name(name)
                .value(value)
                .build();
    }
}
