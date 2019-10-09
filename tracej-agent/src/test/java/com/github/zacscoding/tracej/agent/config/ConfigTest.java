package com.github.zacscoding.tracej.agent.config;

import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.Test;

/**
 * Test of {@link Config}
 *
 * @GitHub : https://github.com/zacscoding
 */
public class ConfigTest {

    private static final String CONFIG_PATH = "tracej.config.path";

    @Test
    public void initializeConfigErrorWhenNoArgs() {
        Config.INSTANCE.reload();
        assertTrue(Config.INSTANCE.hasError());
    }

    @Test
    public void initializeConfigErrorWhenNoFile() {
        File notExistFile = new File("src/test/java/resources/config/not_exist.yaml");
        System.setProperty(CONFIG_PATH, notExistFile.getAbsolutePath());
        Config.INSTANCE.reload();
        assertTrue(Config.INSTANCE.hasError());
    }
}
