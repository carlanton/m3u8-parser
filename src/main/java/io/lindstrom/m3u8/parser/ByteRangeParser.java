package io.lindstrom.m3u8.parser;

import io.lindstrom.m3u8.model.ByteRange;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

class ByteRangeParser implements TagParser<ByteRange> {
    private static final Pattern BYTE_RANGE_PATTERN = Pattern.compile("(\\d+)(?:@(\\d+))?");

    @Override
    public ByteRange parse(String attributes) throws PlaylistParserException {
        Matcher matcher = BYTE_RANGE_PATTERN.matcher(attributes);
        if (!matcher.matches()) {
            throw new PlaylistParserException("Invalid byte range " + attributes);
        }
        ByteRange.Builder byteRange = ByteRange.builder();
        byteRange.length(Long.parseLong(matcher.group(1)));
        if (matcher.group(2) != null) {
            byteRange.offset(Long.parseLong(matcher.group(2)));
        }
        return byteRange.build();
    }

    @Override
    public void write(ByteRange byteRange, StringBuilder stringBuilder) {
        stringBuilder
                .append(Tags.EXT_X_BYTERANGE)
                .append(":")
                .append(writeAttributes(byteRange))
                .append("\n");
    }

    String writeAttributes(ByteRange byteRange) {
        return byteRange.length() + byteRange.offset().map(offset -> "@" + offset).orElse("");
    }
}
