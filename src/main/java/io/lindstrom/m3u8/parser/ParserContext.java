package io.lindstrom.m3u8.parser;

import io.lindstrom.m3u8.model.IBuilder;
import io.lindstrom.m3u8.model.MasterPlaylist;
import io.lindstrom.m3u8.model.MediaPlaylist;
import io.lindstrom.m3u8.model.MediaSegment;

import java.util.Map;

public class ParserContext {
    private final Map<String, Tag<MediaSegment, MediaSegment.Builder>> mediaSegmentTags;
    private final Map<String, Tag<MasterPlaylist, MasterPlaylist.Builder>> masterPlaylistTags;
    private final Map<String, Tag<MediaPlaylist, MediaPlaylist.Builder>> mediaPlaylistTags;

    private final Map<Class<T>> m = null;

    public ParserContext() {
        this.mediaSegmentTags = ParserUtils.toMap(MediaSegmentTag.values());
        this.masterPlaylistTags = ParserUtils.toMap(MasterPlaylistTag.values());
        this.mediaPlaylistTags = ParserUtils.toMap(MediaPlaylistTag.values());
    }

    public <T, B extends IBuilder<T>, X extends Tag<T, B>> X getTags(Class<T> clazz) {
        return (X) m.get(clazz);
    }
}
