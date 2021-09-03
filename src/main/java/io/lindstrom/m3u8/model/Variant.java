package io.lindstrom.m3u8.model;

import org.immutables.value.Value;

import java.util.List;
import java.util.Optional;

/**
 * Variant Stream (EXT-X-STREAM-INF)
 *
 * @see <a href="https://tools.ietf.org/html/rfc8216#section-4.3.4.1" target="_blank">
 * RFC 8216 - 4.3.4.2.  EXT-X-STREAM-INF</a>
 */
@Value.Immutable
public interface Variant {
    /**
     * The value is a decimal-integer of bits per second.  It represents the peak segment
     * bit rate of the Variant Stream.
     *
     * @return variant attribute BANDWIDTH
     */
    long bandwidth();

    /**
     * The value is a decimal-integer of bits per second.  It represents the average segment
     * bit rate of the Variant Stream.
     *
     * @return variant attribute AVERAGE-BANDWIDTH
     */
    Optional<Long> averageBandwidth();

    /**
     * @return variant attribute SCORE
     */
    Optional<Double> score();

    /**
     * @return variant attribute CODECS
     */
    List<String> codecs();

    /**
     * @return variant attribute RESOLUTION
     */
    Optional<Resolution> resolution();

    /**
     * @return variant attribute FRAME-RATE
     */
    Optional<Double> frameRate();

    /**
     * @return variant attribute HDCP-LEVEL
     */
    Optional<String> hdcpLevel();

    /**
     * @return variant attribute ALLOWED-CPC
     */
    List<String> allowedCpc();

    /**
     * @return variant attribute STABLE-VARIANT-ID
     */
    Optional<String> stableVariantId();

    /**
     * @return variant attribute AUDIO
     */
    Optional<String> audio();

    /**
     * @return variant attribute VIDEO
     */
    Optional<String> video();

    /**
     * @return variant attribute SUBTITLES
     */
    Optional<String> subtitles();

    /**
     * @return variant attribute CLOSED-CAPTIONS
     */
    Optional<String> closedCaptions();

    /**
     * @return Set to true to get CLOSED-CAPTIONS=NONE
     */
    Optional<Boolean> closedCaptionsNone();

    /**
     * @return URI to the media playlist
     */
    String uri();

    /**
     * @return variant attribute PROGRAM-ID
     */
    Optional<Integer> programId();

    /**
     * @return variant attribute VIDEO-RANGE
     */
    Optional<VideoRange> videoRange();

    static Builder builder() {
        return new Builder();
    }

    class Builder extends VariantBuilder {
        public Builder resolution(int width, int height) {
            return resolution(Resolution.of(width, height));
        }
        
        public Builder videoRange(String value) {
            return videoRange(VideoRange.valueOf(value));
        }
    }
}
