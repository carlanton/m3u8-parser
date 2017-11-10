package io.lindstrom.m3u8;

import io.lindstrom.m3u8.model.MasterPlaylist;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;
import static org.junit.runners.Parameterized.Parameter;

@RunWith(Parameterized.class)
public class MasterPlaylistReadAndWrite {
    private final MasterPlaylistParser masterPlaylistParser = new MasterPlaylistParser();

    @Parameters
    public static List<Path> data() throws IOException {
        Path resources = Paths.get("src/test/resources/");
        List<Path> media = Files.list(resources.resolve("master")).collect(Collectors.toList());

        media.addAll(Stream.of(
                "masterPlaylist.m3u8",
                "masterPlaylistWithAlternativeAudio.m3u8",
                "masterPlaylistWithAlternativeVideo.m3u8",
                "masterPlaylistWithIFrames.m3u8")
                .map(resources.resolve("open-m3u8")::resolve)
                .collect(Collectors.toList()));

        return media;
    }

    @Parameter
    public Path playlistPath;

    @Test
    public void readAndWrite() throws Exception {
        MasterPlaylist playlist = masterPlaylistParser.readPlaylist(playlistPath);
        assertEquals(playlist, masterPlaylistParser.readPlaylist(masterPlaylistParser.writePlaylistAsString(playlist)));
    }
}
