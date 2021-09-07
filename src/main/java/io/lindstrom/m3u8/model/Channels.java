package io.lindstrom.m3u8.model;

import org.immutables.value.Value;

import java.util.List;

@Value.Immutable
public interface Channels {

    int count();

    List<String> objectCodingIdentifiers();

    static Builder builder() {
        return new Builder();
    }

    class Builder extends ChannelsBuilder {

    }

    static Channels of(int count) {
        return builder().count(count).build();
    }

}
