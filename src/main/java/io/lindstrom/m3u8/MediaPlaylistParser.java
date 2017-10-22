package io.lindstrom.m3u8;

import io.lindstrom.m3u8.model.MediaPlaylist;
import io.lindstrom.m3u8.model.MediaSegment;
import io.lindstrom.m3u8.model.PlaylistType;
import io.lindstrom.m3u8.parser.ByteRangeParser;
import io.lindstrom.m3u8.parser.MapInfoParser;

import java.time.OffsetDateTime;
import java.util.function.Supplier;

import static io.lindstrom.m3u8.Tags.*;

public class MediaPlaylistParser extends AbstractPlaylistParser<MediaPlaylist, MediaPlaylistParser.Builder> {
    private final MapInfoParser mapInfoParser = new MapInfoParser();
    private final ByteRangeParser byteRangeParser = new ByteRangeParser();

    protected static class Builder {
        private final MediaPlaylist.Builder playlistBuilder = MediaPlaylist.builder();
        private MediaSegment.Builder segmentBuilder = MediaSegment.builder();
    }

    @Override
    protected Builder newBuilder() {
        return new Builder();
    }

    @Override
    protected void onTag(Builder builderWrapper, String prefix, String attributes, Supplier<String> nextLine) {
        MediaPlaylist.Builder builder = builderWrapper.playlistBuilder;
        MediaSegment.Builder mediaSegmentBuilder = builderWrapper.segmentBuilder;

        switch (prefix) {
            case EXT_X_VERSION:
                builder.version(Integer.parseInt(attributes));
                break;

            case EXT_X_INDEPENDENT_SEGMENTS:
                builder.independentSegments(true);
                break;

            case EXT_X_PLAYLIST_TYPE:
                builder.playlistType(PlaylistType.valueOf(attributes));
                break;

            case EXT_X_I_FRAMES_ONLY:
                builder.iFramesOnly(true);
                break;

            case EXT_X_BYTERANGE:
                mediaSegmentBuilder.byteRange(byteRangeParser.parse(attributes));
                break;

            case EXT_X_TARGETDURATION:
                builder.targetDuration(Integer.parseInt(attributes));
                break;

            case EXTINF:
                String[] values = attributes.split(",", 2);
                mediaSegmentBuilder.duration(Double.parseDouble(values[0]));
                if (values.length == 2) {
                    mediaSegmentBuilder.title(values[1]);
                }
                break;

            case EXT_X_PROGRAM_DATE_TIME:
                mediaSegmentBuilder.programDateTime(OffsetDateTime.parse(attributes));
                break;

            case EXT_X_MAP:
                mediaSegmentBuilder.mapInfo(mapInfoParser.parse(attributes));
                break;

            case EXT_X_ENDLIST:
                builder.ongoing(false);
                break;

            case EXT_X_MEDIA_SEQUENCE:
                builder.mediaSequence(Integer.parseInt(attributes));
                break;

            case EXT_X_DISCONTINUITY_SEQUENCE:
            case EXT_X_DISCONTINUITY:
            case EXT_X_KEY:
            case EXT_X_DATERANGE:
            case EXT_X_START:
            default:
                throw new UnsupportedOperationException("Tag not implemented: " + prefix);
        }
    }

    @Override
    protected void onURI(Builder builderWrapper, String uri) {
        builderWrapper.segmentBuilder.uri(uri);
        builderWrapper.playlistBuilder.addMediaSegments(builderWrapper.segmentBuilder.build());
        builderWrapper.segmentBuilder = MediaSegment.builder();
    }

    @Override
    protected MediaPlaylist build(Builder builderWrapper) {
        return builderWrapper.playlistBuilder.build();
    }

    @Override
    protected void write(MediaPlaylist playlist, StringBuilder stringBuilder) {
        if (playlist.iFramesOnly()) {
            stringBuilder.append(EXT_X_I_FRAMES_ONLY).append("\n");
        }

        playlist.playlistType().ifPresent(value ->
                stringBuilder.append(EXT_X_PLAYLIST_TYPE).append(":")
                        .append(value.toString()).append('\n'));

        stringBuilder.append(String.format("%s:%d\n", EXT_X_TARGETDURATION, playlist.targetDuration()));
        stringBuilder.append(String.format("%s:%d\n", EXT_X_MEDIA_SEQUENCE, playlist.mediaSequence()));

        playlist.mediaSegments().forEach(mediaSegment ->
                writeMediaSegment(mediaSegment, stringBuilder));

        if (!playlist.ongoing()) {
            stringBuilder.append(EXT_X_ENDLIST).append('\n');
        }
    }

    private void writeMediaSegment(MediaSegment mediaSegment, StringBuilder stringBuilder) {

        // EXT-X-PROGRAM-DATE-TIME
        mediaSegment.programDateTime().ifPresent(value -> stringBuilder
                .append(EXT_X_PROGRAM_DATE_TIME).append(':')
                .append(value)
                .append('\n'));

        // EXT-X-MAP
        mediaSegment.mapInfo().ifPresent(mapInfo -> mapInfoParser.write(mapInfo, stringBuilder));

        // EXTINF
        stringBuilder.append(String.format("%s:%.5f,", EXTINF, mediaSegment.duration()));
        mediaSegment.title().ifPresent(stringBuilder::append);
        stringBuilder.append('\n');

        // EXT-X-BYTERANGE
        mediaSegment.byteRange().ifPresent(byteRange -> byteRangeParser.write(byteRange, stringBuilder));

        // <URI>
        stringBuilder.append(mediaSegment.uri()).append('\n');
    }
}
