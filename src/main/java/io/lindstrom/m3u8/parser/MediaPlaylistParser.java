package io.lindstrom.m3u8.parser;

import io.lindstrom.m3u8.model.MediaPlaylist;
import io.lindstrom.m3u8.model.MediaSegment;
import io.lindstrom.m3u8.model.PartialSegment;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * MediaPlaylistParser can read and write Media Playlists according to RFC 8216 (HTTP Live Streaming).
 * <p>
 * Example usage:
 * <pre>
 * {@code
 * MediaPlaylistParser parser = new MediaPlaylistParser();
 *
 * // Parse playlist
 * MediaPlaylist playlist = parser.readPlaylist(Paths.get("path/to/media-playlist.m3u8"));
 *
 * // Update playlist version
 * MediaPlaylist updated = MediaPlaylist.builder()
 *                                      .from(playlist)
 *                                      .version(2)
 *                                      .build();
 *
 * // Write playlist to standard out
 * System.out.println(parser.writePlaylistAsString(updated));
 * }
 * </pre>
 *
 * This implementation is reusable and thread safe.
 */
public class MediaPlaylistParser extends AbstractPlaylistParser<MediaPlaylist, MediaPlaylistParser.Builder> {
    private final ParsingMode parsingMode;

    public MediaPlaylistParser() {
        this(ParsingMode.STRICT);
    }

    public MediaPlaylistParser(ParsingMode parsingMode) {
        this.parsingMode = parsingMode;
    }

    @Override
    Builder newBuilder() {
        return new Builder();
    }

    @Override
    void onTag(Builder builderWrapper, String name, String attributes, Iterator<String> lineIterator) throws PlaylistParserException {
        if (MediaSegmentTag.EXT_X_PART.tag().equals(name)) {
            builderWrapper.partialSegments.add(PartialSegmentAttribute.parse(attributes, parsingMode));
        } else if (MediaPlaylistTag.tags.containsKey(name)) {
            MediaPlaylistTag.tags.get(name).read(builderWrapper.playlistBuilder, attributes, parsingMode);
        } else if (MediaSegmentTag.tags.containsKey(name)) {
            MediaSegmentTag.tags.get(name).read(builderWrapper.segmentBuilder, attributes, parsingMode);
        } else if (MediaPlaylistEndTag.tags.containsKey(name)){
            MediaPlaylistEndTag.tags.get(name).read(builderWrapper.playlistBuilder, attributes, parsingMode);
        } else if (parsingMode.failOnUnknownTags()) {
            throw new PlaylistParserException("Tag not implemented: " + name);
        }
    }

    @Override
    void onComment(Builder builder, String value) {
        builder.playlistBuilder.addComments(
                value
        );
    }

    @Override
    void onURI(Builder builderWrapper, String uri) {
        builderWrapper.segmentBuilder.uri(uri).partialSegments(builderWrapper.partialSegments);
        builderWrapper.playlistBuilder.addMediaSegments(builderWrapper.segmentBuilder.build());
        builderWrapper.segmentBuilder = MediaSegment.builder();
        builderWrapper.partialSegments = new ArrayList<>();
    }

    @Override
    MediaPlaylist build(Builder builderWrapper) {
        return builderWrapper.playlistBuilder.partialSegments(builderWrapper.partialSegments).build();
    }

    @Override
    void write(MediaPlaylist playlist, TextBuilder textBuilder) {
        for (MediaPlaylistTag tag : MediaPlaylistTag.tags.values()) {
            tag.write(playlist, textBuilder);
        }

        playlist.mediaSegments().forEach(mediaSegment -> {
            for (MediaSegmentTag tag : MediaSegmentTag.tags.values()) {
                tag.write(mediaSegment, textBuilder);
            }
            textBuilder.add(mediaSegment.uri()).add('\n');
        });

        for (MediaPlaylistEndTag tag : MediaPlaylistEndTag.tags.values()) {
            tag.write(playlist, textBuilder);
        }
    }

    /**
     * Wrapper class for playlist and segment builders
     */
    static class Builder {
        private final MediaPlaylist.Builder playlistBuilder = MediaPlaylist.builder();
        private MediaSegment.Builder segmentBuilder = MediaSegment.builder();
        private List<PartialSegment> partialSegments = new ArrayList<>();
    }
}
