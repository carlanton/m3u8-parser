package io.lindstrom.m3u8;

import io.lindstrom.m3u8.model.KeyMethod;
import io.lindstrom.m3u8.model.SegmentKey;
import org.junit.Test;

import static io.lindstrom.m3u8.model.KeyMethod.AES_128;
import static io.lindstrom.m3u8.model.KeyMethod.NONE;
import static io.lindstrom.m3u8.model.KeyMethod.SAMPLE_AES;
import static org.junit.Assert.assertEquals;

public class SegmentKeyParserTest {
    private SegmentKeyParser parser = new SegmentKeyParser();

    private SegmentKey key = SegmentKey.builder()
            .method(KeyMethod.AES_128)
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
        assertEquals(parser.parse(attributes), key);
    }

    @Test
    public void writeAttributes() throws Exception {
        assertEquals(attributes, parser.writeAttributes(key));
    }

    @Test
    public void parseMethods() throws Exception {
        assertEquals(AES_128, parser.parse("METHOD=AES-128").method());
        assertEquals(SAMPLE_AES, parser.parse("METHOD=SAMPLE-AES").method());
        assertEquals(NONE, parser.parse("METHOD=NONE").method());
    }

    @Test
    public void writeMethods() throws Exception {
        assertEquals("METHOD=AES-128", parser.writeAttributes(SegmentKey.builder()
                .method(KeyMethod.AES_128)
                .build()));
        assertEquals("METHOD=SAMPLE-AES", parser.writeAttributes(SegmentKey.builder()
                .method(KeyMethod.SAMPLE_AES)
                .build()));
        assertEquals("METHOD=NONE", parser.writeAttributes(SegmentKey.builder()
                .method(KeyMethod.NONE)
                .build()));
    }
}
