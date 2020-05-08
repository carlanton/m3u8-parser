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

    enum TagMappers implements Tag<MasterPlaylist, MasterPlaylist.Builder> {
        EXT_X_VERSION {
            @Override
            public void read(MasterPlaylist.Builder builder, String attributes, Iterator<String> lineIterator) {
                builder.version(Integer.parseInt(attributes));
            }

            @Override
            public void write(MasterPlaylist playlist, TextBuilder textBuilder) {
                // TODO
            }
        },

        EXT_X_MEDIA {
            @Override
            public void read(MasterPlaylist.Builder builder, String attributes, Iterator<String> lineIterator) throws PlaylistParserException {
                builder.addAlternativeRenditions(
                        readAttributes(
                                AlternativeRenditionParser.class, attributes, AlternativeRendition.builder()
                        ).build()
                );
            }

            @Override
            public void write(MasterPlaylist playlist, TextBuilder textBuilder) {
                playlist.alternativeRenditions()
                        .forEach(value -> textBuilder.add(tag(), value, AlternativeRenditionParser.class));
            }
        },

        EXT_X_STREAM_INF {
            @Override
            public void read(MasterPlaylist.Builder builder, String attributes, Iterator<String> lineIterator) throws PlaylistParserException {
                String uriLine = lineIterator.next();
                if (uriLine == null || uriLine.startsWith("#")) {
                    throw new PlaylistParserException("Expected URI, got " + uriLine);
                }

                builder.addVariants(
                        readAttributes(VariantParser.class, attributes, Variant.builder())
                                .uri(uriLine)
                                .build()
                );
            }

            @Override
            public void write(MasterPlaylist playlist, TextBuilder textBuilder) {
                playlist.variants()
                        .forEach(value -> textBuilder.add(tag(), value, VariantParser.class));
            }
        },

        EXT_X_I_FRAME_STREAM_INF {
            @Override
            public void read(MasterPlaylist.Builder builder, String attributes, Iterator<String> lineIterator) throws PlaylistParserException {
                builder.addIFrameVariants(readAttributes(IFrameParser.class, attributes, IFrameVariant.builder()).build());
            }

            @Override
            public void write(MasterPlaylist playlist, TextBuilder textBuilder) {
                playlist.iFrameVariants()
                        .forEach(value -> textBuilder.add(tag(), value, IFrameParser.class));
            }
        },

        EXT_X_INDEPENDENT_SEGMENTS {
            @Override
            public void read(MasterPlaylist.Builder builder, String attributes, Iterator<String> lineIterator) throws PlaylistParserException {
                builder.independentSegments(true);
            }

            @Override
            public void write(MasterPlaylist playlist, TextBuilder textBuilder) {
                // TODO
            }
        },

        EXT_X_START {
            @Override
            public void read(MasterPlaylist.Builder builder, String attributes, Iterator<String> lineIterator) throws PlaylistParserException {
                //builder.startTimeOffset(startTimeOffsetParser.parse(attributes));
            }

            @Override
            public void write(MasterPlaylist playlist, TextBuilder textBuilder) {
                // TODO
            }
        },

        EXT_X_SESSION_DATA {
            @Override
            public void read(MasterPlaylist.Builder builder, String attributes, Iterator<String> lineIterator) throws PlaylistParserException {
                builder.addSessionData(readAttributes(SessionDataParser.class, attributes, SessionData.builder()).build());
            }

            @Override
            public void write(MasterPlaylist playlist, TextBuilder textBuilder) {
                playlist.sessionData()
                        .forEach(value -> textBuilder.add(tag(), value, SessionDataParser.class));
            }
        },

        EXT_X_SESSION_KEY {
            private final Class<SegmentKeyParser> attributeMapper = SegmentKeyParser.class;

            @Override
            public void read(MasterPlaylist.Builder builder, String attributes, Iterator<String> lineIterator) throws PlaylistParserException {
                builder.addSessionKeys(
                        readAttributes(attributeMapper, attributes, SegmentKey.builder()).build()
                );
            }

            @Override
            public void write(MasterPlaylist playlist, TextBuilder textBuilder) {
                playlist.sessionKeys()
                        .forEach(value -> textBuilder.add(tag(), value, attributeMapper));
            }
        }
    }



    @Override
    void write(MasterPlaylist playlist, TextBuilder textBuilder) {
        for (TagMappers mapper : TagMappers.values()) {
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
        TagMappers.valueOf(name).read(builder, attributes, lineIterator);
    }

    @Override
    MasterPlaylist build(MasterPlaylist.Builder builder) {
        return builder.build();
    }
}
