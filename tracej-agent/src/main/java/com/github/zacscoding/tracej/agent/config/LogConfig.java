package com.github.zacscoding.tracej.agent.config;

/**
 * Configuration of logging
 */
public class LogConfig {

    private boolean traceCallStack;
    private DumpConfig dumpConfig;
    private ConsoleConfig consoleConfig;
    private FileConfig fileConfig;

    // getters, setters
    public boolean isTraceCallStack() {
        return traceCallStack;
    }

    public void setTraceCallStack(boolean traceCallStack) {
        this.traceCallStack = traceCallStack;
    }

    public DumpConfig getDumpConfig() {
        return dumpConfig;
    }

    public void setDumpConfig(DumpConfig dumpConfig) {
        this.dumpConfig = dumpConfig;
    }

    public ConsoleConfig getConsoleConfig() {
        return consoleConfig;
    }

    public void setConsoleConfig(ConsoleConfig consoleConfig) {
        this.consoleConfig = consoleConfig;
    }

    public FileConfig getFileConfig() {
        return fileConfig;
    }

    public void setFileConfig(FileConfig fileConfig) {
        this.fileConfig = fileConfig;
    }

    /**
     * Dump config
     */
    public static class DumpConfig {

        private boolean enable;
        private boolean error;
        private String path;

        public boolean isEnable() {
            return enable;
        }

        public void setEnable(boolean enable) {
            this.enable = enable;
        }

        public boolean isError() {
            return error;
        }

        public void setError(boolean error) {
            this.error = error;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }
    }

    /**
     * log.console config
     */
    public static class ConsoleConfig {
        private boolean enable;

        public boolean isEnable() {
            return enable;
        }

        public void setEnable(boolean enable) {
            this.enable = enable;
        }
    }

    /**
     * log.file config
     */
    public static class FileConfig {
        private boolean enable;
        private String path;

        public boolean isEnable() {
            return enable;
        }

        public void setEnable(boolean enable) {
            this.enable = enable;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }
    }
}
