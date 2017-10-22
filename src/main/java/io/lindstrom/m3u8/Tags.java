package io.lindstrom.m3u8;

public class Tags {
    // https://tools.ietf.org/html/rfc8216

    /*
     * 4.3.1 Basic Tags
     */

    // 4.3.1.1 EXTM3U
    public static final String EXTM3U = "#EXTM3U";

    // 4.3.1.2 EXT-X-VERSION
    public static final String EXT_X_VERSION = "#EXT-X-VERSION";


    /*
     * 4.3.2 Media Segment Tags
     */

    // 4.3.2.1 EXTINF
    public static final String EXTINF = "#EXTINF";

    // 4.3.2.2 EXT-X-BYTERANGE
    public static final String EXT_X_BYTERANGE = "#EXT-X-BYTERANGE";

    // 4.3.2.3 EXT-X-DISCONTINUITY
    public static final String EXT_X_DISCONTINUITY = "#EXT-X-DISCONTINUITY";

    // 4.3.2.4 EXT-X-KEY
    public static final String EXT_X_KEY = "#EXT-X-KEY";

    // 4.3.2.5 EXT-X-MAP
    public static final String EXT_X_MAP = "#EXT-X-MAP";
    public static final String BYTERANGE = "BYTERANGE";

    // 4.3.2.6 EXT-X-PROGRAM-DATE-TIME
    public static final String EXT_X_PROGRAM_DATE_TIME = "#EXT-X-PROGRAM-DATE-TIME";

    // 4.3.2.7 EXT-X-DATERANGE
    public static final String EXT_X_DATERANGE = "#EXT-X-DATERANGE";


    /*
     * 4.3.3 Media Playlist Tags
     */

    // 4.3.3.1 EXT-X-TARGETDURATION
    public static final String EXT_X_TARGETDURATION = "#EXT-X-TARGETDURATION";

    // 4.3.3.2 EXT-X-MEDIA-SEQUENCE
    public static final String EXT_X_MEDIA_SEQUENCE = "#EXT-X-MEDIA-SEQUENCE";

    // 4.3.3.3 EXT-X-DISCONTINUITY-SEQUENCE
    public static final String EXT_X_DISCONTINUITY_SEQUENCE = "#EXT-X-DISCONTINUITY-SEQUENCE";

    // 4.3.3.4 EXT-X-ENDLIST
    public static final String EXT_X_ENDLIST = "#EXT-X-ENDLIST";

    // 4.3.3.5 EXT-X-PLAYLIST-TYPE
    public static final String EXT_X_PLAYLIST_TYPE = "#EXT-X-PLAYLIST-TYPE";

    // 4.3.3.6 EXT-X-I-FRAMES-ONLY
    public static final String EXT_X_I_FRAMES_ONLY = "#EXT-X-I-FRAMES-ONLY";


    /*
     * 4.3.4 Master Playlist Tags
     */

    // 4.3.4.1 EXT-X-MEDIA
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

    // 4.3.4.4 EXT_X_SESSION_DATA
    public static final String EXT_X_SESSION_DATA = "#EXT-X-SESSION-DATA";

    // 4.3.4.5 EXT-X-SESSION-KEY
    public static final String EXT_X_SESSION_KEY = "#EXT-X-SESSION-KEY";


    /*
     * 4.3.5 Media or Master Playlist Tags
     */

    // 4.3.5.1 EXT-X-INDEPENDENT-SEGMENTS
    public static final String EXT_X_INDEPENDENT_SEGMENTS = "#EXT-X-INDEPENDENT-SEGMENTS";

    // 4.3.5.2 EXT-X-START
    public static final String EXT_X_START = "#EXT-X-START";
}
