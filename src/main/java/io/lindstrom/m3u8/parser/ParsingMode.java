package io.lindstrom.m3u8.parser;

public class ParsingMode {
    public static final ParsingMode STRICT = new ParsingMode(true, true);
    public static final ParsingMode LENIENT = new ParsingMode(false, false);

    private final boolean failOnUnknownAttributes;
    private final boolean failOnUnknownTags;

    private ParsingMode(boolean failOnUnknownAttributes, boolean failOnUnknownTags) {
        this.failOnUnknownAttributes = failOnUnknownAttributes;
        this.failOnUnknownTags = failOnUnknownTags;
    }

    boolean failOnUnknownAttributes() {
        return failOnUnknownAttributes;
    }

    boolean failOnUnknownTags() {
        return failOnUnknownTags;
    }
}
