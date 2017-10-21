package io.lindstrom.m3u8;

import io.lindstrom.m3u8.model.MasterPlaylist;
import org.junit.Test;

import java.nio.file.Files;
import java.nio.file.Paths;

public class MasterPlaylistParserTest {
    @Test
    public void masterParser() throws Exception {
        MasterPlaylist playlist = MasterPlaylistParser.parse(Files.newInputStream(Paths.get("src/test/resources/master.m3u8")));
        System.out.println(playlist);


        System.out.println(MasterPlaylistParser.writePlaylist(playlist));
    }



    @Test
    public void testAttributeListParser() throws Exception {
        String string = "CODECS=\"avc1.42c016,mp4a.40.2\",RESOLUTION=512x288,BANDWIDTH=444000,AUDIO=\"audio\"";
        MasterPlaylistParser.parseAttributes(string);
    }
}