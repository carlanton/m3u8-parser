package io.lindstrom.m3u8;

import io.lindstrom.m3u8.model.MapInfo;
import io.lindstrom.m3u8.util.AttributeListBuilder;

import java.util.Map;

import static io.lindstrom.m3u8.Tags.*;

class MapInfoParser extends AbstractLineParser<MapInfo> {
    private final ByteRangeParser byteRangeParser = new ByteRangeParser();

    MapInfoParser() {
        super(EXT_X_MAP);
    }

    @Override
    protected MapInfo parseAttributes(Map<String, String> attributes) {
        MapInfo.Builder builder = MapInfo.builder()
                .uri(attributes.get(URI));

        if (attributes.containsKey(BYTERANGE)) {
            builder.byteRange(byteRangeParser.parse(attributes.get(BYTERANGE)));
        }

        return builder.build();
    }

    @Override
    protected String writeAttributes(MapInfo mapInfo) {
        AttributeListBuilder attributes = new AttributeListBuilder();
        attributes.addQuoted(URI, mapInfo.uri());
        mapInfo.byteRange().map(byteRangeParser::writeAttributes).ifPresent(value ->
                attributes.addQuoted(BYTERANGE, value));
        return attributes.toString();
    }
}
