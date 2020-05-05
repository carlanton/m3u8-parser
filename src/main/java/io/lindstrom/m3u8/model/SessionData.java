package io.lindstrom.m3u8.model;

import org.immutables.value.Value;

import java.util.Optional;

/**
 * Session data (EXT-X-SESSION-DATA)
 *
 * @see <a href="https://tools.ietf.org/html/rfc8216#section-4.3.4.4" target="_blank">
 * RFC 8216 - 4.3.4.4.  EXT-X-SESSION-DATA</a>
 */
@Value.Immutable
public interface SessionData {
    String dataId();
    Optional<String> value();
    Optional<String> uri();
    Optional<String> language();

    static Builder builder() {
        return new Builder();
    }

    class Builder extends SessionDataBuilder {
    }
}
