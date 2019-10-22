package com.github.zacscoding.tracej.agent.config;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.regex.Pattern;

import org.junit.Test;

/**
 * @GitHub : https://github.com/zacscoding
 */
public class ProxyMethodConfigTest {

    @Test
    public void matches() {
        // given
        ProxyMethodConfig config = new ProxyMethodConfig();
        config.setName("method1");
        config.setInvoker(false);
        config.setFilterType(FilterType.STARTSWITH);

        // when then
        assertTrue(config.matches("method1"));
        assertTrue(config.matches("method12"));
        assertTrue(config.matches("method123"));
        assertFalse(config.matches("method321"));

        // given
        config.setFilterType(FilterType.ALL);

        // when then
        assertTrue(config.matches("com/demo/temp"));
        assertTrue(config.matches("com/demo/temp/temp2/temp3"));

        // given
        config.setFilterType(FilterType.REGEX);
        config.setPattern(Pattern.compile("^[a-zA-Z0-9_]*$"));

        // when then
        assertTrue(config.matches("method2"));
        assertTrue(config.matches("method2_"));
        assertFalse(config.matches("method$$"));
    }
}
