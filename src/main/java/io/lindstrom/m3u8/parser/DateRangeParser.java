package io.lindstrom.m3u8.parser;

import io.lindstrom.m3u8.model.DateRange;

import java.time.OffsetDateTime;

import static io.lindstrom.m3u8.parser.Tags.YES;

enum DateRangeParser implements Attribute<DateRange, DateRange.Builder> {
    ID {
        @Override
        public void read(DateRange.Builder builder, String value) {
            builder.id(value);
        }

        @Override
        public void write(AttributeListBuilder attributes, DateRange value) {
            attributes.addQuoted(name(), value.id());
        }
    },

    CLASS {
        @Override
        public void read(DateRange.Builder builder, String value) {
            builder.classAttribute(value);
        }

        @Override
        public void write(AttributeListBuilder attributes, DateRange value) {
            value.classAttribute().ifPresent(v -> attributes.addQuoted(name(), v));
        }
    },

    START_DATE {
        @Override
        public void read(DateRange.Builder builder, String value) {
            builder.startDate(OffsetDateTime.parse(value, ParserUtils.FORMATTER));
        }

        @Override
        public void write(AttributeListBuilder attributes, DateRange value) {
            attributes.addQuoted(key(), value.startDate().toString());
        }
    },

    END_DATE {
        @Override
        public void read(DateRange.Builder builder, String value) {
            builder.endDate(OffsetDateTime.parse(value, ParserUtils.FORMATTER));
        }

        @Override
        public void write(AttributeListBuilder attributes, DateRange value) {
            value.endDate().ifPresent(v -> attributes.addQuoted(key(), v.toString()));
        }
    },

    DURATION {
        @Override
        public void read(DateRange.Builder builder, String value) {
            builder.duration(Double.parseDouble(value));
        }

        @Override
        public void write(AttributeListBuilder attributes, DateRange value) {
            value.duration().ifPresent(v -> attributes.add(name(), Double.toString(v)));
        }
    },

    PLANNED_DURATION {
        @Override
        public void read(DateRange.Builder builder, String value) {
            builder.plannedDuration(Double.parseDouble(value));
        }

        @Override
        public void write(AttributeListBuilder attributes, DateRange value) {
            value.plannedDuration().ifPresent(v -> attributes.add(key(), Double.toString(v)));
        }
    },

    SCTE35_CMD {
        @Override
        public void read(DateRange.Builder builder, String value) {
            builder.scte35Cmd(value);
        }

        @Override
        public void write(AttributeListBuilder attributes, DateRange value) {
            value.scte35Cmd().ifPresent(v -> attributes.add(key(), v));
        }
    },

    SCTE35_OUT {
        @Override
        public void read(DateRange.Builder builder, String value) throws PlaylistParserException {
            builder.scte35Out(value);
        }

        @Override
        public void write(AttributeListBuilder attributes, DateRange value) {
            value.scte35Out().ifPresent(v -> attributes.add(key(), v));
        }
    },

    SCTE35_IN {
        @Override
        public void read(DateRange.Builder builder, String value) {
            builder.scte35In(value);
        }

        @Override
        public void write(AttributeListBuilder attributes, DateRange value) {
            value.scte35In().ifPresent(v -> attributes.add(key(), v));
        }
    },

    END_ON_NEXT {
        @Override
        public void read(DateRange.Builder builder, String value) throws PlaylistParserException {
            builder.endOnNext(ParserUtils.yesOrNo(value));
        }

        @Override
        public void write(AttributeListBuilder attributes, DateRange value) {
            if (value.endOnNext()) {
                attributes.add(key(), YES);
            }
        }
    },

    CLIENT_ATTRIBUTE {
        @Override
        public void read(DateRange.Builder builder, String value) {
            throw new IllegalStateException();
        }

        @Override
        public void read(DateRange.Builder builder, String key, String value) {
            builder.putClientAttributes(key, value);
        }

        @Override
        public void write(AttributeListBuilder attributes, DateRange value) {
            // TODO: support client attribute types (quoted-string, hexadecimal-sequence & decimal-floating-point)
            value.clientAttributes().forEach(attributes::addQuoted);
        }
    }
}
