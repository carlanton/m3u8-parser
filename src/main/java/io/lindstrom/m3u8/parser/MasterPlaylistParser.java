package io.lindstrom.m3u8.parser;

import io.lindstrom.m3u8.model.MasterPlaylist;

import java.util.Collections;
import java.util.Iterator;

import static io.lindstrom.m3u8.parser.Tags.*;

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
    private final VariantParser variantParser = new VariantParser();
    private final IFrameParser iFrameParser = new IFrameParser();
    private final AlternativeRenditionParser alternativeRenditionParser = new AlternativeRenditionParser();

    @Override
    MasterPlaylist.Builder newBuilder() {
        return MasterPlaylist.builder();
    }

    @Override
    void onTag(MasterPlaylist.Builder builder, String prefix, String attributes, Iterator<String> lineIterator) throws PlaylistParserException {
        switch (prefix) {
            case EXT_X_VERSION:
                builder.version(Integer.parseInt(attributes));
                break;

            case EXT_X_MEDIA:
                builder.addAlternativeRenditions(alternativeRenditionParser.parse(attributes));
                break;

            case EXT_X_STREAM_INF:
                String uriLine = lineIterator.next();
                if (uriLine == null || uriLine.startsWith("#")) {
                    throw new PlaylistParserException("Expected URI, got " + uriLine);
                }
                builder.addVariants(variantParser.parse(attributes,
                        Collections.singletonMap(URI, uriLine)));
                break;

            case EXT_X_I_FRAME_STREAM_INF:
                builder.addIFrameVariants(iFrameParser.parse(attributes));
                break;

            case EXT_X_INDEPENDENT_SEGMENTS:
                builder.independentSegments(true);
                break;

            case EXT_X_START:
                builder.startTimeOffset(startTimeOffsetParser.parse(attributes));
                break;

            case EXT_X_SESSION_DATA:
            case EXT_X_SESSION_KEY:
                throw new PlaylistParserException("Tag not implemented: " + prefix);

            default:
                throw new PlaylistParserException("Invalid line: " + prefix);
        }
    }

    @Override
    void onURI(MasterPlaylist.Builder builder, String uri) throws PlaylistParserException {
        throw new PlaylistParserException("Unexpected URI in master playlist");
    }

    @Override
    MasterPlaylist build(MasterPlaylist.Builder builder) {
        return builder.build();
    }

    @Override
    void write(MasterPlaylist playlist, StringBuilder stringBuilder) {
        playlist.alternativeRenditions()
                .forEach(value -> alternativeRenditionParser.write(value, stringBuilder));

        playlist.variants()
                .forEach(value -> variantParser.write(value, stringBuilder));

        playlist.iFrameVariants()
                .forEach(value -> iFrameParser.write(value, stringBuilder));
    }
}
