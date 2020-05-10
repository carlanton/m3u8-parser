package io.lindstrom.m3u8.parser;

import io.lindstrom.m3u8.model.*;

import java.time.OffsetDateTime;

enum MediaSegmentTag implements Tag<MediaSegment, MediaSegment.Builder> {
    EXT_X_BYTERANGE {
        @Override
        public void read(MediaSegment.Builder builder, String attributes) throws PlaylistParserException {
            builder.byteRange(ParserUtils.parseByteRange(attributes));
        }

        @Override
        public void write(MediaSegment mediaSegment, TextBuilder textBuilder) {
            mediaSegment.byteRange().ifPresent(value -> textBuilder.addTag(tag(), ParserUtils.writeByteRange(value)));
        }
    },

    EXTINF {
        @Override
        public void read(MediaSegment.Builder builder, String attributes) {
            int p = attributes.indexOf(',');

            if (p < 0) {
                builder.duration(Double.parseDouble(attributes));
            } else {
                builder.duration(Double.parseDouble(attributes.substring(0, p)));
                String title = attributes.substring(p + 1);
                if (!title.isEmpty()) {
                    builder.title(title);
                }
            }
        }

        @Override
        public void write(MediaSegment mediaSegment, TextBuilder textBuilder) {
            textBuilder.add(tag()).add(":").add(mediaSegment.duration()).add(",");
            mediaSegment.title().ifPresent(textBuilder::add);
            textBuilder.add('\n');
        }
    },

    EXT_X_PROGRAM_DATE_TIME {
        @Override
        public void read(MediaSegment.Builder builder, String attributes) {
            builder.programDateTime(OffsetDateTime.parse(attributes, ParserUtils.FORMATTER));
        }

        @Override
        public void write(MediaSegment mediaSegment, TextBuilder textBuilder) {
            mediaSegment.programDateTime().ifPresent(value -> textBuilder.addTag(tag(), value.toString()));
        }
    },

    EXT_X_DATERANGE {
        @Override
        public void read(MediaSegment.Builder builder, String attributes) throws PlaylistParserException {
            builder.dateRange(DateRangeAttributes.parse(attributes));
        }

        @Override
        public void write(MediaSegment mediaSegment, TextBuilder textBuilder) {
            mediaSegment.dateRange().ifPresent(value -> textBuilder.add(tag(), value, DateRangeAttributes.class));
        }
    },

    EXT_X_MAP {
        @Override
        public void read(MediaSegment.Builder builder, String attributes) throws PlaylistParserException {
            builder.segmentMap(SegmentMapAttribute.parse(attributes));
        }

        @Override
        public void write(MediaSegment mediaSegment, TextBuilder textBuilder) {
            mediaSegment.segmentMap().ifPresent(value -> textBuilder.add(tag(), value, SegmentMapAttribute.class));
        }
    },

    EXT_X_KEY {
        @Override
        public void read(MediaSegment.Builder builder, String attributes) throws PlaylistParserException {
            builder.segmentKey(SegmentKeyAttribute.parse(attributes));
        }

        @Override
        public void write(MediaSegment mediaSegment, TextBuilder textBuilder) {
            mediaSegment.segmentKey().ifPresent(key -> textBuilder.add(tag(), key, SegmentKeyAttribute.class));
        }
    },

    EXT_X_DISCONTINUITY {
        @Override
        public void read(MediaSegment.Builder builder, String attributes) {
            builder.discontinuity(true);
        }

        @Override
        public void write(MediaSegment value, TextBuilder textBuilder) {
            if (value.discontinuity()) {
                textBuilder.add(tag()).add('\n');
            }
        }
    }
}
