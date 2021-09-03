package io.lindstrom.m3u8.parser;

import io.lindstrom.m3u8.model.RenditionReport;

import java.util.Map;

public enum RenditionReportAttribute implements Attribute<RenditionReport, RenditionReport.Builder> {

    URI {
        @Override
        public void read(RenditionReport.Builder builder, String value) throws PlaylistParserException {
            builder.uri(value);
        }

        @Override
        public void write(RenditionReport value, TextBuilder textBuilder) {
            textBuilder.addQuoted(name(), value.uri());
        }
    },

    LAST_MSN {
        @Override
        public void read(RenditionReport.Builder builder, String value) throws PlaylistParserException {
            builder.lastMediaSequenceNumber(Long.parseLong(value));
        }

        @Override
        public void write(RenditionReport value, TextBuilder textBuilder) {
            value.lastMediaSequenceNumber().ifPresent(v -> textBuilder.add(key(), Long.toString(v)));
        }
    },

    LAST_PART {
        @Override
        public void read(RenditionReport.Builder builder, String value) throws PlaylistParserException {
            builder.lastPartialSegmentIndex(Long.parseLong(value));
        }

        @Override
        public void write(RenditionReport value, TextBuilder textBuilder) {
            value.lastPartialSegmentIndex().ifPresent(v -> textBuilder.add(key(), Long.toString(v)));
        }
    };

    final static Map<String, RenditionReportAttribute> attributeMap = ParserUtils.toMap(values(), Attribute::key);

    static RenditionReport parse(String attributes, ParsingMode parsingMode) throws PlaylistParserException {
        RenditionReport.Builder builder = RenditionReport.builder();
        ParserUtils.readAttributes(attributeMap, attributes, builder, parsingMode);
        return builder.build();
    }
}
