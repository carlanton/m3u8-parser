package io.lindstrom.m3u8.parser;

import io.lindstrom.m3u8.model.Playlist;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;

import static io.lindstrom.m3u8.parser.Tags.*;
import static java.nio.charset.StandardCharsets.UTF_8;

public abstract class AbstractPlaylistParser<T extends Playlist, B> {
    final StartTimeOffsetParser startTimeOffsetParser = new StartTimeOffsetParser();

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
                onURI(builder, line);
            }
        }

        return build(builder);
    }

    abstract B newBuilder();

    abstract void onTag(B builder, String prefix, String attributes, Iterator<String> lineIterator) throws PlaylistParserException;

    abstract void onURI(B builder, String uri) throws PlaylistParserException;

    abstract T build(B builder);

    abstract void write(T playlist, StringBuilder stringBuilder);

    public String writePlaylistAsString(T playlist) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(EXTM3U).append('\n');
        playlist.version().ifPresent(version -> stringBuilder.append(EXT_X_VERSION)
                .append(":").append(version).append('\n'));
        if (playlist.independentSegments()) {
            stringBuilder.append(EXT_X_INDEPENDENT_SEGMENTS).append('\n');
        }

        playlist.startTimeOffset().ifPresent(value -> startTimeOffsetParser.write(value, stringBuilder));

        write(playlist, stringBuilder);
        return stringBuilder.toString();
    }

    public byte[] writePlaylistAsBytes(T playlist) {
        return writePlaylistAsString(playlist).getBytes(UTF_8);
    }

    public ByteBuffer writePlaylistAsByteBuffer(T playlist) {
        return ByteBuffer.wrap(writePlaylistAsBytes(playlist));
    }
}
