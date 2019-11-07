package io.lindstrom.m3u8.parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.lindstrom.m3u8.model.AdSmart;
import io.lindstrom.m3u8.model.AdSmartBuilder;
import io.lindstrom.m3u8.model.CueSpan;

public class CueTagsSupport implements TagsSupport {
    public static final String EXT_X_CUE_OUT ="#EXT-X-CUE-OUT";
    public static final String EXT_X_CUE_SPAN ="#EXT-X-CUE-SPAN";
    public static final String EXT_X_CUE_IN ="#EXT-X-CUE-IN";
    private List<CueSpan> cueSpans = new ArrayList<>();
    private static final List<String> SUPPORTED_TAGS = Arrays.asList(
            EXT_X_CUE_OUT,
            EXT_X_CUE_SPAN,
            EXT_X_CUE_IN
    );

    private final CueOutParser cueOutParser = new CueOutParser();
    private final CueInParser cueInParser = new CueInParser();
    private final CueSpanParser cueSpanParser = new CueSpanParser();
    @Override
    public boolean supports(String prefix) {
        return SUPPORTED_TAGS.contains(prefix);
    }

    @Override
    public void process(String prefix, String attributes, MediaPlaylistBuildersContainer container) throws PlaylistParserException {

        AdSmartBuilder builder =  (AdSmartBuilder) container.builderByType(AdSmartBuilder.class);

        switch(prefix){
            case EXT_X_CUE_OUT:
                builder.cueOut(cueOutParser.parse(attributes));

            break;
            case EXT_X_CUE_IN:
                builder.cueIn(cueInParser.parse(attributes));
                break;

            case EXT_X_CUE_SPAN:
                cueSpans.add(cueSpanParser.parse(attributes));
                builder.cueSpan(cueSpans);

            break;

        }
    }

}
