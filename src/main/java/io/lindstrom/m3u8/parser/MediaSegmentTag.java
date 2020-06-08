package io.lindstrom.m3u8.parser;

import io.lindstrom.m3u8.model.MediaSegment;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.OffsetDateTime;
import java.util.Locale;

enum MediaSegmentTag implements Tag<MediaSegment, MediaSegment.Builder> {
    EXT_X_DISCONTINUITY {
        @Override
        public void read(MediaSegment.Builder builder, String attributes) {
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
            mediaSegment.dateRange().ifPresent(value -> textBuilder.addTag(tag(), value, DateRangeAttributes.class));
        }
    },

    EXT_X_MAP {
        @Override
        public void read(MediaSegment.Builder builder, String attributes) throws PlaylistParserException {
            builder.segmentMap(SegmentMapAttribute.parse(attributes));
        }

        @Override
        public void write(MediaSegment mediaSegment, TextBuilder textBuilder) {
            mediaSegment.segmentMap().ifPresent(value -> textBuilder.addTag(tag(), value, SegmentMapAttribute.class));
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
        public void read(MediaSegment.Builder builder, String attributes) throws PlaylistParserException {
            builder.byteRange(ParserUtils.parseByteRange(attributes));
        }

        @Override
        public void write(MediaSegment mediaSegment, TextBuilder textBuilder) {
            mediaSegment.byteRange().ifPresent(value -> textBuilder.addTag(tag(), ParserUtils.writeByteRange(value)));
        }
    },

    EXT_X_KEY {
        @Override
        public void read(MediaSegment.Builder builder, String attributes) throws PlaylistParserException {
            builder.segmentKey(SegmentKeyAttribute.parse(attributes));
        }

        @Override
        public void write(MediaSegment mediaSegment, TextBuilder textBuilder) {
            mediaSegment.segmentKey().ifPresent(key -> textBuilder.addTag(tag(), key, SegmentKeyAttribute.class));
        }
    }
}
