package com.github.zacscoding.tracej.agent.config;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.Test;

/**
 * Test of {@link Config}
 *
 * @GitHub : https://github.com/zacscoding
 */
public class ConfigTest {

    @Test
    public void parseConfig() {
        File configFile = new File("src/test/resources/config/valid-config.yaml");
        Config.INSTANCE.reload(configFile.getAbsolutePath());
        assertFalse(Config.INSTANCE.hasError());

        ProxyConfig proxyConfig = Config.INSTANCE.getProxy();
        assertNotNull(proxyConfig);

        // TODO : add assertion of config values
    }

    @Test
    public void initializeConfigErrorWhenNoArgs() {
        Config.INSTANCE.reload("");
        assertTrue(Config.INSTANCE.hasError());
    }

    @Test
    public void initializeConfigErrorWhenNoFile() {
        File notExistFile = new File("src/test/resources/config/not_exist.yaml");
        Config.INSTANCE.reload(notExistFile.getAbsolutePath());
        assertTrue(Config.INSTANCE.hasError());
    }
}
