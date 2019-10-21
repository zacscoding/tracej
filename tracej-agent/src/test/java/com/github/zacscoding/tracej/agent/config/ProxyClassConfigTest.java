package com.github.zacscoding.tracej.agent.config;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.regex.Pattern;

import org.junit.Test;

/**
 * @GitHub : https://github.com/zacscoding
 */
public class ProxyClassConfigTest {

    @Test
    public void matches() {
        // given
        ProxyClassConfig config = new ProxyClassConfig();
        config.setName("com/demo");
        config.setInvoker(false);
        config.setFilterType(FilterType.STARTSWITH);

        // when then
        assertTrue(config.matches("com/demo/temp"));
        assertTrue(config.matches("com/demo/temp/temp2/temp3"));
        assertFalse(config.matches("com/zaccoding/temp/temp2/temp3"));

        // given
        config.setFilterType(FilterType.ALL);

        // when then
        // always returns false if filter type is all
        assertFalse(config.matches("com/demo/temp"));
        assertFalse(config.matches("com/demo/temp/temp2/temp3"));

        // given
        config.setFilterType(FilterType.REGEX);
        config.setPattern(Pattern.compile("^[a-z][a-z0-9_]*(\\/[a-z0-9_]+)+[0-9a-z_]$"));

        // when then
        assertTrue(config.matches("com/demo/temp"));
        assertFalse(config.matches("123/p4"));
    }
}
