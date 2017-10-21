package io.lindstrom.m3u8;

import io.lindstrom.m3u8.model.MasterPlaylist;
import io.lindstrom.m3u8.parser.AlternativeRenditionParser;
import io.lindstrom.m3u8.parser.IFramePlaylistParser;
import io.lindstrom.m3u8.parser.VariantStreamParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static io.lindstrom.m3u8.Tags.*;

public class MasterPlaylistParser {

    private static final VariantStreamParser VARIANT_STREAM_PARSER = new VariantStreamParser();
    private static final IFramePlaylistParser I_FRAME_PLAYLIST_PARSER = new IFramePlaylistParser();
    private static final AlternativeRenditionParser ALTERNATIVE_RENDITION_PARSER = new AlternativeRenditionParser();

    public static MasterPlaylist parse(InputStream inputStream) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            return parse(reader);
        }
    }

    private static MasterPlaylist parse(BufferedReader reader) throws IOException {
        MasterPlaylist.Builder builder = MasterPlaylist.builder();

        String line = reader.readLine();
        if (!Tags.EXTM3U.equals(line)) {
            throw new IOException("nope");
        }
        line = reader.readLine();
        if (line == null || !line.startsWith(Tags.EXT_X_VERSION)) {
            throw new IOException("Nope");
        }
        builder.version(Integer.parseInt(line.substring(Tags.EXT_X_VERSION.length())));

        while ((line = reader.readLine()) != null) {
            if (line.isEmpty()) {
                continue; // ignore blank lines
            }

            if (line.startsWith("#") && !line.startsWith("#EXT")) {
                continue; // ignore comments
            }

            int colonPosition = line.indexOf(':');
            String prefix = line.substring(0, colonPosition > 0 ? colonPosition : line.length());
            String attributeString = colonPosition > 0 ? line.substring(colonPosition + 1) : null;
            Map<String, String> attributes = attributeString != null ? parseAttributes(attributeString) : Collections.emptyMap();

            switch (prefix) {
                case EXT_X_MEDIA:
                    builder.addAlternativeRenditions(ALTERNATIVE_RENDITION_PARSER.parse(attributes));
                    break;
                case EXT_X_STREAM_INF:
                    String uriLine = reader.readLine();
                    if (uriLine == null || uriLine.startsWith("#")) {
                        throw new RuntimeException("Expected URI, got " + uriLine);
                    }
                    attributes.put(Tags.URI, uriLine);
                    builder.addVariantStreams(VARIANT_STREAM_PARSER.parse(attributes));
                    break;
                case EXT_X_I_FRAME_STREAM_INF:
                    builder.addIFramePlaylists(I_FRAME_PLAYLIST_PARSER.parse(attributes));
                    break;
                case EXT_X_INDEPENDENT_SEGMENTS:
                    builder.independentSegments(true);
                    break;
                default:
                    throw new IOException("Invalid line: " + line);
            }
        }

        return builder.build();
    }

    static Map<String, String> parseAttributes(String attributeList) {
        Pattern pattern = Pattern.compile("([A-Z0-9\\-]+)=(?:(?:\"([^\"]+)\")|([^,]+))");
        Matcher matcher = pattern.matcher(attributeList);
        Map<String, String> attributes = new HashMap<>();
        while (matcher.find()) {
            attributes.put(matcher.group(1), matcher.group(2) != null ? matcher.group(2) : matcher.group(3));
        }
        return attributes;
    }

    public static String writePlaylist(MasterPlaylist masterPlaylist) {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(Tags.EXTM3U).append('\n');
        stringBuilder.append(Tags.EXT_X_VERSION).append(masterPlaylist.version()).append('\n');
        if (masterPlaylist.independentSegments()) {
            stringBuilder.append(Tags.EXT_X_INDEPENDENT_SEGMENTS).append('\n');
        }

        masterPlaylist.alternativeRenditions().stream()
                .map(ALTERNATIVE_RENDITION_PARSER::write)
                .forEach(stringBuilder::append);

        masterPlaylist.variantStreams().stream()
                .map(VARIANT_STREAM_PARSER::write)
                .forEach(stringBuilder::append);

        masterPlaylist.iFramePlaylists().stream()
                .map(I_FRAME_PLAYLIST_PARSER::write)
                .forEach(stringBuilder::append);

        return stringBuilder.toString();
    }
}
