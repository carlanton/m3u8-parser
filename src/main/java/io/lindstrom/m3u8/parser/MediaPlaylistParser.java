package io.lindstrom.m3u8.parser;

import io.lindstrom.m3u8.model.Buildable;
import io.lindstrom.m3u8.model.MediaPlaylist;
import io.lindstrom.m3u8.model.MediaSegment;

import java.util.Iterator;
import java.util.Map;

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
public class MediaPlaylistParser extends AbstractPlaylistParser<MediaPlaylist, MediaPlaylist.Builder> {
    private final ParsingMode parsingMode;
    private static final Map<String, MediaSegmentTag> mediaSegmentTags = ParserUtils.toMap(MediaSegmentTag.values());
    private static final Map<String, MediaPlaylistTag> mediaPlaylistTags = ParserUtils.toMap(MediaPlaylistTag.values());

    public MediaPlaylistParser() {
        this(ParsingMode.STRICT);
    }

    public MediaPlaylistParser(ParsingMode parsingMode) {
        this.parsingMode = parsingMode;
    }

    @Override
    MediaPlaylist.Builder newBuilder() {
        return new MediaPlaylist.Builder();
    }

    @Override
    void onTag(MediaPlaylist.Builder builder, String name, String attributes, Iterator<String> lineIterator) throws PlaylistParserException {
        if (mediaPlaylistTags.containsKey(name)) {
            mediaPlaylistTags.get(name).read(builder, attributes, parsingMode);
        } else if (mediaSegmentTags.containsKey(name)) {
            mediaSegmentTags.get(name).read(builder.segmentBuilder(), attributes, parsingMode);
        } else if (parsingMode.failOnUnknownTags()) {
            throw new PlaylistParserException("Tag not implemented: " + name);
        }
    }

    @Override
    void onURI(MediaPlaylist.Builder builder, String uri) {
        builder.addMediaSegments(builder.segmentBuilder().uri(uri).build());
        builder.segmentBuilder(MediaSegment.builder());
    }

    @Override
    void write(MediaPlaylist playlist, TextBuilder textBuilder) {
        for (MediaPlaylistTag tag : mediaPlaylistTags.values()) {
            tag.write(playlist, textBuilder);
        }

        playlist.mediaSegments().forEach(mediaSegment -> {
            for (MediaSegmentTag tag : mediaSegmentTags.values()) {
                tag.write(mediaSegment, textBuilder);
            }
            textBuilder.add(mediaSegment.uri()).add('\n');
        });

        if (!playlist.ongoing()) {
            textBuilder.addTag(MediaPlaylistTag.EXT_X_ENDLIST.tag());
        }
    }
}
