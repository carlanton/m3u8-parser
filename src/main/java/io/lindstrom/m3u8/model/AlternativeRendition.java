package io.lindstrom.m3u8.model;

import org.immutables.value.Value;

import java.util.List;
import java.util.Optional;

@Value.Immutable
public interface AlternativeRendition {
    MediaType type();
    Optional<String> uri();
    String groupdId();
    Optional<String> language();
    Optional<String> assocLanguage();
    String name();
    Optional<Boolean> getDefault();
    Optional<Boolean> autoSelect();
    Optional<Boolean> forced();
    Optional<String> inStreamId();
    List<String> characteristics();
    List<String> channels();

    static Builder builder() {
        return new Builder();
    }

    class Builder extends AlternativeRenditionBuilder {}

}
