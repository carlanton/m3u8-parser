package io.lindstrom.m3u8.model;

import org.immutables.value.Value;

import java.util.List;
import java.util.Optional;

@Value.Immutable
public interface Variant {
    long bandwidth();
    Optional<Long> averageBandwidth();
    List<String> codecs();
    Optional<Resolution> resolution();
    Optional<Double> frameRate();
    Optional<String> hdcpLevel();
    Optional<String> audio();
    Optional<String> video();
    Optional<String> subtitles();
    Optional<String> closedCaptions();
    String uri();

    static Builder builder() {
        return new Builder();
    }

    class Builder extends VariantBuilder {}
}
