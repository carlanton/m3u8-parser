package io.lindstrom.m3u8.model;

import org.immutables.value.Value;

import java.util.Optional;

@Value.Immutable
public interface RenditionReport {

    String uri();

    Optional<Long> lastMediaSequenceNumber();

    Optional<Long> lastPartialSegmentIndex();

    static Builder builder() {
        return new Builder();
    }

    static RenditionReport of(String uri) {
        return builder().uri(uri).build();
    }

    class Builder extends RenditionReportBuilder {

    }
}
