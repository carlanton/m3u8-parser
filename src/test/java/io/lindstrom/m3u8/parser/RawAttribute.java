package io.lindstrom.m3u8.parser;

import java.util.Objects;

public class RawAttribute {
    final String name;
    final String value;
    final boolean quoted;

    public RawAttribute(String name, String value, boolean quoted) {
        this.name = name;
        this.value = value;
        this.quoted = quoted;
    }

    @Override
    public String toString() {
        return name + "=" + (quoted ? ("\"" + value + "\"") : value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RawAttribute attribute = (RawAttribute) o;
        return quoted == attribute.quoted &&
                Objects.equals(name, attribute.name) &&
                Objects.equals(value, attribute.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, value, quoted);
    }
}
