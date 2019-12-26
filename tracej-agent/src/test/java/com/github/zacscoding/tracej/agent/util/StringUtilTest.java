package com.github.zacscoding.tracej.agent.util;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class StringUtilTest {

    @Test
    public void testTrimClassName() {
        // given
        String[][] data = {
                {
                        "com/package1/package2/ClassName",
                        "OnlyClassName"
                },
                {
                        "c.p.p.ClassName",
                        "OnlyClassName"
                }
        };

        // when then
        for (int i = 0; i < data[0].length; i++) {
            String actual = StringUtil.trimClassName(data[0][i]);
            assertThat(actual, is(data[1][i]));
        }
    }
}
