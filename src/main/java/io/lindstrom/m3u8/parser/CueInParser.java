package io.lindstrom.m3u8.parser;

import io.lindstrom.m3u8.model.CueIn;
import io.lindstrom.m3u8.model.CueOut;

import java.util.Map;

import static io.lindstrom.m3u8.parser.CueTagsSupport.EXT_X_CUE_IN;
import static io.lindstrom.m3u8.parser.CueTagsSupport.EXT_X_CUE_OUT;

public class CueInParser extends AbstractLineParser<CueIn> {
    CueInParser() {
        super(EXT_X_CUE_IN);
    }

    @Override
    CueIn parseAttributes(Map<String, String> attributes) throws PlaylistParserException {
        CueIn.Builder builder = CueIn.builder();


        builder.id(attributes.get("ID"));

        return builder.build();

    }

    @Override
    String writeAttributes(CueIn value) {
        return null;
    }
}
