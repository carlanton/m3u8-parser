package io.lindstrom.m3u8;

import io.lindstrom.m3u8.model.AlternativeRendition;
import io.lindstrom.m3u8.model.MediaType;
import io.lindstrom.m3u8.util.AttributeListBuilder;
import io.lindstrom.m3u8.util.ParserUtils;

import java.util.Map;

import static io.lindstrom.m3u8.Tags.*;

class AlternativeRenditionParser extends AbstractLineParser<AlternativeRendition> {
    AlternativeRenditionParser() {
        super(EXT_X_MEDIA);
    }

    @Override
    protected AlternativeRendition parseAttributes(Map<String, String> attributes) {
        AlternativeRendition.Builder builder = AlternativeRendition.builder();
        for (Map.Entry<String, String> entry : attributes.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            switch (key) {
                case TYPE:
                    builder.type(MediaType.parse(value));
                    break;
                case URI:
                    builder.uri(value);
                    break;
                case GROUP_ID:
                    builder.groupId(value);
                    break;
                case LANGUAGE:
                    builder.language(value);
                    break;
                case ASSOC_LANGUAGE:
                    builder.assocLanguage(value);
                    break;
                case NAME:
                    builder.name(value);
                    break;
                case DEFAULT:
                    builder.defaultRendition(yesOrNo(value));
                    break;
                case AUTOSELECT:
                    builder.autoSelect(yesOrNo(value));
                    break;
                case FORCED:
                    builder.forced(yesOrNo(value));
                    break;
                case INSTREAM_ID:
                    builder.inStreamId(value);
                    break;
                case CHARACTERISTICS:
                    builder.characteristics(ParserUtils.split(value, ","));
                    break;
                case CHANNELS:
                    builder.channels(ParserUtils.split(value, "/"));
                    break;
                default:
                    throw new RuntimeException("Unknown key " + key);
            }
        }
        return builder.build();
    }

    @Override
    protected String writeAttributes(AlternativeRendition alternativeRendition) {
        AttributeListBuilder attributes = new AttributeListBuilder();

        attributes.add(Tags.TYPE, alternativeRendition.type());
        alternativeRendition.uri().ifPresent(uri -> attributes.addQuoted(Tags.URI, uri));
        attributes.addQuoted(Tags.GROUP_ID, alternativeRendition.groupId());
        alternativeRendition.language().ifPresent(value -> attributes.addQuoted(Tags.LANGUAGE, value));
        alternativeRendition.assocLanguage().ifPresent(value -> attributes.addQuoted(Tags.ASSOC_LANGUAGE, value));
        attributes.addQuoted(Tags.NAME, alternativeRendition.name());
        alternativeRendition.defaultRendition().ifPresent(value -> attributes.add(Tags.DEFAULT, value));
        alternativeRendition.autoSelect().ifPresent(value -> attributes.add(Tags.AUTOSELECT, value));
        alternativeRendition.forced().ifPresent(value -> attributes.add(Tags.FORCED, value));
        alternativeRendition.inStreamId().ifPresent(value -> attributes.addQuoted(Tags.INSTREAM_ID, value));

        if (!alternativeRendition.characteristics().isEmpty()) {
            attributes.addQuoted(Tags.CHARACTERISTICS, String.join(",", alternativeRendition.characteristics()));
        }

        if (!alternativeRendition.channels().isEmpty()) {
            attributes.addQuoted(Tags.CHANNELS, String.join("/", alternativeRendition.channels()));
        }
        return attributes.toString();
    }

    private static boolean yesOrNo(String value) {
        switch (value) {
            case "YES":
                return true;
            case "NO":
                return false;
            default:
                throw new RuntimeException("Meh");
        }
    }
}
