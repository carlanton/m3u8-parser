package io.lindstrom.m3u8.model;

import org.immutables.value.Value;

import java.util.List;
import java.util.Optional;

@Value.Immutable
public interface IFramePlaylist {
    String uri();
    long bandwidth();
    Optional<Long> averageBandwidth();
    List<String> codecs();
    Optional<String> resolution();
    Optional<String> hdcpLevel();
    Optional<String> video();

    static Builder builder() {
        return new Builder();
    }

    class Builder extends IFramePlaylistBuilder {}
}
