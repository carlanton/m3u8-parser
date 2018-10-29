package io.lindstrom.m3u8.parser;

class Tags {
    // https://tools.ietf.org/html/rfc8216

    /*
     * 4.3.1 Basic Tags
     */

    // 4.3.1.1 EXTM3U
    static final String EXTM3U = "#EXTM3U";

    // 4.3.1.2 EXT-X-VERSION
    static final String EXT_X_VERSION = "#EXT-X-VERSION";


    /*
     * 4.3.2 Media Segment Tags
     */

    // 4.3.2.1 EXTINF
    static final String EXTINF = "#EXTINF";

    // 4.3.2.2 EXT-X-BYTERANGE
    static final String EXT_X_BYTERANGE = "#EXT-X-BYTERANGE";

    // 4.3.2.3 EXT-X-DISCONTINUITY
    static final String EXT_X_DISCONTINUITY = "#EXT-X-DISCONTINUITY";

    // 4.3.2.4 EXT-X-KEY
    static final String EXT_X_KEY = "#EXT-X-KEY";
    static final String METHOD = "METHOD";
    static final String IV = "IV";
    static final String KEYFORMAT = "KEYFORMAT";
    static final String KEYFORMATVERSIONS = "KEYFORMATVERSIONS";

    // 4.3.2.5 EXT-X-MAP
    static final String EXT_X_MAP = "#EXT-X-MAP";
    static final String BYTERANGE = "BYTERANGE";

    // 4.3.2.6 EXT-X-PROGRAM-DATE-TIME
    static final String EXT_X_PROGRAM_DATE_TIME = "#EXT-X-PROGRAM-DATE-TIME";

    // 4.3.2.7 EXT-X-DATERANGE
    static final String EXT_X_DATERANGE = "#EXT-X-DATERANGE";


    /*
     * 4.3.3 Media Playlist Tags
     */

    // 4.3.3.1 EXT-X-TARGETDURATION
    static final String EXT_X_TARGETDURATION = "#EXT-X-TARGETDURATION";

    // 4.3.3.2 EXT-X-MEDIA-SEQUENCE
    static final String EXT_X_MEDIA_SEQUENCE = "#EXT-X-MEDIA-SEQUENCE";

    // 4.3.3.3 EXT-X-DISCONTINUITY-SEQUENCE
    static final String EXT_X_DISCONTINUITY_SEQUENCE = "#EXT-X-DISCONTINUITY-SEQUENCE";

    // 4.3.3.4 EXT-X-ENDLIST
    static final String EXT_X_ENDLIST = "#EXT-X-ENDLIST";

    // 4.3.3.5 EXT-X-PLAYLIST-TYPE
    static final String EXT_X_PLAYLIST_TYPE = "#EXT-X-PLAYLIST-TYPE";

    // 4.3.3.6 EXT-X-I-FRAMES-ONLY
    static final String EXT_X_I_FRAMES_ONLY = "#EXT-X-I-FRAMES-ONLY";


    /*
     * 4.3.4 Master Playlist Tags
     */

    // 4.3.4.1 EXT-X-MEDIA
    static final String EXT_X_MEDIA = "#EXT-X-MEDIA";
    static final String TYPE = "TYPE";
    static final String URI = "URI";
    static final String GROUP_ID = "GROUP-ID";
    static final String LANGUAGE = "LANGUAGE";
    static final String ASSOC_LANGUAGE = "ASSOC-LANGUAGE";
    static final String NAME = "NAME";
    static final String DEFAULT = "DEFAULT";
    static final String AUTOSELECT = "AUTOSELECT";
    static final String FORCED = "FORCED";
    static final String INSTREAM_ID = "INSTREAM-ID";
    static final String CHARACTERISTICS = "CHARACTERISTICS";
    static final String CHANNELS = "CHANNELS";

    // 4.3.4.2 EXT-X-STREAM-INF
    static final String EXT_X_STREAM_INF = "#EXT-X-STREAM-INF";
    static final String BANDWIDTH = "BANDWIDTH";
    static final String AVERAGE_BANDWIDTH = "AVERAGE-BANDWIDTH";
    static final String CODECS = "CODECS";
    static final String RESOLUTION = "RESOLUTION";
    static final String FRAME_RATE = "FRAME-RATE";
    static final String HDCP_LEVEL = "HDCP-LEVEL";
    static final String AUDIO = "AUDIO";
    static final String VIDEO = "VIDEO";
    static final String SUBTITLES = "SUBTITLES";
    static final String CLOSED_CAPTIONS = "CLOSED-CAPTIONS";
    static final String PROGRAM_ID = "PROGRAM-ID";

    // 4.3.4.3 EXT-X-I-FRAME-STREAM-INF
    static final String EXT_X_I_FRAME_STREAM_INF = "#EXT-X-I-FRAME-STREAM-INF";

    // 4.3.4.4 EXT_X_SESSION_DATA
    static final String EXT_X_SESSION_DATA = "#EXT-X-SESSION-DATA";

    // 4.3.4.5 EXT-X-SESSION-KEY
    static final String EXT_X_SESSION_KEY = "#EXT-X-SESSION-KEY";


    /*
     * 4.3.5 Media or Master Playlist Tags
     */

    // 4.3.5.1 EXT-X-INDEPENDENT-SEGMENTS
    static final String EXT_X_INDEPENDENT_SEGMENTS = "#EXT-X-INDEPENDENT-SEGMENTS";

    // 4.3.5.2 EXT-X-START
    static final String EXT_X_START = "#EXT-X-START";

    static final String YES = "YES";
    static final String NO = "NO";
}
