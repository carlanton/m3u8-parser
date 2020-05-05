package io.lindstrom.m3u8.parser;

import io.lindstrom.m3u8.model.DateRange;

import java.time.OffsetDateTime;
import java.util.Map;

import static io.lindstrom.m3u8.parser.Tags.*;

public class DateRangeParser extends AbstractLineParser<DateRange> {
    DateRangeParser() {
        super(EXT_X_DATERANGE);
    }

    @Override
    DateRange parseAttributes(Map<String, String> attributes) throws PlaylistParserException {
        DateRange.Builder builder = DateRange.builder();

        for (Map.Entry<String, String> entry : attributes.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            switch (key) {
                case ID:
                    builder.id(value);
                    break;

                case CLASS:
                    builder.classAttribute(value);
                    break;

                case START_DATE:
                    builder.startDate(OffsetDateTime.parse(value, MediaPlaylistParser.FORMATTER));
                    break;

                case END_DATE:
                    builder.endDate(OffsetDateTime.parse(value, MediaPlaylistParser.FORMATTER));
                    break;

                case DURATION:
                    builder.duration(Double.parseDouble(value));
                    break;

                case PLANNED_DURATION:
                    builder.plannedDuration(Double.parseDouble(value));
                    break;

                case SCTE35_CMD:
                    builder.scte35Cmd(value);
                    break;

                case SCTE35_OUT:
                    builder.scte35Out(value);
                    break;

                case SCTE35_IN:
                    builder.scte35In(value);
                    break;

                case END_ON_NEXT:
                    builder.endOnNext(ParserUtils.yesOrNo(value));
                    break;

                default:
                    if (key.startsWith("X-")) {
                        builder.putClientAttributes(key, value);
                    } else {
                        throw new PlaylistParserException("Unknown key " + key);
                    }
            }
        }

        return builder.build();
    }

    @Override
    String writeAttributes(DateRange dateRange) {
        AttributeListBuilder attributes = new AttributeListBuilder();

        attributes.addQuoted(ID, dateRange.id());
        dateRange.classAttribute().ifPresent(v -> attributes.addQuoted(CLASS, v));
        attributes.addQuoted(START_DATE, dateRange.startDate().toString());
        dateRange.endDate().ifPresent(v -> attributes.addQuoted(END_DATE, v.toString()));
        dateRange.duration().ifPresent(v -> attributes.add(DURATION, Double.toString(v)));
        dateRange.plannedDuration().ifPresent(v -> attributes.add(PLANNED_DURATION, Double.toString(v)));

        // TODO: support client attribute types (quoted-string, hexadecimal-sequence & decimal-floating-point)
        dateRange.clientAttributes().forEach(attributes::addQuoted);

        dateRange.scte35Cmd().ifPresent(v -> attributes.add(SCTE35_CMD, v));
        dateRange.scte35Out().ifPresent(v -> attributes.add(SCTE35_OUT, v));
        dateRange.scte35In().ifPresent(v -> attributes.add(SCTE35_IN, v));

        if (dateRange.endOnNext()) {
            attributes.add(END_ON_NEXT, YES);
        }

        return attributes.toString();
    }
}
