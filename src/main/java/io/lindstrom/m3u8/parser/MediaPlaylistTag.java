package io.lindstrom.m3u8.parser;

import io.lindstrom.m3u8.model.MediaPlaylist;
import io.lindstrom.m3u8.model.PlaylistType;

import static io.lindstrom.m3u8.parser.ParserUtils.NO;
import static io.lindstrom.m3u8.parser.ParserUtils.YES;

enum MediaPlaylistTag implements Tag<MediaPlaylist, MediaPlaylist.Builder> {
    EXT_X_VERSION {
        @Override
        public void read(MediaPlaylist.Builder builder, String attributes) {
            builder.version(Integer.parseInt(attributes));
        }

        @Override
        public void write(MediaPlaylist playlist, TextBuilder textBuilder) {
            playlist.version().ifPresent(version -> textBuilder.addTag(tag(), version));
        }
    },

    EXT_X_INDEPENDENT_SEGMENTS {
        @Override
        public void read(MediaPlaylist.Builder builder, String attributes) {
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
        @Override
        public void read(MediaPlaylist.Builder builder, String attributes) throws PlaylistParserException {
            builder.startTimeOffset(StartTimeOffsetAttribute.parse(attributes));
        }

        @Override
        public void write(MediaPlaylist playlist, TextBuilder textBuilder) {
            playlist.startTimeOffset().ifPresent(value -> textBuilder.addTag(tag(), value, StartTimeOffsetAttribute.class));
        }
    },

    EXT_X_I_FRAMES_ONLY {
        @Override
        public void read(MediaPlaylist.Builder builder, String attributes) {
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
        @Override
        public void read(MediaPlaylist.Builder builder, String attributes) throws PlaylistParserException {
            builder.serverControl(ServerControlAttribute.parse(attributes));
        }

        @Override
        public void write(MediaPlaylist playlist, TextBuilder textBuilder) {
            playlist.serverControl().ifPresent(v -> textBuilder.addTag(tag(), v, ServerControlAttribute.class));
        }
    },

    EXT_X_ALLOW_CACHE {
        @Override
        public void read(MediaPlaylist.Builder builder, String attributes) throws PlaylistParserException {
            builder.allowCache(ParserUtils.yesOrNo(attributes));
        }

        @Override
        public void write(MediaPlaylist playlist, TextBuilder textBuilder) {
            playlist.allowCache().ifPresent(value -> textBuilder.addTag(tag(), value ? YES : NO));
        }
    },

    EXT_X_PLAYLIST_TYPE {
        @Override
        public void read(MediaPlaylist.Builder builder, String attributes) {
            builder.playlistType(PlaylistType.valueOf(attributes));
        }

        @Override
        public void write(MediaPlaylist playlist, TextBuilder textBuilder) {
            playlist.playlistType().ifPresent(value -> textBuilder.addTag(tag(), value.toString()));
        }
    },

    EXT_X_TARGETDURATION {
        @Override
        public void read(MediaPlaylist.Builder builder, String attributes) {
            builder.targetDuration(Integer.parseInt(attributes));
        }

        @Override
        public void write(MediaPlaylist playlist, TextBuilder textBuilder) {
            textBuilder.addTag(tag(), playlist.targetDuration());
        }
    },

    EXT_X_MEDIA_SEQUENCE {
        @Override
        public void read(MediaPlaylist.Builder builder, String attributes) {
            builder.mediaSequence(Long.parseLong(attributes));

        }

        @Override
        public void write(MediaPlaylist playlist, TextBuilder textBuilder) {
            textBuilder.addTag(tag(), playlist.mediaSequence());
        }
    },

    EXT_X_DISCONTINUITY_SEQUENCE {
        @Override
        public void read(MediaPlaylist.Builder builder, String attributes) {
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
        public void read(MediaPlaylist.Builder builder, String attributes) {
            builder.ongoing(false);
        }

        @Override
        public void write(MediaPlaylist playlist, TextBuilder textBuilder) {
            // written elsewhere
        }
    }
}
