package io.lindstrom.m3u8.parser;

import io.lindstrom.m3u8.model.MediaPlaylist;
import io.lindstrom.m3u8.model.PlaylistType;
import io.lindstrom.m3u8.model.ServerControl;
import io.lindstrom.m3u8.model.StartTimeOffset;

import java.util.Map;

import static io.lindstrom.m3u8.parser.ParserUtils.NO;
import static io.lindstrom.m3u8.parser.ParserUtils.YES;

enum MediaPlaylistTag implements Tag<MediaPlaylist, MediaPlaylist.Builder> {
    EXT_X_VERSION {
        @Override
        public void read(MediaPlaylist.Builder builder, String attributes, ParsingMode parsingMode) {
            builder.version(Integer.parseInt(attributes));
        }

        @Override
        public void write(MediaPlaylist playlist, TextBuilder textBuilder) {
            playlist.version().ifPresent(version -> textBuilder.addTag(tag(), version));
        }
    },

    EXT_X_INDEPENDENT_SEGMENTS {
        @Override
        public void read(MediaPlaylist.Builder builder, String attributes, ParsingMode parsingMode) {
            builder.independentSegments(true);
        }

        @Override
        public void write(MediaPlaylist playlist, TextBuilder textBuilder) {
            if (playlist.independentSegments()) {
                textBuilder.addTag(tag());
            }
        }
    },

    EXT_X_START {
        private final Map<String, StartTimeOffsetAttribute> attributeMap = ParserUtils.toMap(StartTimeOffsetAttribute.values());

        @Override
        public void read(MediaPlaylist.Builder builder, String attributes, ParsingMode parsingMode) throws PlaylistParserException {
            builder.startTimeOffset(ParserUtils.readAttributes(attributeMap, attributes, StartTimeOffset.builder(), parsingMode));
        }

        @Override
        public void write(MediaPlaylist playlist, TextBuilder textBuilder) {
            playlist.startTimeOffset().ifPresent(value -> textBuilder.addTag(tag(), value, attributeMap));
        }
    },

    EXT_X_I_FRAMES_ONLY {
        @Override
        public void read(MediaPlaylist.Builder builder, String attributes, ParsingMode parsingMode) {
            builder.iFramesOnly(true);
        }

        @Override
        public void write(MediaPlaylist playlist, TextBuilder textBuilder) {
            if (playlist.iFramesOnly()) {
                textBuilder.addTag(tag());
            }
        }
    },

    EXT_X_SERVER_CONTROL {
        private final Map<String, ServerControlAttribute> attributeMap = ParserUtils.toMap(ServerControlAttribute.values());

        @Override
        public void read(MediaPlaylist.Builder builder, String attributes, ParsingMode parsingMode) throws PlaylistParserException {
            builder.serverControl(ParserUtils.readAttributes(attributeMap, attributes, ServerControl.builder(), parsingMode));
        }

        @Override
        public void write(MediaPlaylist playlist, TextBuilder textBuilder) {
            playlist.serverControl().ifPresent(v -> textBuilder.addTag(tag(), v, attributeMap));
        }
    },

    EXT_X_ALLOW_CACHE {
        @Override
        public void read(MediaPlaylist.Builder builder, String attributes, ParsingMode parsingMode) throws PlaylistParserException {
            builder.allowCache(ParserUtils.yesOrNo(attributes));
        }

        @Override
        public void write(MediaPlaylist playlist, TextBuilder textBuilder) {
            playlist.allowCache().ifPresent(value -> textBuilder.addTag(tag(), value ? YES : NO));
        }
    },

    EXT_X_PLAYLIST_TYPE {
        @Override
        public void read(MediaPlaylist.Builder builder, String attributes, ParsingMode parsingMode) {
            builder.playlistType(PlaylistType.valueOf(attributes));
        }

        @Override
        public void write(MediaPlaylist playlist, TextBuilder textBuilder) {
            playlist.playlistType().ifPresent(value -> textBuilder.addTag(tag(), value.toString()));
        }
    },

    EXT_X_TARGETDURATION {
        @Override
        public void read(MediaPlaylist.Builder builder, String attributes, ParsingMode parsingMode) {
            builder.targetDuration(Integer.parseInt(attributes));
        }

        @Override
        public void write(MediaPlaylist playlist, TextBuilder textBuilder) {
            textBuilder.addTag(tag(), playlist.targetDuration());
        }
    },

    EXT_X_MEDIA_SEQUENCE {
        @Override
        public void read(MediaPlaylist.Builder builder, String attributes, ParsingMode parsingMode) {
            builder.mediaSequence(Long.parseLong(attributes));

        }

        @Override
        public void write(MediaPlaylist playlist, TextBuilder textBuilder) {
            textBuilder.addTag(tag(), playlist.mediaSequence());
        }
    },

    EXT_X_DISCONTINUITY_SEQUENCE {
        @Override
        public void read(MediaPlaylist.Builder builder, String attributes, ParsingMode parsingMode) {
            builder.discontinuitySequence(Long.parseLong(attributes));
        }

        @Override
        public void write(MediaPlaylist playlist, TextBuilder textBuilder) {
            if (playlist.discontinuitySequence() != 0) {
                textBuilder.addTag(tag(), playlist.discontinuitySequence());
            }
        }
    },

    EXT_X_ENDLIST {
        @Override
        public void read(MediaPlaylist.Builder builder, String attributes, ParsingMode parsingMode) {
            builder.ongoing(false);
        }

        @Override
        public void write(MediaPlaylist playlist, TextBuilder textBuilder) {
            // written elsewhere
        }
    }
}
