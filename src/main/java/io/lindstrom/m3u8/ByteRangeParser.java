package io.lindstrom.m3u8;

import io.lindstrom.m3u8.model.ByteRange;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static io.lindstrom.m3u8.Tags.EXT_X_BYTERANGE;

class ByteRangeParser extends AbstractLineParser<ByteRange> {
    private static final Pattern BYTE_RANGE_PATTERN = Pattern.compile("(\\d+)(?:@(\\d+))?");
    ByteRangeParser() {
        super(EXT_X_BYTERANGE);
    }

    @Override
    public ByteRange parse(String attributes) {
        Matcher matcher = BYTE_RANGE_PATTERN.matcher(attributes);
        if (!matcher.matches()) {
            throw new RuntimeException("Invalid byte range " + attributes);
        }
        ByteRange.Builder byteRange = ByteRange.builder();
        byteRange.length(Long.parseLong(matcher.group(1)));
        if (matcher.group(2) != null) {
            byteRange.offset(Long.parseLong(matcher.group(2)));
        }
        return byteRange.build();
    }

    @Override
    protected String writeAttributes(ByteRange byteRange) {
        return byteRange.length() +
                byteRange.offset().map(offset -> "@" + offset).orElse("");
    }
}
