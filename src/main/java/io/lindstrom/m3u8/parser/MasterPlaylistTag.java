package io.lindstrom.m3u8.parser;

import io.lindstrom.m3u8.model.*;

import java.util.Map;

enum MasterPlaylistTag implements Tag<MasterPlaylist, MasterPlaylist.Builder> {
    EXT_X_VERSION {
        @Override
        public void read(MasterPlaylist.Builder builder, String attributes, ParsingMode parsingMode) {
            builder.version(Integer.parseInt(attributes));
        }

        @Override
        public void write(MasterPlaylist playlist, TextBuilder textBuilder) {
            playlist.version().ifPresent(version -> textBuilder.addTag(tag(), version));
        }
    },

    EXT_X_INDEPENDENT_SEGMENTS {
        @Override
        public void read(MasterPlaylist.Builder builder, String attributes, ParsingMode parsingMode) {
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
        private final Map<String, StartTimeOffsetAttribute> attributeMap = ParserUtils.toMap(StartTimeOffsetAttribute.values());

        @Override
        public void read(MasterPlaylist.Builder builder, String attributes, ParsingMode parsingMode) throws PlaylistParserException {
            builder.startTimeOffset(ParserUtils.readAttributes(attributeMap, attributes, StartTimeOffset.builder(), parsingMode));
        }

        @Override
        public void write(MasterPlaylist playlist, TextBuilder textBuilder) {
            playlist.startTimeOffset().ifPresent(value -> textBuilder.addTag(tag(), value, attributeMap));
        }
    },

    EXT_X_DEFINE {
        private final Map<String, PlaylistVariableAttribute> attributeMap = ParserUtils.toMap(PlaylistVariableAttribute.values());

        @Override
        public void read(MasterPlaylist.Builder builder, String attributes, ParsingMode parsingMode) throws PlaylistParserException {
            builder.addVariables(ParserUtils.readAttributes(attributeMap, attributes, PlaylistVariable.builder(), parsingMode));
        }

        @Override
        public void write(MasterPlaylist playlist, TextBuilder textBuilder) {
            textBuilder.addTag(tag(), playlist.variables(), attributeMap);
        }
    },

    EXT_X_MEDIA {
        private final Map<String, AlternativeRenditionAttribute> attributeMap =
                ParserUtils.toMap(AlternativeRenditionAttribute.values());

        @Override
        public void read(MasterPlaylist.Builder builder, String attributes, ParsingMode parsingMode) throws PlaylistParserException {
            builder.addAlternativeRenditions(ParserUtils.readAttributes(attributeMap, attributes, AlternativeRendition.builder(), parsingMode));
        }

        @Override
        public void write(MasterPlaylist playlist, TextBuilder textBuilder) {
            textBuilder.addTag(tag(), playlist.alternativeRenditions(), attributeMap);
        }
    },

    EXT_X_STREAM_INF {
        private final Map<String, VariantAttribute> attributeMap = ParserUtils.toMap(VariantAttribute.values());

        @Override
        public void read(MasterPlaylist.Builder builder, String attributes, ParsingMode parsingMode) {
            // Not used. This is handled by the MasterPlaylistParser directly.
        }

        @Override
        public void write(MasterPlaylist playlist, TextBuilder textBuilder) {
            String tag = tag();
            playlist.variants().forEach(variant -> textBuilder.addTag(tag, variant, attributeMap)
                    .add(variant.uri())
                    .add("\n"));
        }
    },

    EXT_X_I_FRAME_STREAM_INF {
        private final Map<String, IFrameVariantAttribute> attributeMap = ParserUtils.toMap(IFrameVariantAttribute.values());

        @Override
        public void read(MasterPlaylist.Builder builder, String attributes, ParsingMode parsingMode) throws PlaylistParserException {
            builder.addIFrameVariants(ParserUtils.readAttributes(attributeMap, attributes, IFrameVariant.builder(), parsingMode));
        }

        @Override
        public void write(MasterPlaylist playlist, TextBuilder textBuilder) {
            textBuilder.addTag(tag(), playlist.iFrameVariants(), attributeMap);
        }
    },

    EXT_X_SESSION_DATA {
        private final Map<String, SessionDataAttribute> attributeMap = ParserUtils.toMap(SessionDataAttribute.values());

        @Override
        public void read(MasterPlaylist.Builder builder, String attributes, ParsingMode parsingMode) throws PlaylistParserException {
            builder.addSessionData(ParserUtils.readAttributes(attributeMap, attributes, SessionData.builder(), parsingMode));
        }

        @Override
        public void write(MasterPlaylist playlist, TextBuilder textBuilder) {
            textBuilder.addTag(tag(), playlist.sessionData(), attributeMap);
        }
    },

    EXT_X_SESSION_KEY {
        private final Map<String, SegmentKeyAttribute> attributeMap = ParserUtils.toMap(SegmentKeyAttribute.values());

        @Override
        public void read(MasterPlaylist.Builder builder, String attributes, ParsingMode parsingMode) throws PlaylistParserException {
            builder.addSessionKeys(ParserUtils.readAttributes(attributeMap, attributes, SegmentKey.builder(), parsingMode));
        }

        @Override
        public void write(MasterPlaylist playlist, TextBuilder textBuilder) {
            textBuilder.addTag(tag(), playlist.sessionKeys(), attributeMap);
        }
    }
}
