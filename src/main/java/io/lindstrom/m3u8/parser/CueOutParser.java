package io.lindstrom.m3u8.parser;

import io.lindstrom.m3u8.model.CueOut;

import java.util.Map;

import static io.lindstrom.m3u8.parser.CueTagsSupport.EXT_X_CUE_OUT;

public class CueOutParser extends AbstractLineParser<CueOut> {
    CueOutParser() {
        super(EXT_X_CUE_OUT);
    }

    @Override
    CueOut parseAttributes(Map<String, String> attributes) throws PlaylistParserException {
        CueOut.Builder builder = CueOut.builder();


        builder.duration(Double.valueOf(attributes.get("DURATION")));
        builder.id(attributes.get("ID"));
        builder.cue(attributes.get("CUE"));

        return builder.build();

    }

    @Override
    String writeAttributes(CueOut value) {
        return null;
    }
}
