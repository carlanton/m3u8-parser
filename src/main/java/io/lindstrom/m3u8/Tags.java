package io.lindstrom.m3u8;

public class Tags {
    // https://tools.ietf.org/html/rfc8216#section-4.3

    // 4.3.1 Basic Tags
    public static final String EXTM3U = "#EXTM3U";
    public static final String EXT_X_VERSION = "#EXT-X-VERSION:";

    // 4.3.2 Media Segment Tags

    // 4.3.3 Media Playlist Tags

    // 4.3.4 Master Playlist Tags
    public static final String EXT_X_MEDIA = "#EXT-X-MEDIA";

    public static final String TYPE = "TYPE";
    public static final String URI = "URI";
    public static final String GROUP_ID = "GROUP-ID";
    public static final String LANGUAGE = "LANGUAGE";
    public static final String ASSOC_LANGUAGE = "ASSOC-LANGUAGE";
    public static final String NAME = "NAME";
    public static final String DEFAULT = "DEFAULT";
    public static final String AUTOSELECT = "AUTOSELECT";
    public static final String FORCED = "FORCED";
    public static final String INSTREAM_ID = "INSTREAM-ID";
    public static final String CHARACTERISTICS = "CHARACTERISTICS";
    public static final String CHANNELS = "CHANNELS";

    // 4.3.4.2 EXT-X-STREAM-INF
    public static final String EXT_X_STREAM_INF = "#EXT-X-STREAM-INF";

    public static final String BANDWIDTH = "BANDWIDTH";
    public static final String AVERAGE_BANDWIDTH = "AVERAGE-BANDWIDTH";
    public static final String CODECS = "CODECS";
    public static final String RESOLUTION = "RESOLUTION";
    public static final String FRAME_RATE = "FRAME-RATE";
    public static final String HDCP_LEVEL = "HDCP-LEVEL";
    public static final String AUDIO = "AUDIO";
    public static final String VIDEO = "VIDEO";
    public static final String SUBTITLES = "SUBTITLES";
    public static final String CLOSED_CAPTIONS = "CLOSED-CAPTIONS";

    // 4.3.4.3 EXT-X-I-FRAME-STREAM-INF
    public static final String EXT_X_I_FRAME_STREAM_INF = "#EXT-X-I-FRAME-STREAM-INF";


    // 4.3.5.  Media or Master Playlist Tags
    public static final String EXT_X_INDEPENDENT_SEGMENTS = "#EXT-X-INDEPENDENT-SEGMENTS";
}
