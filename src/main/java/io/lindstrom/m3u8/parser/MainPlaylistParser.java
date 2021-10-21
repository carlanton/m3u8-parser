package io.lindstrom.m3u8.parser;

import io.lindstrom.m3u8.model.MainPlaylist;

import java.util.Iterator;

/**
 * MainPlaylistParser can read and write Main Playlists according to RFC 8216 (HTTP Live Streaming).
 * <p>
 * Example usage:
 * <pre>
 * {@code
 * MainPlaylistParser parser = new MainPlaylistParser();
 *
 * // Parse playlist
 * MainPlaylist playlist = parser.readPlaylist(Paths.get("path/to/main.m3u8"));
 *
 * // Update playlist version
 * MainPlaylist updated = MainPlaylist.builder()
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
public class MainPlaylistParser extends AbstractPlaylistParser<MainPlaylist, MainPlaylist.Builder> {
    private final ParsingMode parsingMode;

    public MainPlaylistParser() {
        this(ParsingMode.STRICT);
    }

    public MainPlaylistParser(ParsingMode parsingMode) {
        this.parsingMode = parsingMode;
    }

    @Override
    void write(MainPlaylist playlist, TextBuilder textBuilder) {
        for (MainPlaylistTag tag : MainPlaylistTag.tags.values()) {
            tag.write(playlist, textBuilder);
        }
    }

    @Override
    MainPlaylist.Builder newBuilder() {
        return MainPlaylist.builder();
    }

    @Override
    void onTag(MainPlaylist.Builder builder, String name, String attributes, Iterator<String> lineIterator) throws PlaylistParserException{
        MainPlaylistTag tag = MainPlaylistTag.tags.get(name);

        if (tag == MainPlaylistTag.EXT_X_STREAM_INF) {
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
    void onComment(MainPlaylist.Builder builder, String value) {
        builder.addComments(
                value
        );
    }

    @Override
    MainPlaylist build(MainPlaylist.Builder builder) {
        return builder.build();
    }
}
