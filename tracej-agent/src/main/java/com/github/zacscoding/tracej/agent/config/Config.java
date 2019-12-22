package com.github.zacscoding.tracej.agent.config;

import java.io.File;
import java.util.regex.Pattern;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import com.github.zacscoding.tracej.agent.LOGGER;
import com.github.zacscoding.tracej.agent.config.LogConfig.ConsoleConfig;
import com.github.zacscoding.tracej.agent.config.LogConfig.DumpConfig;
import com.github.zacscoding.tracej.agent.config.LogConfig.FileConfig;

/**
 * Agent configuration.
 *
 * @GitHub : https://github.com/zacscoding
 */
public class Config {

    public static final Config INSTANCE = new Config();

    // config path key in vm args
    private static final String CONFIG_PATH = "tracej.config.path";

    private YAMLMapper yamlMapper;

    private boolean initialized;
    private boolean hasError;

    private ProxyConfig proxy;
    private LogConfig log;

    /**
     * Returns a {@link ProxyClassConfig} given class name
     *
     * @return {@link ProxyClassConfig} if matches given class name or null
     */
    public ProxyClassConfig getProxyClassConfig(String className) {
        ProxyClassConfig config = null;

        for (ProxyClassConfig classConfig : proxy.getProxyClasses()) {
            if (classConfig.matches(className)) {
                if (config != null) {
                    LOGGER.error("found multiple matched proxy class config. class name : " + className);
                    continue;
                }
                config = classConfig;
            }
        }

        return config;
    }

    /**
     * Returns a boolean whether has error nor not
     */
    public boolean hasError() {
        return hasError;
    }

    /**
     * Settings error to true
     */
    public void setError(boolean hasError) {
        this.hasError = hasError;
    }

    /**
     * Returns a {@link LogConfig}
     */
    public LogConfig getLogConfig() {
        return log;
    }

    // for tests
    ProxyConfig getProxy() {
        return proxy;
    }

    ///////////////////////////////////////
    // private methods
    private Config() {
        initialize();
    }

    private void initialize() {
        if (initialized) {
            return;
        }

        YAMLFactory yamlFactory = new YAMLFactory()
                .configure(YAMLGenerator.Feature.USE_NATIVE_TYPE_ID, false)
                .configure(YAMLGenerator.Feature.WRITE_DOC_START_MARKER, false)
                .configure(YAMLGenerator.Feature.MINIMIZE_QUOTES, true)
                .configure(YAMLGenerator.Feature.INDENT_ARRAYS, true);

        yamlMapper = new YAMLMapper(yamlFactory);
        yamlMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        try {
            String configPath = System.getProperty(CONFIG_PATH);

            if (configPath == null) {
                throw new Exception("empty config path in args about key : " + CONFIG_PATH);
            }

            File configFile = new File(configPath);

            if (!configFile.exists()) {
                throw new Exception("cannot find config file. path : " + configPath);
            }

            parseConfig(configFile);
            initialized = true;
        } catch (Exception e) {
            System.err.println("failed to initialize config.");
            e.printStackTrace(System.err);
            initialized = true;
            hasError = true;
        }
    }

    private void parseConfig(File configFile) throws Exception {
        JsonNode rootNode = yamlMapper.readTree(configFile);

        // parse proxy section
        if (!rootNode.has("proxy")) {
            throw new Exception("Skip to trace because empty proxy configs");
        }
        proxy = parseProxyConfig(rootNode.get("proxy"));

        // parse log section
        log = parseLogConfig(rootNode.get("log"));
    }

    private ProxyConfig parseProxyConfig(JsonNode proxyNode) throws Exception {
        ProxyConfig proxyConfig = new ProxyConfig();

        if (proxyNode.has("classes")) {
            ArrayNode classesNodes = (ArrayNode) proxyNode.get("classes");

            for (int i = 0; i < classesNodes.size(); i++) {
                JsonNode classNode = classesNodes.get(i);
                proxyConfig.addProxyClassConfig(parseProxyClassConfig(classNode));
            }
        }

        return proxyConfig;
    }

    /**
     * Parse class config from given {@link JsonNode}
     */
    private ProxyClassConfig parseProxyClassConfig(JsonNode classNode) throws Exception {
        final ProxyClassConfig config = new ProxyClassConfig();

        final String name = classNode.has("name") ? classNode.get("name").asText() : "";
        final String regex = classNode.has("pattern") ? classNode.get("pattern").asText() : "";
        final FilterType filterType = FilterType.getType(classNode.get("filtertype").asText());
        final boolean invoker = classNode.get("invoker").asBoolean();

        if (filterType == FilterType.UNKNOWN) {
            throw new Exception("Unknown filter type : " + classNode.get("filtertype").asText());
        }

        if (filterType == FilterType.REGEX) {
            if (regex.isEmpty()) {
                throw new Exception(
                        String.format("filter type is regex but invalid regex expression. class %s, regex %s."
                                , name, regex)
                );
            }

            config.setPattern(Pattern.compile(regex));
        }

        config.setName(name);
        config.setFilterType(filterType);
        config.setInvoker(invoker);

        final ArrayNode methodNodes = (ArrayNode) classNode.get("methods");

        if (methodNodes.size() < 1) {
            throw new Exception("must have at least one method type in class config.");
        }

        for (int i = 0; i < methodNodes.size(); i++) {
            JsonNode methodNode = methodNodes.get(i);
            config.addProxyMethod(parseProxyMethodConfig(methodNode));
        }

        return config;
    }

    /**
     * Parse method config from given {@link JsonNode}
     */
    private ProxyMethodConfig parseProxyMethodConfig(JsonNode methodNode) throws Exception {
        final ProxyMethodConfig config = new ProxyMethodConfig();

        final String name = methodNode.has("name") ? methodNode.get("name").asText() : "";
        final String regex = methodNode.has("pattern") ? methodNode.get("pattern").asText() : "";
        final boolean invoker = methodNode.get("invoker").asBoolean();
        final FilterType filterType = "*".equals(name) ? FilterType.ALL :
                                      FilterType.getType(methodNode.get("filtertype").asText());

        if (filterType == FilterType.UNKNOWN) {
            throw new Exception("unknown filter type : " + methodNode.get("filtertype").asText());
        }

        if (filterType == FilterType.REGEX) {
            if (regex.isEmpty()) {
                throw new Exception("filter type is regex but invalid regex expression.");
            }

            config.setPattern(Pattern.compile(regex));
        }

        config.setName(name);
        config.setFilterType(filterType);
        config.setInvoker(invoker);

        return config;
    }

    /**
     * Parse log config from given {@link JsonNode}
     */
    private LogConfig parseLogConfig(JsonNode logNode) {
        final LogConfig config = new LogConfig();

        config.setDumpConfig(new DumpConfig());
        config.setConsoleConfig(new ConsoleConfig());
        config.setFileConfig(new FileConfig());

        final JsonNode dumpNode = findNode(logNode, new String[] { "dump" });
        if (dumpNode != null) {
            boolean enable = false;

            if (dumpNode.has("enable")) {
                enable = dumpNode.get("enable").asBoolean();
            }

            config.getDumpConfig().setEnable(enable);

            if (enable) {
                config.getDumpConfig().setPath(dumpNode.get("path").asText());
                try {
                    setUpDumpClassDirectory(config.getDumpConfig().getPath());
                } catch (Exception e) {
                    config.getDumpConfig().setError(true);
                }
            }
        }

        final JsonNode consoleNode = findNode(logNode, new String[] { "console" });
        if (consoleNode != null) {
            boolean enable = false;

            if (consoleNode.has("enable")) {
                enable = consoleNode.get("enable").asBoolean();
            }

            config.getConsoleConfig().setEnable(enable);
        }

        final JsonNode fileNode = findNode(logNode, new String[] { "file" });
        if (fileNode != null) {
            boolean enable = false;

            if (fileNode.has("enable")) {
                enable = fileNode.get("enable").asBoolean();
            }

            config.getFileConfig().setEnable(enable);

            if (enable) {
                config.getFileConfig().setPath(fileNode.get("path").asText());
            }
        }

        return config;
    }

    /**
     * Find a {@link JsonNode} given key array.
     *
     * @return json node if exist, otherwise null
     */
    private JsonNode findNode(JsonNode rootNode, String[] keys) {
        if (rootNode == null || keys == null || keys.length == 0) {
            return null;
        }

        JsonNode currentNode = rootNode;

        for (String key : keys) {
            currentNode = currentNode.get(key);

            if (currentNode == null) {
                break;
            }
        }

        return currentNode;
    }

    private void setUpDumpClassDirectory(String path) {
        try {
            File rootDir = new File(path);
            if (!rootDir.canWrite()) {
                rootDir.mkdirs();
            }

            File[] prevs = rootDir.listFiles();
            for (File file : prevs) {
                if (".idea".equals(file.getName())) {
                    continue;
                }

                if (file.isFile()) {
                    file.delete();
                } else {
                    deleteAll(file);
                }
            }

            if (!rootDir.canWrite()) {
                rootDir.mkdirs();
            }
            if (!rootDir.canWrite()) {
                hasError = true;
            }
        } catch (Exception e) {
            hasError = true;
        }
    }

    private void deleteAll(File dir) {
        if (dir == null) {
            return;
        }

        File[] files = dir.listFiles();
        for (File file : files) {
            if (!file.isFile()) {
                deleteAll(file);
            }

            file.delete();
        }
        dir.delete();
    }

    // for tests
    void reload(String configPath) {
        hasError = false;
        initialized = false;
        System.setProperty(CONFIG_PATH, configPath);
        initialize();
    }
}
