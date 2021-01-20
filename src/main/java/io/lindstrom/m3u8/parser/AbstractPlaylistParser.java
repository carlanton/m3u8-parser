package io.lindstrom.m3u8.parser;

import io.lindstrom.m3u8.model.Playlist;
import io.lindstrom.m3u8.model.IBuilder;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

import static java.nio.charset.StandardCharsets.UTF_8;

public abstract class AbstractPlaylistParser<T extends Playlist, B extends IBuilder<T>> {
    private static final String EXTM3U = "#EXTM3U";

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
            if (EXTM3U.equals(line)) {
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
                String prefix = colonPosition > 0 ? line.substring(1, colonPosition) : line.substring(1);
                String attributes = colonPosition > 0 ? line.substring(colonPosition + 1) : "";

                onTag(builder, prefix, attributes, lineIterator);
            } else if (!(line.startsWith("#") || line.isEmpty())) {
                onURI(builder, line); // <-- TODO silly?
            }
        }

        return builder.build();
    }

    abstract B newBuilder();

    abstract void onTag(B builder, String prefix, String attributes, Iterator<String> lineIterator) throws PlaylistParserException;

    void onURI(B builder, String uri) throws PlaylistParserException {
        throw new PlaylistParserException("Unexpected URI in playlist: " + uri);
    }

    abstract void write(T playlist, TextBuilder textBuilder);

    public String writePlaylistAsString(T playlist) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(EXTM3U).append('\n');

        TextBuilder textBuilder = new TextBuilder(stringBuilder);
        write(playlist, textBuilder);
        return stringBuilder.toString();
    }

    public byte[] writePlaylistAsBytes(T playlist) {
        return writePlaylistAsString(playlist).getBytes(UTF_8);
    }

    public ByteBuffer writePlaylistAsByteBuffer(T playlist) {
        return ByteBuffer.wrap(writePlaylistAsBytes(playlist));
    }

}
