package io.lindstrom.m3u8.model;

public enum KeyMethod {
    NONE, AES_128, SAMPLE_AES;

    @Override
    public String toString() {
        switch (this) {
            case AES_128:
                return AES_128_STRING;
            case SAMPLE_AES:
                return SAMPLE_AES_STRING;
            default:
                return super.toString();
        }
    }

    public static KeyMethod parse(String name) {
        switch (name) {
            case AES_128_STRING:
                return AES_128;
            case SAMPLE_AES_STRING:
                return SAMPLE_AES;
            default:
                return KeyMethod.valueOf(name);
        }
    }

    private static final String AES_128_STRING = "AES-128";
    private static final String SAMPLE_AES_STRING = "SAMPLE-AES";
}
