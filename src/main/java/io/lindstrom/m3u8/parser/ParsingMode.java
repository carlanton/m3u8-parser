package io.lindstrom.m3u8.parser;

public class ParsingMode {
    public static final ParsingMode STRICT = new ParsingMode(true);
    public static final ParsingMode LENIENT = new ParsingMode(false);

    private final boolean failOnUnknownAttributes;

    private ParsingMode(boolean failOnUnknownAttributes) {
        this.failOnUnknownAttributes = failOnUnknownAttributes;
    }

    boolean failOnUnknownAttributes() {
        return failOnUnknownAttributes;
    }
}
