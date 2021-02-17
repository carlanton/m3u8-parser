package io.lindstrom.m3u8.parser;

import io.lindstrom.m3u8.model.MasterPlaylist;
import io.lindstrom.m3u8.model.Variant;

import java.util.Iterator;
import java.util.Map;

/**
 * MasterPlaylistParser can read and write Master Playlists according to RFC 8216 (HTTP Live Streaming).
 * <p>
 * Example usage:
 * <pre>
 * {@code
 * MasterPlaylistParser parser = new MasterPlaylistParser();
 *
 * // Parse playlist
 * MasterPlaylist playlist = parser.readPlaylist(Paths.get("path/to/master.m3u8"));
 *
 * // Update playlist version
 * MasterPlaylist updated = MasterPlaylist.builder()
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
public class MasterPlaylistParser extends AbstractPlaylistParser<MasterPlaylist, MasterPlaylist.Builder> {
    private final ParsingMode parsingMode;
    private static final Map<String, MasterPlaylistTag> tags = ParserUtils.toMap(MasterPlaylistTag.values());
    private static final Map<String, VariantAttribute> variantAttributeMap = ParserUtils.toMap(VariantAttribute.values());

    public MasterPlaylistParser() {
        this(ParsingMode.STRICT);
    }

    public MasterPlaylistParser(ParsingMode parsingMode) {
        this.parsingMode = parsingMode;
    }

    @Override
    void write(MasterPlaylist playlist, TextBuilder textBuilder) {
        for (MasterPlaylistTag tag : tags.values()) {
            tag.write(playlist, textBuilder);
        }
    }

    @Override
    MasterPlaylist.Builder newBuilder() {
        return MasterPlaylist.builder();
    }

    @Override
    void onTag(MasterPlaylist.Builder builder, String name, String attributes, Iterator<String> lineIterator) throws PlaylistParserException{
        MasterPlaylistTag tag = tags.get(name);

        if (tag == MasterPlaylistTag.EXT_X_STREAM_INF) {
            String uriLine = lineIterator.next();
            if (uriLine == null || uriLine.startsWith("#")) {
                throw new PlaylistParserException("Expected URI, got " + uriLine);
            }
            builder.addVariants(ParserUtils.readAttributes(variantAttributeMap, attributes, Variant.builder().uri(uriLine), parsingMode));
        } else if (tag != null) {
            tag.read(builder, attributes, parsingMode);
        } else if (parsingMode.failOnUnknownTags()) {
            throw new PlaylistParserException("Tag not implemented: " + name);
        }
    }
}
