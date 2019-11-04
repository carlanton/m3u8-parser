package io.lindstrom.m3u8.parser;

import io.lindstrom.m3u8.model.CueOut;
import io.lindstrom.m3u8.model.CueSpan;

import java.util.Map;

import static io.lindstrom.m3u8.parser.CueTagsSupport.EXT_X_CUE_OUT;
import static io.lindstrom.m3u8.parser.CueTagsSupport.EXT_X_CUE_SPAN;

public class CueSpanParser extends AbstractLineParser<CueSpan> {
    CueSpanParser() {
        super(EXT_X_CUE_SPAN);
    }

    @Override
    CueSpan parseAttributes(Map<String, String> attributes) throws PlaylistParserException {

        CueSpan.Builder builder = CueSpan.builder();


        builder.timeFromSignal(attributes.get("TIMEFROMSIGNAL"));
        builder.id(Double.valueOf(attributes.get("ID")));

        return builder.build();

    }

    @Override
    String writeAttributes(CueSpan value) {
        return null;
    }

}
