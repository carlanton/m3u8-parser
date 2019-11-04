package io.lindstrom.m3u8.parser;

import io.lindstrom.m3u8.model.MediaPlaylist;

import java.util.Arrays;
import java.util.List;

public class CueTagsSupport implements TagsSupport {
    public static final String EXT_X_CUE_OUT ="#EXT-X-CUE-OUT";
    public static final String EXT_X_CUE_SPAN ="#EXT-X-CUE-SPAN";
    public static final String EXT_X_CUE_IN ="#EXT-X-CUE-IN";
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
    public void process(String prefix, String attributes, MediaPlaylistBuilder builder) throws PlaylistParserException {

        switch(prefix){
            case EXT_X_CUE_OUT:
                cueOutParser.parse(attributes);
            break;
            case EXT_X_CUE_IN:
                cueInParser.parse(attributes);
                break;

            case EXT_X_CUE_SPAN:
                cueSpanParser.parse(attributes);

            break;

        }
    }
}
