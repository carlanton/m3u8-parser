package io.lindstrom.m3u8.parser;

import io.lindstrom.m3u8.model.Playlist;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static io.lindstrom.m3u8.parser.Tags.*;
import static java.nio.charset.StandardCharsets.UTF_8;

public abstract class AbstractPlaylistParser<T extends Playlist, B> {
    static final Pattern ATTRIBUTE_LIST_PATTERN = Pattern.compile("([A-Z0-9\\-]+)=(?:(?:\"([^\"]+)\")|([^,]+))");

    public T readPlaylist(InputStream inputStream) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, UTF_8))) {
            return readPlaylist(reader);
        }
    }

    public T readPlaylist(Path path) throws IOException {
        try (BufferedReader reader = Files.newBufferedReader(path, UTF_8)) {
            return readPlaylist(reader);
        }
    }

    public T readPlaylist(BufferedReader bufferedReader) throws PlaylistParserException {
        Iterator<String> lineIterator = new Iterator<String>() {
            String nextLine = null;

            @Override
            public boolean hasNext() {
                if (nextLine != null) {
                    return true;
                } else {
                    try {
                        nextLine = bufferedReader.readLine();
                        return (nextLine != null);
                    } catch (IOException e) {
                        throw new UncheckedIOException(e);
                    }
                }
            }

            @Override
            public String next() {
                if (nextLine != null || hasNext()) {
                    String line = nextLine;
                    nextLine = null;
                    return line;
                } else {
                    throw new NoSuchElementException();
                }
            }
        };

        return readPlaylist(lineIterator);
    }

    public T readPlaylist(String string) throws PlaylistParserException {
        return readPlaylist(Arrays.asList(string.split("\n")).iterator());
    }

    public T readPlaylist(Iterator<String> lineIterator) throws PlaylistParserException {
        // All playlists must start with the tag #EXTM3U. According to the RFC this must be on the first line,
        // but some examples allow empty lines before the tag.
        boolean extM3uFound = false;
        while (lineIterator.hasNext() && !extM3uFound) {
            String line = lineIterator.next();
            if (Tags.EXTM3U.equals(line)) {
                extM3uFound = true;
            } else if (!line.isEmpty()) {
                break; // invalid line  found
            }
            // else: line is empty
        }
        if (!extM3uFound) {
            throw new PlaylistParserException("Invalid playlist. Expected #EXTM3U.");
        }

        final B builder = newBuilder();

        while (lineIterator.hasNext()) {
            String line = lineIterator.next();

            if (line.startsWith("#EXT")) {
                int colonPosition = line.indexOf(':');
                String prefix = colonPosition > 0 ? line.substring(0, colonPosition) : line;
                String attributes = colonPosition > 0 ? line.substring(colonPosition + 1) : "";

                onTag(builder, prefix, attributes, lineIterator);
            } else if (!(line.startsWith("#") || line.isEmpty())) {
                onURI(builder, line); // <-- TODO silly?
            }
        }

        return build(builder);
    }

    abstract B newBuilder();

     abstract void onTag(B builder, String prefix, String attributes, Iterator<String> lineIterator) throws PlaylistParserException;

     void onURI(B builder, String uri) throws PlaylistParserException {
         throw new PlaylistParserException("Unexpected URI in playlist: " + uri);
     }

    abstract T build(B builder);

    abstract void write(T playlist, TextBuilder textBuilder);

    public String writePlaylistAsString(T playlist) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(EXTM3U).append('\n');
        playlist.version().ifPresent(version -> stringBuilder.append(EXT_X_VERSION)
                .append(":").append(version).append('\n'));
        if (playlist.independentSegments()) {
            stringBuilder.append(EXT_X_INDEPENDENT_SEGMENTS).append('\n');
        }

        TextBuilder textBuilder = new TextBuilder(stringBuilder);

        playlist.startTimeOffset().ifPresent(value -> textBuilder.add(EXT_X_START, value, StartTimeOffsetParser.class));


        write(playlist, textBuilder);
        return stringBuilder.toString();
    }

    public byte[] writePlaylistAsBytes(T playlist) {
        return writePlaylistAsString(playlist).getBytes(UTF_8);
    }

    public ByteBuffer writePlaylistAsByteBuffer(T playlist) {
        return ByteBuffer.wrap(writePlaylistAsBytes(playlist));
    }


    static <X, Y, Z extends Enum<Z> & AttributeMapper<X, Y>> Y readAttributes(Class<Z> mapperClass,
                                                                              String attributes,
                                                                              Y builder) throws PlaylistParserException {
        Matcher matcher = ATTRIBUTE_LIST_PATTERN.matcher(attributes);

        while (matcher.find()) {
            String attribute = matcher.group(1);
            String value = matcher.group(2) != null ? matcher.group(2) : matcher.group(3);

            try {
                if (attribute.startsWith("X-")) {
                    Enum.valueOf(mapperClass, "CUSTOM").read(builder, attribute, value);
                } else {
                    Enum.valueOf(mapperClass, attribute.replace("-", "_")).read(builder, value);
                }
            } catch (IllegalArgumentException e) {
                throw new PlaylistParserException("Unknown attribute: " + attribute);
            }
        }

        return builder;
    }

}
