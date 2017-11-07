package io.lindstrom.m3u8;

import io.lindstrom.m3u8.model.Playlist;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Supplier;

import static io.lindstrom.m3u8.Tags.*;

abstract class AbstractPlaylistParser<T extends Playlist, B> {
    public T readPlaylist(InputStream inputStream) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            return parse(reader);
        }
    }

    public T readPlaylist(Path path) throws IOException {
        try (BufferedReader reader = Files.newBufferedReader(path)) {
            return parse(reader);
        }
    }

    private T parse(BufferedReader reader) throws IOException {
        if (!Tags.EXTM3U.equals(reader.readLine())) {
            throw new IOException("Invalid playlist. Expected #EXTM3U on first line.");
        }

        B builder = newBuilder();

        String line;

        Supplier<String> lineSupplier = () -> {
            try {
                return reader.readLine();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        };

        while ((line = reader.readLine()) != null) {
            if (line.isEmpty()) {
                continue; // ignore blank lines
            }

            if (line.startsWith("#EXT")) {
                int colonPosition = line.indexOf(':');
                String prefix = colonPosition > 0 ? line.substring(0, colonPosition) : line;
                String attributes = colonPosition > 0 ? line.substring(colonPosition + 1) : "";

                onTag(builder, prefix, attributes, lineSupplier);
            }

            if (line.startsWith("#")) {
                continue; // ignore comments
            }

            onURI(builder, line);
        }

        return build(builder);
    }

    abstract B newBuilder();

    abstract void onTag(B builder, String prefix, String attributes, Supplier<String> nextLine);

    abstract void onURI(B builder, String uri);

    abstract T build(B builder);

    abstract void write(T playlist, StringBuilder stringBuilder);

    public String writePlaylistAsString(T playlist) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(EXTM3U).append('\n');
        stringBuilder.append(EXT_X_VERSION).append(":").append(playlist.version()).append('\n');
        if (playlist.independentSegments()) {
            stringBuilder.append(EXT_X_INDEPENDENT_SEGMENTS).append('\n');
        }

        write(playlist, stringBuilder);
        return stringBuilder.toString();
    }

    public byte[] writePlaylistAsBytes(T playlist) {
        return writePlaylistAsString(playlist).getBytes(StandardCharsets.UTF_8);
    }

    public ByteBuffer writePlaylistAsByteBuffer(T playlist) {
        return ByteBuffer.wrap(writePlaylistAsBytes(playlist));
    }
}
