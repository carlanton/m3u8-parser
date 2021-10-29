package io.lindstrom.m3u8.model;

import org.immutables.value.Value;

import java.util.List;
import java.util.Optional;

/**
 * I-Frame variant stream (EXT-X-I-FRAME-STREAM-INF)
 *
 * @see <a href="https://tools.ietf.org/html/rfc8216#section-4.3.4.3" target="_blank">
 * RFC 8216 - 4.3.4.3.  EXT-X-I-FRAME-STREAM-INF</a>
 * @see Variant
 */
@Value.Immutable
public interface IFrameVariant {
    String uri();

    long bandwidth();

    Optional<Long> averageBandwidth();

    Optional<Double> score();

    List<String> codecs();

    Optional<Resolution> resolution();

    Optional<String> hdcpLevel();

    List<String> allowedCpc();

    Optional<String> stableVariantId();

    Optional<String> video();

    Optional<Integer> programId();
    
    Optional<VideoRange> videoRange();

    Optional<String> name();

    Optional<String> language();
    
    static Builder builder() {
        return new Builder();
    }

    class Builder extends IFrameVariantBuilder {
        public Builder videoRange(String value) {
            return videoRange(VideoRange.valueOf(value));
        }
    }
}
