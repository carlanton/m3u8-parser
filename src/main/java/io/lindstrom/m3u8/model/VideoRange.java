package io.lindstrom.m3u8.model;

public enum VideoRange {
    SDR, PQ, HLG;
    
    public static VideoRange parse(String name) {
        return VideoRange.valueOf(name);
    }
}
