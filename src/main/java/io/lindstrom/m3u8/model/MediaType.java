package io.lindstrom.m3u8.model;

public enum MediaType {
    AUDIO,
    VIDEO,
    SUBTITLES,
    CLOSED_CAPTIONS;

    @Override
    public String toString() {
        if (this == CLOSED_CAPTIONS) {
            return CLOSED_CAPTIONS_STRING;
        }
        return super.toString();
    }

    public static MediaType parse(String name) {
        if (CLOSED_CAPTIONS_STRING.equals(name)) {
            return CLOSED_CAPTIONS;
        }
        return MediaType.valueOf(name);
    }

    private static final String CLOSED_CAPTIONS_STRING = "CLOSED-CAPTIONS";
}
