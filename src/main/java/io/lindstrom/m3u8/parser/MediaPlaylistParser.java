package io.lindstrom.m3u8.parser;

import io.lindstrom.m3u8.model.*;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Iterator;

import static io.lindstrom.m3u8.parser.Tags.*;

/**
 * MediaPlaylistParser can read and write Media Playlists according to RFC 8216 (HTTP Live Streaming).
 * <p>
 * Example usage:
 * <pre>
 * {@code
 * MediaPlaylistParser parser = new MediaPlaylistParser();
 *
 * // Parse playlist
 * MediaPlaylist playlist = parser.readPlaylist(Paths.get("path/to/media-playlist.m3u8"));
 *
 * // Update playlist version
 * MediaPlaylist updated = MediaPlaylist.builder()
 *                                      .from(playlist)
 *                                      .version(2)
 *                                      .build();
 *
 * // Write playlist to standard out
 * System.out.println(parser.writePlaylistAsString(updated));
 * }
 * </pre>
 *
 * This implementation is reusable and thread safe.
 */
public class MediaPlaylistParser extends AbstractPlaylistParser<MediaPlaylist, MediaPlaylistParser.Builder> {
    static final DateTimeFormatter FORMATTER = new DateTimeFormatterBuilder()
            .append(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
            .optionalStart().appendOffset("+HH:MM", "+00:00").optionalEnd()
            .optionalStart().appendOffset("+HHMM", "+0000").optionalEnd()
            .optionalStart().appendOffset("+HH", "Z").optionalEnd()
            .toFormatter();

    enum TagMappers implements Tag<MediaPlaylist, MediaPlaylist.Builder> {
        EXT_X_VERSION {
            @Override
            public void read(MediaPlaylist.Builder builder, String attributes, Iterator<String> lineIterator) {
                builder.version(Integer.parseInt(attributes));
            }

            @Override
            public void write(MediaPlaylist playlist, TextBuilder textBuilder) {
                // TODO
            }
        },

        EXT_X_INDEPENDENT_SEGMENTS {
            @Override
            public void read(MediaPlaylist.Builder builder, String attributes, Iterator<String> lineIterator) {
                builder.independentSegments(true);
            }

            @Override
            public void write(MediaPlaylist playlist, TextBuilder textBuilder) {
                // TODO
            }
        },

        EXT_X_PLAYLIST_TYPE {
            @Override
            public void read(MediaPlaylist.Builder builder, String attributes, Iterator<String> lineIterator) {
                builder.playlistType(PlaylistType.valueOf(attributes));
            }

            @Override
            public void write(MediaPlaylist playlist, TextBuilder textBuilder) {
                playlist.playlistType().ifPresent(value ->
                        textBuilder.add(tag()).add(":").add(value.toString()).add('\n'));
            }
        },

        EXT_X_I_FRAMES_ONLY {
            @Override
            public void read(MediaPlaylist.Builder builder, String attributes, Iterator<String> lineIterator) {
                builder.iFramesOnly(true);
            }

            @Override
            public void write(MediaPlaylist playlist, TextBuilder textBuilder) {
                if (playlist.iFramesOnly()) {
                    textBuilder.add(tag()).add("\n");
                }
            }
        },

        EXT_X_TARGETDURATION {
            @Override
            public void read(MediaPlaylist.Builder builder, String attributes, Iterator<String> lineIterator) {
                builder.targetDuration(Integer.parseInt(attributes));
            }

            @Override
            public void write(MediaPlaylist playlist, TextBuilder textBuilder) {
                textBuilder
                        .add(tag()).add(":")
                        .add(playlist.targetDuration()).add("\n");
            }
        },

        EXT_X_ENDLIST {
            @Override
            public void read(MediaPlaylist.Builder builder, String attributes, Iterator<String> lineIterator) {
                builder.ongoing(false);
            }

            @Override
            public void write(MediaPlaylist playlist, TextBuilder textBuilder) {
                // written elsewhere
            }
        },

        EXT_X_START {
            @Override
            public void read(MediaPlaylist.Builder builder, String attributes, Iterator<String> lineIterator) throws PlaylistParserException {
             //   builder.startTimeOffset(startTimeOffsetParser.parse(attributes));
            }

            @Override
            public void write(MediaPlaylist playlist, TextBuilder textBuilder) {
                // TODO
            }
        },

        EXT_X_DISCONTINUITY_SEQUENCE {
            @Override
            public void read(MediaPlaylist.Builder builder, String attributes, Iterator<String> lineIterator) {
                builder.discontinuitySequence(Long.parseLong(attributes));
            }

            @Override
            public void write(MediaPlaylist playlist, TextBuilder textBuilder) {
                if (playlist.discontinuitySequence() != 0) {
                    textBuilder.add(tag()).add(":").add(playlist.discontinuitySequence()).add("\n");
                }
            }
        },

        EXT_X_MEDIA_SEQUENCE {
            @Override
            public void read(MediaPlaylist.Builder builder, String attributes, Iterator<String> lineIterator) {
                builder.mediaSequence(Long.parseLong(attributes));

            }

            @Override
            public void write(MediaPlaylist playlist, TextBuilder textBuilder) {
                textBuilder.add(tag()).add(":").add(playlist.mediaSequence()).add("\n");
            }
        },

        EXT_X_ALLOW_CACHE {
            @Override
            public void read(MediaPlaylist.Builder builder, String attributes, Iterator<String> lineIterator) throws PlaylistParserException {
                builder.allowCache(ParserUtils.yesOrNo(attributes));
            }

            @Override
            public void write(MediaPlaylist playlist, TextBuilder textBuilder) {
                playlist.allowCache().ifPresent(value ->
                        textBuilder.add(tag()).add(":").add(value ? YES : NO).add("\n")
                );
            }
        }
    }

    enum MediaSegmentTags implements Tag<MediaSegment, MediaSegment.Builder> {
        EXT_X_BYTERANGE {
            private final ByteRangeParser byteRangeParser = new ByteRangeParser(); // TODO?

            @Override
            public void read(MediaSegment.Builder builder, String attributes, Iterator<String> lineIterator) throws PlaylistParserException {
                builder.byteRange(byteRangeParser.parse(attributes));
            }

            @Override
            public void write(MediaSegment mediaSegment, TextBuilder textBuilder) {
                mediaSegment.byteRange().ifPresent(byteRange -> byteRangeParser.write(byteRange, textBuilder.stringBuilder()));
            }
        },

        EXTINF {
            @Override
            public void read(MediaSegment.Builder builder, String attributes, Iterator<String> lineIterator) throws PlaylistParserException {
                String[] values = attributes.split(",", 2);
                builder.duration(Double.parseDouble(values[0]));
                if (values.length == 2 && !values[1].isEmpty()) {
                    builder.title(values[1]);
                }


            }

            @Override
            public void write(MediaSegment mediaSegment, TextBuilder textBuilder) {
                textBuilder.add(tag()).add(":").add(mediaSegment.duration()).add(",");
                mediaSegment.title().ifPresent(textBuilder::add);
                textBuilder.add('\n');
            }
        },

        EXT_X_PROGRAM_DATE_TIME {
            @Override
            public void read(MediaSegment.Builder builder, String attributes, Iterator<String> lineIterator) throws PlaylistParserException {
                builder.programDateTime(OffsetDateTime.parse(attributes, FORMATTER));
            }

            @Override
            public void write(MediaSegment mediaSegment, TextBuilder textBuilder) {
                mediaSegment.programDateTime().ifPresent(value -> textBuilder
                        .add(tag()).add(':')
                        .add(value)
                        .add('\n'));
            }
        },

        EXT_X_DATERANGE {
            @Override
            public void read(MediaSegment.Builder builder, String attributes, Iterator<String> lineIterator) throws PlaylistParserException {
                builder.dateRange(readAttributes(DateRangeParser.class, attributes, DateRange.builder()).build());
            }

            @Override
            public void write(MediaSegment mediaSegment, TextBuilder textBuilder) {
                mediaSegment.dateRange().ifPresent(value -> textBuilder.add(tag(), value, DateRangeParser.class));
            }
        },

        EXT_X_MAP {
            @Override
            public void read(MediaSegment.Builder builder, String attributes, Iterator<String> lineIterator) throws PlaylistParserException {
                builder.segmentMap(readAttributes(SegmentMapParser.class, attributes, SegmentMap.builder()).build());
            }

            @Override
            public void write(MediaSegment mediaSegment, TextBuilder textBuilder) {
                mediaSegment.segmentMap().ifPresent(map -> textBuilder.add(tag(), map, SegmentMapParser.class));
            }
        },

        EXT_X_KEY {
            @Override
            public void read(MediaSegment.Builder builder, String attributes, Iterator<String> lineIterator) throws PlaylistParserException {
                builder.segmentKey(readAttributes(SegmentKeyParser.class, attributes, SegmentKey.builder()).build());
            }

            @Override
            public void write(MediaSegment mediaSegment, TextBuilder textBuilder) {
                mediaSegment.segmentKey().ifPresent(key -> textBuilder.add(tag(), key, SegmentKeyParser.class));
            }
        },

        EXT_X_DISCONTINUITY {
            @Override
            public void read(MediaSegment.Builder builder, String attributes, Iterator<String> lineIterator) {
                builder.discontinuity(true);
            }

            @Override
            public void write(MediaSegment value, TextBuilder textBuilder) {
                if (value.discontinuity()) {
                    textBuilder.add(tag()).add('\n');
                }
            }
        }

    }

    @Override
    Builder newBuilder() {
        return new Builder();
    }

    @Override
    void onTag(Builder builderWrapper, String prefix, String attributes, Iterator<String> lineIterator) throws PlaylistParserException {
        String name = prefix.substring(1).replace("-", "_"); // TODO FIXME

        for (TagMappers tag : TagMappers.values()) {
            if (tag.name().equals(name)) {
                tag.read(builderWrapper.playlistBuilder, attributes, lineIterator);
                return;
            }
        }

        for (MediaSegmentTags tag : MediaSegmentTags.values()) {
            if (tag.name().equals(name)) {
                tag.read(builderWrapper.segmentBuilder, attributes, lineIterator);
                return;
            }
        }

        // unknown tag
        throw new PlaylistParserException("Tag not implemented: " + prefix);
    }

    @Override
    void onURI(Builder builderWrapper, String uri) {
        builderWrapper.segmentBuilder.uri(uri);
        builderWrapper.playlistBuilder.addMediaSegments(builderWrapper.segmentBuilder.build());
        builderWrapper.segmentBuilder = MediaSegment.builder();
    }

    @Override
    MediaPlaylist build(Builder builderWrapper) {
        return builderWrapper.playlistBuilder.build();
    }

    @Override
    void write(MediaPlaylist playlist, TextBuilder textBuilder) {
        for (TagMappers mapper : TagMappers.values()) {
            mapper.write(playlist, textBuilder);
        }

        playlist.mediaSegments().forEach(mediaSegment -> {
            for (MediaSegmentTags mapper : MediaSegmentTags.values()) {
                mapper.write(mediaSegment, textBuilder);
            }
            textBuilder.add(mediaSegment.uri()).add('\n');
        });

        if (!playlist.ongoing()) {
            textBuilder.add(EXT_X_ENDLIST).add('\n');
        }
    }

    /**
     * Wrapper class for playlist and segment builders
     */
    static class Builder {
        private final MediaPlaylist.Builder playlistBuilder = MediaPlaylist.builder();
        private MediaSegment.Builder segmentBuilder = MediaSegment.builder();
    }
}
