package io.lindstrom.m3u8.parser;

import io.lindstrom.m3u8.model.ByteRange;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static io.lindstrom.m3u8.parser.Tags.EXT_X_BYTERANGE;

class ByteRangeParser extends AbstractLineParser<ByteRange> {
    private static final Pattern BYTE_RANGE_PATTERN = Pattern.compile("(\\d+)(?:@(\\d+))?");

    ByteRangeParser() {
        super(EXT_X_BYTERANGE);
    }

    @Override
    ByteRange parse(String attributes) throws PlaylistParserException {
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
    ByteRange parseAttributes(Map<String, String> attributes) throws PlaylistParserException {
        throw new IllegalStateException();
    }

    @Override
    String writeAttributes(ByteRange byteRange) {
        return byteRange.length() +
                byteRange.offset().map(offset -> "@" + offset).orElse("");
    }
}
