package io.lindstrom.m3u8.parser;

import io.lindstrom.m3u8.model.SegmentKey;
import org.junit.Test;

import static io.lindstrom.m3u8.model.KeyMethod.*;
import static org.junit.Assert.assertEquals;

public class SegmentKeyParserTest {
    private SegmentKey key = SegmentKey.builder()
            .method(AES_128)
            .uri("https://priv.example.com/key.php?r=53")
            .iv("0xc055ee9f6c1eb7aa50bfab02b0814972")
            .keyFormat("identity")
            .keyFormatVersions("1/2/5")
            .build();

    private String attributes = "METHOD=AES-128," +
            "URI=\"https://priv.example.com/key.php?r=53\"," +
            "IV=0xc055ee9f6c1eb7aa50bfab02b0814972," +
            "KEYFORMAT=\"identity\"," +
            "KEYFORMATVERSIONS=\"1/2/5\"";

    @Test
    public void parseAttributes() throws Exception {
        assertEquals(SegmentKeyAttribute.parse(attributes, ParsingMode.STRICT), key);
    }

    @Test
    public void writeAttributes() throws Exception {
        assertEquals(attributes, writeAttributes(key));
    }

    @Test
    public void parseMethods() throws Exception {
        assertEquals(AES_128, SegmentKeyAttribute.parse("METHOD=AES-128", ParsingMode.STRICT).method());
        assertEquals(SAMPLE_AES, SegmentKeyAttribute.parse("METHOD=SAMPLE-AES", ParsingMode.STRICT).method());
        assertEquals(NONE, SegmentKeyAttribute.parse("METHOD=NONE", ParsingMode.STRICT).method());
    }

    @Test
    public void writeMethods() throws Exception {
        assertEquals("METHOD=AES-128", writeAttributes(SegmentKey.builder()
                .method(AES_128)
                .build()));
        assertEquals("METHOD=SAMPLE-AES", writeAttributes(SegmentKey.builder()
                .method(SAMPLE_AES)
                .build()));
        assertEquals("METHOD=NONE", writeAttributes(SegmentKey.builder()
                .method(NONE)
                .build()));
    }

    private String writeAttributes(SegmentKey segmentKey) {
        return new TextBuilder()
                .addTag("EXT-X-KEY", segmentKey, SegmentKeyAttribute.attributeMap)
                .toString()
                .substring(11)
                .trim();
    }
}
