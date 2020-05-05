package io.lindstrom.m3u8.parser;

import io.lindstrom.m3u8.model.SessionData;

import java.util.Map;

import static io.lindstrom.m3u8.parser.Tags.*;

public class SessionDataParser extends AbstractLineParser<SessionData> {
    SessionDataParser() {
        super(EXT_X_SESSION_DATA);
    }

    @Override
    SessionData parseAttributes(Map<String, String> attributes) throws PlaylistParserException {
        SessionData.Builder builder = SessionData.builder();
        for (Map.Entry<String, String> entry : attributes.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            switch (key) {
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
                    throw new PlaylistParserException("Unknown key " + key);
            }
        }

        return builder.build();
    }

    @Override
    String writeAttributes(SessionData sessionData) {
        AttributeListBuilder attributes = new AttributeListBuilder();

        attributes.addQuoted(DATA_ID, sessionData.dataId());
        sessionData.value().ifPresent(v -> attributes.addQuoted(VALUE, v));
        sessionData.uri().ifPresent(v -> attributes.addQuoted(URI, v));
        sessionData.language().ifPresent(v -> attributes.addQuoted(LANGUAGE, v));

        return attributes.toString();
    }
}
