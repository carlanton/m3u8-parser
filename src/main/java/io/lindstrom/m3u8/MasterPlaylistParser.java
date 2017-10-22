package io.lindstrom.m3u8;

import io.lindstrom.m3u8.model.MasterPlaylist;
import io.lindstrom.m3u8.parser.AlternativeRenditionParser;
import io.lindstrom.m3u8.parser.IFramePlaylistParser;
import io.lindstrom.m3u8.parser.VariantStreamParser;

import java.util.Collections;
import java.util.function.Supplier;

import static io.lindstrom.m3u8.Tags.*;

public class MasterPlaylistParser extends AbstractPlaylistParser<MasterPlaylist, MasterPlaylist.Builder> {

    private final VariantStreamParser variantStreamParser = new VariantStreamParser();
    private final IFramePlaylistParser iFramePlaylistParser = new IFramePlaylistParser();
    private final AlternativeRenditionParser alternativeRenditionParser = new AlternativeRenditionParser();

    @Override
    protected MasterPlaylist.Builder newBuilder() {
        return MasterPlaylist.builder();
    }

    @Override
    protected void onTag(MasterPlaylist.Builder builder, String prefix, String attributes, Supplier<String> nextLine) {
        switch (prefix) {
            case EXT_X_VERSION:
                builder.version(Integer.parseInt(attributes));
                break;

            case EXT_X_MEDIA:
                builder.addAlternativeRenditions(alternativeRenditionParser.parse(attributes));
                break;
            case EXT_X_STREAM_INF:
                String uriLine = nextLine.get();
                if (uriLine == null || uriLine.startsWith("#")) {
                    throw new RuntimeException("Expected URI, got " + uriLine);
                }
                builder.addVariantStreams(variantStreamParser.parse(attributes,
                        Collections.singletonMap(URI, uriLine)));
                break;
            case EXT_X_I_FRAME_STREAM_INF:
                builder.addIFramePlaylists(iFramePlaylistParser.parse(attributes));
                break;
            case EXT_X_INDEPENDENT_SEGMENTS:
                builder.independentSegments(true);
                break;

            case EXT_X_SESSION_DATA:
            case EXT_X_SESSION_KEY:
            case EXT_X_START:
                throw new UnsupportedOperationException("Tag not implemented: " + prefix);

            default:
                throw new RuntimeException("Invalid line: " + prefix);
        }
    }

    @Override
    protected void onURI(MasterPlaylist.Builder builder, String uri) {
        throw new IllegalStateException("Unexpected URI in master playlist");
    }

    @Override
    protected MasterPlaylist build(MasterPlaylist.Builder builder) {
        return builder.build();
    }

    @Override
    protected void write(MasterPlaylist playlist, StringBuilder stringBuilder) {
        playlist.alternativeRenditions()
                .forEach(value -> alternativeRenditionParser.write(value, stringBuilder));

        playlist.variantStreams()
                .forEach(value -> variantStreamParser.write(value, stringBuilder));

        playlist.iFramePlaylists()
                .forEach(value -> iFramePlaylistParser.write(value, stringBuilder));
    }
}
