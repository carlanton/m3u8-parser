package io.lindstrom.m3u8.parser;

import io.lindstrom.m3u8.model.*;

import java.util.Iterator;

import static io.lindstrom.m3u8.parser.AbstractPlaylistParser.readAttributes;

enum MasterPlaylistTags implements Tag<MasterPlaylist, MasterPlaylist.Builder> {
    EXT_X_VERSION {
        @Override
        public void read(MasterPlaylist.Builder builder, String attributes, Iterator<String> lineIterator) {
            builder.version(Integer.parseInt(attributes));
        }

        @Override
        public void write(MasterPlaylist playlist, TextBuilder textBuilder) {
            playlist.version().ifPresent(version -> textBuilder.add(tag())
                    .add(":").add(version).add('\n'));
        }
    },

    EXT_X_INDEPENDENT_SEGMENTS {
        @Override
        public void read(MasterPlaylist.Builder builder, String attributes, Iterator<String> lineIterator) {
            builder.independentSegments(true);
        }

        @Override
        public void write(MasterPlaylist playlist, TextBuilder textBuilder) {
            if (playlist.independentSegments()) {
                textBuilder.add(tag()).add('\n');
            }
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
            textBuilder.add(tag(), playlist.alternativeRenditions(), AlternativeRenditionParser.class);
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
            textBuilder.add(tag(), playlist.variants(), VariantParser.class);
        }
    },

    EXT_X_I_FRAME_STREAM_INF {
        @Override
        public void read(MasterPlaylist.Builder builder, String attributes, Iterator<String> lineIterator) throws PlaylistParserException {
            builder.addIFrameVariants(readAttributes(IFrameParser.class, attributes, IFrameVariant.builder()).build());
        }

        @Override
        public void write(MasterPlaylist playlist, TextBuilder textBuilder) {
            textBuilder.add(tag(), playlist.iFrameVariants(), IFrameParser.class);
        }
    },

    EXT_X_START {
        @Override
        public void read(MasterPlaylist.Builder builder, String attributes, Iterator<String> lineIterator) throws PlaylistParserException {
            builder.startTimeOffset(readAttributes(StartTimeOffsetParser.class, attributes, StartTimeOffset.builder()).build());
        }

        @Override
        public void write(MasterPlaylist playlist, TextBuilder textBuilder) {
            textBuilder.add(tag(), playlist.startTimeOffset(), StartTimeOffsetParser.class);
        }
    },

    EXT_X_SESSION_DATA {
        @Override
        public void read(MasterPlaylist.Builder builder, String attributes, Iterator<String> lineIterator) throws PlaylistParserException {
            builder.addSessionData(readAttributes(SessionDataParser.class, attributes, SessionData.builder()).build());
        }

        @Override
        public void write(MasterPlaylist playlist, TextBuilder textBuilder) {
            textBuilder.add(tag(), playlist.sessionData(), SessionDataParser.class);
        }
    },

    EXT_X_SESSION_KEY {
        @Override
        public void read(MasterPlaylist.Builder builder, String attributes, Iterator<String> lineIterator) throws PlaylistParserException {
            builder.addSessionKeys(
                    readAttributes(SegmentKeyParser.class, attributes, SegmentKey.builder()).build()
            );
        }

        @Override
        public void write(MasterPlaylist playlist, TextBuilder textBuilder) {
            textBuilder.add(tag(), playlist.sessionKeys(), SegmentKeyParser.class);
        }
    }
}
