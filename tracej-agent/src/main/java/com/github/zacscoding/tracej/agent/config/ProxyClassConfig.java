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
    private boolean isInvoker;

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
        return isInvoker;
    }

    public void setInvoker(boolean invoker) {
        isInvoker = invoker;
    }

    public List<ProxyMethodConfig> getProxyMethods() {
        return proxyMethods;
    }

    public void setProxyMethods(List<ProxyMethodConfig> proxyMethods) {
        this.proxyMethods = proxyMethods;
    }
}
