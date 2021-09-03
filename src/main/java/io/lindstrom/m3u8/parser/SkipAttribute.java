package io.lindstrom.m3u8.parser;

import io.lindstrom.m3u8.model.Skip;

import java.util.Map;

public enum SkipAttribute implements Attribute<Skip, Skip.Builder> {
    SKIPPED_SEGMENTS {
        @Override
        public void read(Skip.Builder builder, String value) throws PlaylistParserException {
            builder.skippedSegments(Long.parseLong(value));
        }

        @Override
        public void write(Skip value, TextBuilder textBuilder) {
            textBuilder.add(key(), Long.toString(value.skippedSegments()));
        }
    },

    RECENTLY_REMOVED_DATERANGES {
        @Override
        public void read(Skip.Builder builder, String value) throws PlaylistParserException {
            builder.recentlyRemovedDateRanges(ParserUtils.split(value, "\t"));
        }

        @Override
        public void write(Skip value, TextBuilder textBuilder) {
            if (!value.recentlyRemovedDateRanges().isEmpty()) {
                textBuilder.addQuoted(key(), String.join("\t", value.recentlyRemovedDateRanges()));
            }
        }
    };

    final static Map<String, SkipAttribute> attributeMap = ParserUtils.toMap(values(), Attribute::key);

    static Skip parse(String attributes, ParsingMode parsingMode) throws PlaylistParserException {
        Skip.Builder builder = Skip.builder();
        ParserUtils.readAttributes(attributeMap, attributes, builder, parsingMode);
        return builder.build();
    }
}
