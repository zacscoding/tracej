package com.github.zacscoding.tracej.agent.config;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Proxy class configuration.
 *
 * @GitHub : https://github.com/zacscoding
 */
public class ProxyClassConfig {

    private String name;
    private FilterType filterType;
    private Pattern pattern;
    private boolean invoker;

    private List<ProxyMethodConfig> proxyMethods = new ArrayList<ProxyMethodConfig>();

    public ProxyClassConfig() {
    }

    /**
     * Adds a given {@link ProxyMethodConfig}
     */
    public void addProxyMethod(ProxyMethodConfig config) {
        if (proxyMethods.contains(config)) {
            return;
        }

        proxyMethods.add(config);
    }

    /**
     * Returns matches or not given a class name.
     */
    public boolean matches(String className) {
        if (className == null || className.isEmpty()) {
            return false;
        }

        switch (filterType) {
            case EQUALS:
                return name.equals(className);
            case STARTSWITH:
                return invoker ? name.startsWith(className) : className.startsWith(name);
            case ENDSWITH:
                return invoker ? name.endsWith(className) : className.endsWith(name);
            case CONTAINS:
                return invoker ? name.contains(className) : className.contains(name);
            case REGEX:
                return pattern.matcher(className).matches();
            case ALL:
                // Not supported in class config
                return false;
            case UNKNOWN:
                return false;
        }

        return false;
    }

    public ProxyMethodConfig getProxyMethodConfig(String methodName) {
        for (ProxyMethodConfig methodConfig : proxyMethods) {
            if (methodConfig.matches(methodName)) {
                return methodConfig;
            }
        }

        return null;
    }

    ///////////////////////////////////////
    // getters, setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public FilterType getFilterType() {
        return filterType;
    }

    public void setFilterType(FilterType filterType) {
        this.filterType = filterType;
    }

    public Pattern getPattern() {
        return pattern;
    }

    public void setPattern(Pattern pattern) {
        this.pattern = pattern;
    }

    public boolean isInvoker() {
        return invoker;
    }

    public void setInvoker(boolean invoker) {
        this.invoker = invoker;
    }

    public List<ProxyMethodConfig> getProxyMethods() {
        return proxyMethods;
    }

    public void setProxyMethods(List<ProxyMethodConfig> proxyMethods) {
        this.proxyMethods = proxyMethods;
    }
}
