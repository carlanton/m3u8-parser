# m3u8-parser
[![Build Status](https://travis-ci.org/carlanton/m3u8-parser.svg?branch=master)](https://travis-ci.org/carlanton/m3u8-parser) [![Maven Central](https://maven-badges.herokuapp.com/maven-central/io.lindstrom/m3u8-parser/badge.svg)](https://maven-badges.herokuapp.com/maven-central/io.lindstrom/m3u8-parser)
[![Javadocs](https://www.javadoc.io/badge/io.lindstrom/m3u8-parser.svg)](https://www.javadoc.io/doc/io.lindstrom/m3u8-parser)


A simple HLS playlist parser for Java.

The goal of this project was to implement parsers and a consistent Java object model
according to [RFC 8216 HTTP Live Streaming](https://tools.ietf.org/html/rfc8216).

This parser is very similar to iHeartRadio's [open-m3u8](https://github.com/iheartradio/open-m3u8). The main differences are:
 * m3u8-parser does not try to validate playlists. You are responsible for creating valid playlists.
 * m3u8-parser uses java.util.Optional instead of `null`.
 * m3u8-parser uses [Immutables](https://immutables.github.io/) to generate all builders.
 * The parser objects are thread safe & reusable and could be used as a singleton (like Jackson's ObjectMapper).
 * m3u8-parser requires Java 8 or later.

## Artifacts
Maven:
```xml
<dependency>
    <groupId>io.lindstrom</groupId>
    <artifactId>m3u8-parser</artifactId>
    <version>0.24</version>
</dependency>
```
Gradle:
```
implementation 'io.lindstrom:m3u8-parser:0.24'
```

## Usage

### Create master playlist
```java
MasterPlaylist playlist = MasterPlaylist.builder()
    .version(4)
    .independentSegments(true)
    .addAlternativeRenditions(AlternativeRendition.builder()
        .type(MediaType.AUDIO)
        .name("Default audio")
        .groupId("AUDIO")
        .build())
    .addVariants(
        Variant.builder()
            .addCodecs("avc1.4d401f", "mp4a.40.2")
            .bandwidth(900000)
            .uri("v0.m3u8")
            .build(),
        Variant.builder()
            .addCodecs("avc1.4d401f", "mp4a.40.2")
            .bandwidth(900000)
            .uri("v1.m3u8")
            .resolution(1280, 720)
            .build())
    .build();

MasterPlaylistParser parser = new MasterPlaylistParser();
System.out.println(parser.writePlaylistAsString(playlist));
```

This code should produce the following master playlist:
```
#EXTM3U
#EXT-X-VERSION:4
#EXT-X-INDEPENDENT-SEGMENTS
#EXT-X-MEDIA:TYPE=AUDIO,GROUP-ID="AUDIO",NAME="Default audio"
#EXT-X-STREAM-INF:BANDWIDTH=900000,CODECS="avc1.4d401f,mp4a.40.2"
v0.m3u8
#EXT-X-STREAM-INF:BANDWIDTH=900000,CODECS="avc1.4d401f,mp4a.40.2",RESOLUTION=1280x720
v1.m3u8
```


### Create media playlist
```java
MediaPlaylist mediaPlaylist = MediaPlaylist.builder()
    .version(3)
    .targetDuration(10)
    .mediaSequence(1)
    .ongoing(false)
    .addMediaSegments(
        MediaSegment.builder()
            .duration(9.009)
            .uri("http://media.example.com/first.ts")
            .build(),
        MediaSegment.builder()
            .duration(9.009)
            .uri("http://media.example.com/second.ts")
            .build(),
        MediaSegment.builder()
            .duration(3.003)
            .uri("http://media.example.com/third.ts")
            .build())
    .build();

MediaPlaylistParser parser = new MediaPlaylistParser();
System.out.println(parser.writePlaylistAsString(mediaPlaylist));
```

This code should produce the following media playlist:
```
#EXTM3U
#EXT-X-VERSION:3
#EXT-X-TARGETDURATION:10
#EXT-X-MEDIA-SEQUENCE:1
#EXTINF:9.009,
http://media.example.com/first.ts
#EXTINF:9.009,
http://media.example.com/second.ts
#EXTINF:3.003,
http://media.example.com/third.ts
#EXT-X-ENDLIST
```

### Parse master playlist
```java
MasterPlaylistParser parser = new MasterPlaylistParser();

// Parse playlist
MasterPlaylist playlist = parser.readPlaylist(Paths.get("path/to/master.m3u8"));

// Update playlist version
MasterPlaylist updated = MasterPlaylist.builder()
                                        .from(playlist)
                                        .version(2)
                                        .build();

// Write playlist to standard out
System.out.println(parser.writePlaylistAsString(updated));
```

### Parse media playlist
```java
MediaPlaylistParser parser = new MediaPlaylistParser();

// Parse playlist
MediaPlaylist playlist = parser.readPlaylist(Paths.get("path/to/media-playlist.m3u8"));

// Update playlist version
MediaPlaylist updated = MediaPlaylist.builder()
                                     .from(playlist)
                                     .version(2)
                                     .build();

// Write playlist to standard out
System.out.println(parser.writePlaylistAsString(updated));
```

### Parsing mode

By default, the parser will throw an exception on unsupported tags and attributes. This can be configured by
passing a `ParsingMode` to the parser. Example:

```java
MasterPlaylistParser lenientParser = new MasterPlaylistParser(ParsingMode.LENIENT);
```

Currently two modes are available:
```java
ParsingMode.STRICT   // fail on unsupported things (this is default)
ParsingMode.LENIENT  // ignore unsupported things
```

## Supported tags
The following tags should be fully supported:
```
EXTM3U
EXT-X-VERSION
EXTINF
EXT-X-BYTERANGE
EXT-X-DISCONTINUITY
EXT-X-KEY
EXT-X-MAP
EXT-X-PROGRAM-DATE-TIME
EXT-X-TARGETDURATION
EXT-X-MEDIA-SEQUENCE
EXT-X-ENDLIST
EXT-X-PLAYLIST-TYPE
EXT-X-I-FRAMES-ONLY
EXT-X-MEDIA
EXT-X-STREAM-INF
EXT-X-I-FRAME-STREAM-INF
EXT-X-INDEPENDENT-SEGMENTS
EXT-X-START
EXT-X-ALLOW-CACHE
EXT-X-SESSION-DATA
EXT-X-SESSION-KEY
EXT-X-DISCONTINUITY-SEQUENCE
EXT-X-DATERANGE

EXT-X-DEFINE
EXT-X-GAP
EXT-X-BITRATE
EXT-X-SERVER-CONTROL

EXT-X-PART
EXT-X-PRELOAD-HINT
EXT-X-RENDITION-REPORT
EXT-X-SKIP
EXT-X-PART-INF

EXT-X-CUE-OUT:<duration>
EXT-X-CUE-IN
```

The following tags are currently not implemented:
```
```

## Android
This library uses `java.time.*` which requires [core library desugaring](https://developer.android.com/studio/write/java8-support#library-desugaring) when running on Android API level < 26.
