package io.lindstrom.m3u8.model;


import org.immutables.value.Value;

import java.time.OffsetDateTime;
import java.util.Map;
import java.util.Optional;

/**
 * Date Range (EXT-X-DATERANGE)
 *
 * @see <a href="https://tools.ietf.org/html/rfc8216#section-4.3.2.7" target="_blank">
 * RFC 8216 - 4.3.2.7.  EXT-X-DATERANGE</a>
 */
@Value.Immutable
public interface DateRange {
    String id();
    Optional<String> classAttribute();
    OffsetDateTime startDate();
    Optional<OffsetDateTime> endDate();
    Optional<Double> duration();
    Optional<Double> plannedDuration();

    Map<String, String> clientAttributes();

    Optional<String> scte35Cmd();
    Optional<String> scte35Out();
    Optional<String> scte35In();

    @Value.Default
    default boolean endOnNext() {
        return false;
    }

    static Builder builder() {
        return new Builder();
    }

    class Builder extends DateRangeBuilder {
    }
}
