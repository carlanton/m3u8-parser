package io.lindstrom.m3u8.parser;

import io.lindstrom.m3u8.model.Playlist;
import io.lindstrom.m3u8.model.PlaylistBuilder;
import io.lindstrom.m3u8.model.PlaylistVariable;
import io.lindstrom.m3u8.model.StartTimeOffset;

import java.util.*;

public class RawPlaylist implements Playlist {
    private final Map<String, List<List<RawAttribute>>> tags;

    RawPlaylist(Map<String, List<List<RawAttribute>>> tags) {
        this.tags = tags;
    }

    @Override
    public Optional<Integer> version() {
        return Optional.empty();
    }

    @Override
    public Optional<StartTimeOffset> startTimeOffset() {
        return Optional.empty();
    }

    @Override
    public List<PlaylistVariable> variables() {
        return Collections.emptyList();
    }

    public Map<String, List<List<RawAttribute>>> tags() {
        return tags;
    }

    static class Builder implements PlaylistBuilder<RawPlaylist> {
        private final Map<String, List<List<RawAttribute>>> tags = new TreeMap<>();
        void addTag(String tag, List<RawAttribute> attributes) {
            tags.computeIfAbsent(tag, k -> new ArrayList<>()).add(attributes);
        }

        public RawPlaylist build() {
            return new RawPlaylist(tags);
        }
    }
}
