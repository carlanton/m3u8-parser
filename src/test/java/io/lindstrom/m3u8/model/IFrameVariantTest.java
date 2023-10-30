package io.lindstrom.m3u8.model;

import org.junit.Test;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static org.junit.Assert.assertTrue;

public class IFrameVariantTest {
    @Test
    public void allMethodsFromVariantExists() {
        // All attributes defined for the EXT-X-STREAM-INF tag (Section 4.4.6.2)
        // are also defined for the EXT-X-I-FRAME-STREAM-INF tag, except for the
        // FRAME-RATE, AUDIO, SUBTITLES, and CLOSED-CAPTIONS attributes.
        List<String> ignoreMethods = new ArrayList<>();
        ignoreMethods.add("frameRate");
        ignoreMethods.add("audio");
        ignoreMethods.add("subtitles");
        ignoreMethods.add("frameRate");
        ignoreMethods.add("closedCaptions");
        ignoreMethods.add("closedCaptionsNone");

        for (Method m : Variant.class.getMethods()) {
            if (ignoreMethods.contains(m.getName())) {
                continue;
            }

            if (Modifier.isStatic(m.getModifiers())) {
                continue;
            }

            boolean found = false;
            for (Method iFrameVariantMethod : IFrameVariant.class.getMethods()) {
                if (!iFrameVariantMethod.getName().equals(m.getName())) {
                    continue;
                }

                if (!Arrays.equals(iFrameVariantMethod.getParameterTypes(), m.getParameterTypes())) {
                    continue;
                }

                if (!Objects.equals(iFrameVariantMethod.getReturnType(), m.getReturnType())) {
                    continue;
                }

                found = true;
                break;
            }

            assertTrue(m.getName() + " should exist", found);
        }
    }

    @Test
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void uriMethodShouldExist() throws NoSuchMethodException {
        IFrameVariant.class.getMethod("uri");
    }
}