package io.lindstrom.m3u8.parser;

import io.lindstrom.m3u8.model.*;

import java.util.Iterator;

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

    @Override
    void write(MasterPlaylist playlist, TextBuilder textBuilder) {
        for (MasterPlaylistTag mapper : MasterPlaylistTag.values()) {
            mapper.write(playlist, textBuilder);
        }
    }

    @Override
    MasterPlaylist.Builder newBuilder() {
        return MasterPlaylist.builder();
    }

    @Override
    void onTag(MasterPlaylist.Builder builder, String prefix, String attributes, Iterator<String> lineIterator) throws PlaylistParserException{
        String name = prefix.substring(1).replace("-", "_"); // TODO FIXME

        MasterPlaylistTag tag = MasterPlaylistTag.valueOf(name);

        if (tag == MasterPlaylistTag.EXT_X_STREAM_INF) {
            String uriLine = lineIterator.next();
            if (uriLine == null || uriLine.startsWith("#")) {
                throw new PlaylistParserException("Expected URI, got " + uriLine);
            }

            attributes += (attributes.isEmpty() ? "" : ",") + "URI=" + uriLine;
        }

        tag.read(builder, attributes);
    }

    @Override
    MasterPlaylist build(MasterPlaylist.Builder builder) {
        return builder.build();
    }
}
