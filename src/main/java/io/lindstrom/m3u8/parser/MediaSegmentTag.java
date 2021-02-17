package io.lindstrom.m3u8.parser;

import io.lindstrom.m3u8.model.DateRange;
import io.lindstrom.m3u8.model.MediaSegment;
import io.lindstrom.m3u8.model.SegmentKey;
import io.lindstrom.m3u8.model.SegmentMap;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Map;

enum MediaSegmentTag implements Tag<MediaSegment, MediaSegment.Builder> {
    EXT_X_DISCONTINUITY {
        @Override
        public void read(MediaSegment.Builder builder, String attributes, ParsingMode parsingMode) {
            builder.discontinuity(true);
        }

        @Override
        public void write(MediaSegment value, TextBuilder textBuilder) {
            if (value.discontinuity()) {
                textBuilder.addTag(tag());
            }
        }
    },

    EXT_X_PROGRAM_DATE_TIME {
        @Override
        public void read(MediaSegment.Builder builder, String attributes, ParsingMode parsingMode) {
            builder.programDateTime(OffsetDateTime.parse(attributes, ParserUtils.FORMATTER));
        }

        @Override
        public void write(MediaSegment mediaSegment, TextBuilder textBuilder) {
            mediaSegment.programDateTime().ifPresent(value ->
                    textBuilder.addTag(tag(), DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(value)));
        }
    },

    EXT_X_GAP {
        @Override
        public void read(MediaSegment.Builder builder, String attributes, ParsingMode parsingMode) {
            builder.gap(true);
        }

        @Override
        public void write(MediaSegment mediaSegment, TextBuilder textBuilder) {
            if (mediaSegment.gap()) {
                textBuilder.addTag(tag());
            }
        }
    },

    EXT_X_DATERANGE {
        private final Map<String, DateRangeAttribute> attributeMap = ParserUtils.toMap(DateRangeAttribute.values());

        @Override
        public void read(MediaSegment.Builder builder, String attributes, ParsingMode parsingMode) throws PlaylistParserException {
            builder.dateRange(
                    ParserUtils.readAttributes(
                            attributeMap,
                            attributes,
                            DateRange.builder(),
                            parsingMode));
        }

        @Override
        public void write(MediaSegment mediaSegment, TextBuilder textBuilder) {
            mediaSegment.dateRange().ifPresent(value -> textBuilder.addTag(tag(), value, attributeMap));
        }
    },

    EXT_X_CUE_OUT {
        @Override
        public void read(MediaSegment.Builder builder, String attributes, ParsingMode parsingMode) {
            builder.cueOut(Integer.parseInt(attributes));
        }

        @Override
        public void write(MediaSegment mediaSegment, TextBuilder textBuilder) {
            mediaSegment.cueOut().ifPresent(cueOut -> textBuilder.addTag(tag(), cueOut));
        }
    },

    EXT_X_CUE_IN {
        @Override
        public void read(MediaSegment.Builder builder, String attributes, ParsingMode parsingMode) {
            builder.cueIn(true);
        }

        @Override
        public void write(MediaSegment mediaSegment, TextBuilder textBuilder) {
            if (mediaSegment.cueIn()) {
                textBuilder.addTag(tag());
            }
        }
    },

    EXT_X_BITRATE {
        @Override
        public void read(MediaSegment.Builder builder, String attributes, ParsingMode parsingMode) {
            builder.bitrate(Long.parseLong(attributes));
        }

        @Override
        public void write(MediaSegment mediaSegment, TextBuilder textBuilder) {
            mediaSegment.bitrate().ifPresent(v -> textBuilder.addTag(tag(), v));
        }
    },

    EXT_X_MAP {
        private final Map<String, SegmentMapAttribute> attributeMap = ParserUtils.toMap(SegmentMapAttribute.values());

        @Override
        public void read(MediaSegment.Builder builder, String attributes, ParsingMode parsingMode) throws PlaylistParserException {
            builder.segmentMap(ParserUtils.readAttributes(attributeMap, attributes, SegmentMap.builder(), parsingMode));
        }

        @Override
        public void write(MediaSegment mediaSegment, TextBuilder textBuilder) {
            mediaSegment.segmentMap().ifPresent(value -> textBuilder.addTag(tag(), value, attributeMap));
        }
    },

    EXTINF {
        @Override
        public void read(MediaSegment.Builder builder, String attributes, ParsingMode parsingMode) {
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
            String duration;
            double d = mediaSegment.duration();
            if (d >= 0.001 && d < 10000000) {
                duration = Double.toString(d);
            } else {
                // When d > 10^3 or d <= 10^7, Double.toString will use "computerized scientific notation" which is not
                // supported by the HLS spec. As a workaround we use DecimalFormat. It's not thread-safe so we will
                // create a new instance on each call. However, this should rarely happen since it's very strange
                // segment size.
                DecimalFormat format = new DecimalFormat("0", DecimalFormatSymbols.getInstance(Locale.ENGLISH));
                format.setMaximumFractionDigits(340);
                duration = format.format(d);
            }

            textBuilder.add('#').add(tag()).add(":").add(duration).add(",");
            mediaSegment.title().ifPresent(textBuilder::add);
            textBuilder.add('\n');
        }
    },

    EXT_X_BYTERANGE {
        @Override
        public void read(MediaSegment.Builder builder, String attributes, ParsingMode parsingMode) throws PlaylistParserException {
            builder.byteRange(ParserUtils.parseByteRange(attributes));
        }

        @Override
        public void write(MediaSegment mediaSegment, TextBuilder textBuilder) {
            mediaSegment.byteRange().ifPresent(value -> textBuilder.addTag(tag(), ParserUtils.writeByteRange(value)));
        }
    },

    EXT_X_KEY {
        private final Map<String, SegmentKeyAttribute> attributeMap = ParserUtils.toMap(SegmentKeyAttribute.values());

        @Override
        public void read(MediaSegment.Builder builder, String attributes, ParsingMode parsingMode) throws PlaylistParserException {
            builder.segmentKey(ParserUtils.readAttributes(attributeMap, attributes, SegmentKey.builder(), parsingMode));
        }

        @Override
        public void write(MediaSegment mediaSegment, TextBuilder textBuilder) {
            mediaSegment.segmentKey().ifPresent(key -> textBuilder.addTag(tag(), key, attributeMap));
        }
    }
}
