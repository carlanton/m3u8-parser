package io.lindstrom.m3u8.parser;

import io.lindstrom.m3u8.model.MediaSegment;
import io.lindstrom.m3u8.model.StandardMediaPlaylistBuilder;
import io.lindstrom.m3u8.model.StandardMediaPlaylist;

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
 * <p>
 * This implementation is reusable and thread safe.
 */
public class MediaPlaylistParser extends AbstractMediaPlaylistParser<StandardMediaPlaylist> {

    public MediaPlaylistParser() {
        super(() -> new Builder());
    }

    /**
     * Wrapper class for playlist and segment builders
     */
    static class Builder implements MediaPlaylistCreator<StandardMediaPlaylist> {
        private final StandardMediaPlaylistBuilder playlistBuilder = StandardMediaPlaylist.builder();
        private MediaSegment.Builder segmentBuilder = MediaSegment.builder();

        @Override
        public StandardMediaPlaylist create() {
            return playlistBuilder.build();
        }


        @Override
        public StandardMediaPlaylistBuilder playlistBuilder() {
            return playlistBuilder;
        }

        @Override
        public MediaSegment.Builder segmentBuilder() {
            return segmentBuilder;
        }

        @Override
        public void newSegmentBuilder(MediaSegment.Builder builder) {
            this.segmentBuilder = builder;
        }
    }
}
