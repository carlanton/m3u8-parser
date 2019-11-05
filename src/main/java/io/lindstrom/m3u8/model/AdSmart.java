package io.lindstrom.m3u8.model;

import org.immutables.value.Value;

import javax.annotation.Nullable;

@Value.Immutable
public interface  AdSmart {

    @Nullable
    CueOut cueOut();

    @Nullable
    CueIn cueIn();

    static AdSmartBuilder builder() {
        return new AdSmartBuilder();
    }
}
