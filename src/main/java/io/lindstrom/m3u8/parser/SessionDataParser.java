package io.lindstrom.m3u8.parser;

import io.lindstrom.m3u8.model.SessionData;

import static io.lindstrom.m3u8.parser.Tags.*;

public final class SessionDataParser extends AbstractTagParser<SessionData, SessionData.Builder> {
    SessionDataParser() {
        super(EXT_X_SESSION_DATA);
    }

    @Override
    void onAttribute(String attribute, String value, SessionData.Builder builder) throws PlaylistParserException {
        switch (attribute) {
            case DATA_ID:
                builder.dataId(value);
                break;

            case VALUE:
                builder.value(value);
                break;

            case URI:
                builder.uri(value);
                break;

            case LANGUAGE:
                builder.language(value);
                break;

            default:
                throw new PlaylistParserException("Unknown key " + attribute);
        }
    }

    @Override
    void write(SessionData sessionData, AttributeListBuilder attributes) {
        attributes.addQuoted(DATA_ID, sessionData.dataId());
        sessionData.value().ifPresent(v -> attributes.addQuoted(VALUE, v));
        sessionData.uri().ifPresent(v -> attributes.addQuoted(URI, v));
        sessionData.language().ifPresent(v -> attributes.addQuoted(LANGUAGE, v));
    }

    @Override
    SessionData.Builder newBuilder() {
        return SessionData.builder();
    }

    @Override
    SessionData build(SessionData.Builder builder) {
        return builder.build();
    }
}
