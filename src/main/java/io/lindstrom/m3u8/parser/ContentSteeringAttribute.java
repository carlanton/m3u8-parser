package io.lindstrom.m3u8.parser;

import io.lindstrom.m3u8.model.ContentSteering;
import io.lindstrom.m3u8.model.SegmentMap;

import java.util.Map;

/*
 * #EXT-X-CONTENT-STEERING:<attribute-list>
 */
enum ContentSteeringAttribute implements Attribute<ContentSteering, ContentSteering.Builder> {

    SERVER_URI {
        @Override
        public void read(ContentSteering.Builder builder, String value) throws PlaylistParserException {
            builder.serverUri(value);
        }

        @Override
        public void write(ContentSteering contentSteering, TextBuilder textBuilder) {
            textBuilder.addQuoted(key(), contentSteering.serverUri());
        }
    },

    PATHWAY_ID {
        @Override
        public void read(ContentSteering.Builder builder, String value) throws PlaylistParserException {
            builder.pathwayId(value);
        }

        @Override
        public void write(ContentSteering contentSteering, TextBuilder textBuilder) {
            contentSteering.pathwayId().ifPresent(pathwayId -> textBuilder.addQuoted(key(), pathwayId));
        }
    };

    final static Map<String, ContentSteeringAttribute> attributeMap = ParserUtils.toMap(values(), Attribute::key);

    static ContentSteering parse(String attributes, ParsingMode parsingMode) throws PlaylistParserException {
        ContentSteering.Builder builder = ContentSteering.builder();
        ParserUtils.readAttributes(attributeMap, attributes, builder, parsingMode);
        return builder.build();
    }
}