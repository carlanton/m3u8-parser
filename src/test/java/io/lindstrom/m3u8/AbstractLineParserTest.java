package io.lindstrom.m3u8;

import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.assertEquals;

public class AbstractLineParserTest {
    @Test
    public void testAttributeListParser() throws Exception {
        String string = "CODECS=\"avc1.42c016,mp4a.40.2\",RESOLUTION=512x288,BANDWIDTH=444000,AUDIO=\"audio\"";
        Map<String, String> map = AbstractLineParser.parseAttributes(string);
        assertEquals(4, map.size());
        assertEquals("avc1.42c016,mp4a.40.2", map.get("CODECS"));
        assertEquals("512x288", map.get("RESOLUTION"));
        assertEquals("444000", map.get("BANDWIDTH"));
        assertEquals("audio", map.get("AUDIO"));
    }
}