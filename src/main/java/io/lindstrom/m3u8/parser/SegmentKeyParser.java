package io.lindstrom.m3u8.parser;

import io.lindstrom.m3u8.model.KeyMethod;
import io.lindstrom.m3u8.model.SegmentKey;

public enum SegmentKeyParser implements AttributeMapper<SegmentKey, SegmentKey.Builder> {
    METHOD {
        @Override
        public void read(SegmentKey.Builder builder, String value) {
            builder.method(KeyMethod.parse(value));
        }

        @Override
        public void write(AttributeListBuilder attributes, SegmentKey segmentKey) {
            attributes.add(name(), segmentKey.method());
        }
    },

    URI {
        @Override
        public void read(SegmentKey.Builder builder, String value) {
            builder.uri(value);
        }

        @Override
        public void write(AttributeListBuilder attributes, SegmentKey segmentKey) {
            segmentKey.uri().ifPresent(uri -> attributes.addQuoted(name(), uri));
        }
    },

    IV {
        @Override
        public void read(SegmentKey.Builder builder, String value) {
            builder.iv(value);
        }

        @Override
        public void write(AttributeListBuilder attributes, SegmentKey segmentKey) {
            segmentKey.iv().ifPresent(iv -> attributes.add(name(), iv));
        }
    },

    KEYFORMAT {
        @Override
        public void read(SegmentKey.Builder builder, String value) {
            builder.keyFormat(value);
        }

        @Override
        public void write(AttributeListBuilder attributes, SegmentKey segmentKey) {
            segmentKey.keyFormat().ifPresent(keyFormat -> attributes.addQuoted(name(), keyFormat));
        }
    },

    KEYFORMATVERSIONS {
        @Override
        public void read(SegmentKey.Builder builder, String value) {
            builder.keyFormatVersions(value);
        }

        @Override
        public void write(AttributeListBuilder attributes, SegmentKey segmentKey) {
            segmentKey.keyFormatVersions().ifPresent(v -> attributes.addQuoted(name(), v));
        }
    }
}
