package io.lindstrom.m3u8.parser;

import io.lindstrom.m3u8.model.*;

import java.time.OffsetDateTime;

enum MediaSegmentTags implements Tag<MediaSegment, MediaSegment.Builder> {
    EXT_X_BYTERANGE {
        @Override
        public void read(MediaSegment.Builder builder, String attributes) throws PlaylistParserException {
            builder.byteRange(ParserUtils.parseByteRange(attributes));
        }

        @Override
        public void write(MediaSegment mediaSegment, TextBuilder textBuilder) {
            mediaSegment.byteRange().ifPresent(byteRange -> textBuilder
                            .add(tag())
                            .add(":")
                            .add(ParserUtils.writeByteRange(byteRange))
                            .add("\n"));
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
            mediaSegment.programDateTime().ifPresent(value -> textBuilder
                    .add(tag()).add(':')
                    .add(value)
                    .add('\n'));
        }
    },

    EXT_X_DATERANGE {
        @Override
        public void read(MediaSegment.Builder builder, String attributes) throws PlaylistParserException {
            builder.dateRange(AbstractPlaylistParser.readAttributes(DateRangeParser.class, attributes, DateRange.builder()).build());
        }

        @Override
        public void write(MediaSegment mediaSegment, TextBuilder textBuilder) {
            mediaSegment.dateRange().ifPresent(value -> textBuilder.add(tag(), value, DateRangeParser.class));
        }
    },

    EXT_X_MAP {
        @Override
        public void read(MediaSegment.Builder builder, String attributes) throws PlaylistParserException {
            builder.segmentMap(AbstractPlaylistParser.readAttributes(SegmentMapParser.class, attributes, SegmentMap.builder()).build());
        }

        @Override
        public void write(MediaSegment mediaSegment, TextBuilder textBuilder) {
            mediaSegment.segmentMap().ifPresent(map -> textBuilder.add(tag(), map, SegmentMapParser.class));
        }
    },

    EXT_X_KEY {
        @Override
        public void read(MediaSegment.Builder builder, String attributes) throws PlaylistParserException {
            builder.segmentKey(AbstractPlaylistParser.readAttributes(SegmentKeyParser.class, attributes, SegmentKey.builder()).build());
        }

        @Override
        public void write(MediaSegment mediaSegment, TextBuilder textBuilder) {
            mediaSegment.segmentKey().ifPresent(key -> textBuilder.add(tag(), key, SegmentKeyParser.class));
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
