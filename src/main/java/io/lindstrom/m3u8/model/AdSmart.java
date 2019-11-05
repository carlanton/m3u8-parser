package io.lindstrom.m3u8.model;

import org.immutables.value.Value;

import javax.annotation.Nullable;
import java.util.List;

@Value.Immutable
public interface  AdSmart {

    @Nullable
    CueOut cueOut();

    @Nullable
    CueIn cueIn();

    @Nullable
    List<CueSpan> cueSpan();

    static AdSmartBuilder builder() {
        return new AdSmartBuilder();
    }
}
