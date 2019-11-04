package io.lindstrom.m3u8.model;


import org.hamcrest.CoreMatchers;
import org.junit.Test;

import static org.junit.Assert.assertThat;

public class ResolutionTest {

    @Test
    public void test() {
        assertThat(Resolution.of(1, 2).width(), CoreMatchers.equalTo(1));
        assertThat(Resolution.builder().width(1).height(2).build().width(), CoreMatchers.equalTo(1));
    }

}