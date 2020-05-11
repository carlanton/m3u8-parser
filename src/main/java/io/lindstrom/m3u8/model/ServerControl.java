package io.lindstrom.m3u8.model;

import org.immutables.value.Value;

import java.util.Optional;

@Value.Immutable
public interface ServerControl {
    Optional<Double> canSkipUntil();

    Optional<Boolean> canSkipDateRanges();

    Optional<Double> holdBack();

    Optional<Double> partHoldBack();

    @Value.Default
    default boolean canBlockReload() {
        return false;
    }

    static Builder builder() {
        return new Builder();
    }

    class Builder extends ServerControlBuilder {
    }
}
