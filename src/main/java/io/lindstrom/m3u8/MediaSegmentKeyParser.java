package io.lindstrom.m3u8;

import io.lindstrom.m3u8.model.KeyMethod;
import io.lindstrom.m3u8.model.MediaSegmentKey;
import io.lindstrom.m3u8.util.AttributeListBuilder;

import java.util.Map;

import static io.lindstrom.m3u8.Tags.*;

class MediaSegmentKeyParser extends AbstractLineParser<MediaSegmentKey> {
    MediaSegmentKeyParser() {
        super(Tags.EXT_X_KEY);
    }

    @Override
    protected MediaSegmentKey parseAttributes(Map<String, String> attributes) throws PlaylistParserException {
        MediaSegmentKey.Builder builder = MediaSegmentKey.builder();
        builder.method(KeyMethod.parse(attributes.get(Tags.METHOD)));

        if (attributes.containsKey(URI)) {
            builder.uri(attributes.get(URI));
        }
        if (attributes.containsKey(IV)) {
            builder.iv(attributes.get(IV));
        }
        if (attributes.containsKey(KEYFORMAT)) {
            builder.keyFormat(attributes.get(KEYFORMAT));
        }
        if (attributes.containsKey(KEYFORMATVERSIONS)) {
            builder.keyFormatVersions(attributes.get(KEYFORMATVERSIONS));
        }

        return builder.build();
    }

    @Override
    protected String writeAttributes(MediaSegmentKey mediaSegmentKey) {
        AttributeListBuilder attributes = new AttributeListBuilder();
        attributes.add(Tags.METHOD, mediaSegmentKey.method());
        mediaSegmentKey.uri().ifPresent(uri -> attributes.addQuoted(URI, uri));
        mediaSegmentKey.iv().ifPresent(iv -> attributes.add(IV, iv));
        mediaSegmentKey.keyFormat().ifPresent(keyFormat -> attributes.addQuoted(KEYFORMAT, keyFormat));
        mediaSegmentKey.keyFormatVersions().ifPresent(value -> attributes.addQuoted(KEYFORMATVERSIONS, value));
        return attributes.toString();
    }
}
