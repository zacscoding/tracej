package com.github.zacscoding.tracej.agent.config;

import java.io.File;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;

/**
 * Agent configuration.
 *
 * @GitHub : https://github.com/zacscoding
 */
public class Config {

    public static final Config INSTANCE = new Config();

    private static final YAMLMapper DEFAULT_YAML_MAPPER;
    // config path key in vm args
    private static final String CONFIG_PATH = "tracej.config.path";

    private boolean initialized = false;
    private boolean hasError;
    private ProxyConfig proxyConfig;

    static {
        YAMLFactory yamlFactory = new YAMLFactory()
                .configure(YAMLGenerator.Feature.USE_NATIVE_TYPE_ID, false)
                .configure(YAMLGenerator.Feature.WRITE_DOC_START_MARKER, false)
                .configure(YAMLGenerator.Feature.MINIMIZE_QUOTES, true)
                .configure(YAMLGenerator.Feature.INDENT_ARRAYS, true);

        DEFAULT_YAML_MAPPER = new YAMLMapper(yamlFactory);
        DEFAULT_YAML_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public boolean hasError() {
        return hasError;
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
        } catch (Exception e) {
            System.err.println("failed to initialize config. reason : " + e.getMessage());
            hasError = true;
        }
    }

    private void parseConfig(File configFile) throws Exception {
        JsonNode rootNode = DEFAULT_YAML_MAPPER.readTree(configFile);
        if (rootNode.has("proxy")) {
            proxyConfig = parseProxyConfig(rootNode.get("proxy"));
        }
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

    private ProxyClassConfig parseProxyClassConfig(JsonNode classNode) throws Exception {
        ProxyClassConfig config = new ProxyClassConfig();
        return config;
    }

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

    // for tests
    void reload() {
        hasError = false;
        initialized = false;
        initialize();
    }
}
