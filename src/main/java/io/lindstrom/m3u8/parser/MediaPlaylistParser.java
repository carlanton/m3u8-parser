package io.lindstrom.m3u8.parser;

import io.lindstrom.m3u8.model.*;

import java.util.Iterator;

import static io.lindstrom.m3u8.parser.Tags.*;

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

    @Override
    Builder newBuilder() {
        return new Builder();
    }

    @Override
    void onTag(Builder builderWrapper, String name, String attributes, Iterator<String> lineIterator) throws PlaylistParserException {
        for (MediaPlaylistTag tag : MediaPlaylistTag.values()) {
            if (tag.name().equals(name)) {
                tag.read(builderWrapper.playlistBuilder, attributes);
                return;
            }
        }

        for (MediaSegmentTag tag : MediaSegmentTag.values()) {
            if (tag.name().equals(name)) {
                tag.read(builderWrapper.segmentBuilder, attributes);
                return;
            }
        }

        // unknown tag
        throw new PlaylistParserException("Tag not implemented: " + name.replace("_", "-"));
    }

    @Override
    void onURI(Builder builderWrapper, String uri) {
        builderWrapper.segmentBuilder.uri(uri);
        builderWrapper.playlistBuilder.addMediaSegments(builderWrapper.segmentBuilder.build());
        builderWrapper.segmentBuilder = MediaSegment.builder();
    }

    @Override
    MediaPlaylist build(Builder builderWrapper) {
        return builderWrapper.playlistBuilder.build();
    }

    @Override
    void write(MediaPlaylist playlist, TextBuilder textBuilder) {
        for (MediaPlaylistTag mapper : MediaPlaylistTag.values()) {
            mapper.write(playlist, textBuilder);
        }

        playlist.mediaSegments().forEach(mediaSegment -> {
            for (MediaSegmentTag mapper : MediaSegmentTag.values()) {
                mapper.write(mediaSegment, textBuilder);
            }
            textBuilder.add(mediaSegment.uri()).add('\n');
        });

        if (!playlist.ongoing()) {
            textBuilder.add(EXT_X_ENDLIST).add('\n');
        }
    }

    /**
     * Wrapper class for playlist and segment builders
     */
    static class Builder {
        private final MediaPlaylist.Builder playlistBuilder = MediaPlaylist.builder();
        private MediaSegment.Builder segmentBuilder = MediaSegment.builder();
    }
}
