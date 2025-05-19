package io.lindstrom.m3u8.parser;

import io.lindstrom.m3u8.model.MultivariantPlaylist;

import java.util.Iterator;

/**
 * MultivariantPlaylistParser can read and write Multivariant Playlists according to RFC 8216 (HTTP Live Streaming).
 * <p>
 * Example usage:
 * <pre>
 * {@code
 * MultivariantPlaylistParser parser = new MultivariantPlaylistParser();
 *
 * // Parse playlist
 * MultivariantPlaylist playlist = parser.readPlaylist(Paths.get("path/to/playlist.m3u8"));
 *
 * // Update playlist version
 * MultivariantPlaylist updated = MultivariantPlaylist.builder()
 *                                        .from(playlist)
 *                                        .version(2)
 *                                        .build();
 *
 * // Write playlist to standard out
 * System.out.println(parser.writePlaylistAsString(updated));
 * }
 * </pre>
 *
 * This implementation is reusable and thread safe.
 */
public class MultivariantPlaylistParser extends AbstractPlaylistParser<MultivariantPlaylist, MultivariantPlaylist.Builder> {
    private final ParsingMode parsingMode;

    public MultivariantPlaylistParser() {
        this(ParsingMode.STRICT);
    }

    public MultivariantPlaylistParser(ParsingMode parsingMode) {
        this.parsingMode = parsingMode;
    }

    @Override
    void write(MultivariantPlaylist playlist, TextBuilder textBuilder) {
        for (MultivariantPlaylistTag tag : MultivariantPlaylistTag.tags.values()) {
            tag.write(playlist, textBuilder);
        }
    }

    @Override
    MultivariantPlaylist.Builder newBuilder() {
        return MultivariantPlaylist.builder();
    }

    @Override
    void onTag(MultivariantPlaylist.Builder builder, String name, String attributes, Iterator<String> lineIterator) throws PlaylistParserException{
        MultivariantPlaylistTag tag = MultivariantPlaylistTag.tags.get(name);

        if (tag == MultivariantPlaylistTag.EXT_X_STREAM_INF) {
            String uriLine = lineIterator.next();
            if (uriLine == null || uriLine.startsWith("#")) {
                throw new PlaylistParserException("Expected URI, got " + uriLine);
            }
            builder.addVariants(VariantAttribute.parse(attributes, uriLine, parsingMode));
        } else if (tag != null) {
            tag.read(builder, attributes, parsingMode);
        } else if (parsingMode.failOnUnknownTags()) {
            throw new PlaylistParserException("Tag not implemented: " + name);
        }
    }

    @Override
    void onComment(MultivariantPlaylist.Builder builder, String value) {
        builder.addComments(
                value
        );
    }

    @Override
    MultivariantPlaylist build(MultivariantPlaylist.Builder builder) {
        return builder.build();
    }
}