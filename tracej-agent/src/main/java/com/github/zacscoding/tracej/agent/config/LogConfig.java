package com.github.zacscoding.tracej.agent.config;

/**
 * Configuration of logging
 */
public class LogConfig {

    private boolean enableConsole;
    private String filePath;

    public boolean isEnableConsole() {
        return enableConsole;
    }

    public void setEnableConsole(boolean enableConsole) {
        this.enableConsole = enableConsole;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}
