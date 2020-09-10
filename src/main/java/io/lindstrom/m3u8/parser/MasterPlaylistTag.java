package io.lindstrom.m3u8.parser;

import io.lindstrom.m3u8.model.MasterPlaylist;

import java.util.Map;

enum MasterPlaylistTag implements Tag<MasterPlaylist, MasterPlaylist.Builder> {
    EXT_X_VERSION {
        @Override
        public void read(MasterPlaylist.Builder builder, String attributes) {
            builder.version(Integer.parseInt(attributes));
        }

        @Override
        public void write(MasterPlaylist playlist, TextBuilder textBuilder) {
            playlist.version().ifPresent(version -> textBuilder.addTag(tag(), version));
        }
    },

    EXT_X_INDEPENDENT_SEGMENTS {
        @Override
        public void read(MasterPlaylist.Builder builder, String attributes) {
            builder.independentSegments(true);
        }

        @Override
        public void write(MasterPlaylist playlist, TextBuilder textBuilder) {
            if (playlist.independentSegments()) {
                textBuilder.addTag(tag());
            }
        }
    },

    EXT_X_START {
        @Override
        public void read(MasterPlaylist.Builder builder, String attributes) throws PlaylistParserException {
            builder.startTimeOffset(StartTimeOffsetAttribute.parse(attributes));
        }

        @Override
        public void write(MasterPlaylist playlist, TextBuilder textBuilder) {
            playlist.startTimeOffset().ifPresent(value -> textBuilder.addTag(tag(), value, StartTimeOffsetAttribute.attributeMap));
        }
    },

    EXT_X_DEFINE {
        @Override
        public void read(MasterPlaylist.Builder builder, String attributes) throws PlaylistParserException {
            builder.addVariables(PlaylistVariableAttribute.parse(attributes));
        }

        @Override
        public void write(MasterPlaylist playlist, TextBuilder textBuilder) {
            textBuilder.addTag(tag(), playlist.variables(), PlaylistVariableAttribute.attributeMap);
        }
    },

    EXT_X_MEDIA {
        @Override
        public void read(MasterPlaylist.Builder builder, String attributes) throws PlaylistParserException {
            builder.addAlternativeRenditions(AlternativeRenditionAttribute.parse(attributes));
        }

        @Override
        public void write(MasterPlaylist playlist, TextBuilder textBuilder) {
            textBuilder.addTag(tag(), playlist.alternativeRenditions(), AlternativeRenditionAttribute.attributeMap);
        }
    },

    EXT_X_STREAM_INF {
        @Override
        public void read(MasterPlaylist.Builder builder, String attributes) throws PlaylistParserException {
            builder.addVariants(VariantAttribute.parse(attributes));
        }

        @Override
        public void write(MasterPlaylist playlist, TextBuilder textBuilder) {
            textBuilder.addTag(tag(), playlist.variants(), VariantAttribute.attributeMap);
        }
    },

    EXT_X_I_FRAME_STREAM_INF {
        @Override
        public void read(MasterPlaylist.Builder builder, String attributes) throws PlaylistParserException {
            builder.addIFrameVariants(IFrameVariantAttribute.parse(attributes));
        }

        @Override
        public void write(MasterPlaylist playlist, TextBuilder textBuilder) {
            textBuilder.addTag(tag(), playlist.iFrameVariants(), IFrameVariantAttribute.attributeMap);
        }
    },

    EXT_X_SESSION_DATA {
        @Override
        public void read(MasterPlaylist.Builder builder, String attributes) throws PlaylistParserException {
            builder.addSessionData(SessionDataAttribute.parse(attributes));
        }

        @Override
        public void write(MasterPlaylist playlist, TextBuilder textBuilder) {
            textBuilder.addTag(tag(), playlist.sessionData(), SessionDataAttribute.attributeMap);
        }
    },

    EXT_X_SESSION_KEY {
        @Override
        public void read(MasterPlaylist.Builder builder, String attributes) throws PlaylistParserException {
            builder.addSessionKeys(SegmentKeyAttribute.parse(attributes));
        }

        @Override
        public void write(MasterPlaylist playlist, TextBuilder textBuilder) {
            textBuilder.addTag(tag(), playlist.sessionKeys(), SegmentKeyAttribute.attributeMap);
        }
    };

    static final Map<String, MasterPlaylistTag> tags = ParserUtils.toMap(values(), Tag::tag);
}
