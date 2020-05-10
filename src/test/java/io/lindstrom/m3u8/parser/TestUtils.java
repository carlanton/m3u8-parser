package io.lindstrom.m3u8.parser;

import java.nio.file.Path;
import java.util.List;

import static org.junit.Assert.assertEquals;

class TestUtils {
    static void attributeConsistencyCheck(String original, String written, Path playlistPath) throws PlaylistParserException {
        RawPlaylistParser quoteCheckingParser = new RawPlaylistParser();

        RawPlaylist expected = quoteCheckingParser.readPlaylist(original);
        RawPlaylist actual = quoteCheckingParser.readPlaylist(written);

        assertEquals(expected.tags().keySet(), actual.tags().keySet());

        for (String tagName : expected.tags().keySet()) {
            List<List<RawAttribute>> expectedTags = expected.tags().get(tagName);
            List<List<RawAttribute>> actualTags = actual.tags().get(tagName);

            assertEquals(expectedTags.size(), actualTags.size());

            for (int j = 0; j < expectedTags.size(); j++) {
                List<RawAttribute> expectedAttributes = expectedTags.get(j);
                List<RawAttribute> actualAttributes = actualTags.get(j);

                assertEquals(expectedAttributes.size(), actualAttributes.size());

                for (int i = 0; i < expectedAttributes.size(); i++) {
                    RawAttribute expectedAttribute = expectedAttributes.get(i);
                    RawAttribute actualAttribute = actualAttributes.get(i);

                    assertEquals(expectedAttribute.name, actualAttribute.name);
                    assertEquals(String.format("Inconsistent use of attribute quotes in tag %s. Expected `%s`, actual `%s`. (playlist: %s)",
                            tagName, expectedAttribute, actualAttribute, playlistPath),
                            expectedAttribute.quoted, actualAttribute.quoted);
                }
            }
        }

    }
}
