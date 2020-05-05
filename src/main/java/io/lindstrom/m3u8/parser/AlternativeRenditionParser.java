package io.lindstrom.m3u8.parser;

import io.lindstrom.m3u8.model.AlternativeRendition;
import io.lindstrom.m3u8.model.MediaType;

import static io.lindstrom.m3u8.parser.Tags.*;

class AlternativeRenditionParser extends AbstractTagParser<AlternativeRendition, AlternativeRendition.Builder> {
    AlternativeRenditionParser() {
        super(EXT_X_MEDIA);
    }

    @Override
    void onAttribute(String attribute, String value, AlternativeRendition.Builder builder) throws PlaylistParserException {
        switch (attribute) {
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
                builder.defaultRendition(ParserUtils.yesOrNo(value));
                break;
            case AUTOSELECT:
                builder.autoSelect(ParserUtils.yesOrNo(value));
                break;
            case FORCED:
                builder.forced(ParserUtils.yesOrNo(value));
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
                throw new PlaylistParserException("Unknown key " + attribute);
        }
    }

    @Override
    void write(AlternativeRendition alternativeRendition, AttributeListBuilder attributes) {
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
    }

    @Override
    AlternativeRendition.Builder newBuilder() {
        return AlternativeRendition.builder();
    }

    @Override
    AlternativeRendition build(AlternativeRendition.Builder builder) {
        return builder.build();
    }
}
