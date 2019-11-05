package io.lindstrom.m3u8.model;

import org.immutables.value.Value;

@Value.Immutable
public interface CueIn {

    String id();

    static Builder builder() {
        return new Builder();
    }
    class Builder extends CueInBuilder {}
}
