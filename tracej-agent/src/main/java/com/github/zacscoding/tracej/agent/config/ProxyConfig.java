package com.github.zacscoding.tracej.agent.config;

import java.util.ArrayList;
import java.util.List;

/**
 * Proxy configuration
 *
 * @GitHub : https://github.com/zacscoding
 */
public class ProxyConfig {

    private List<ProxyClassConfig> proxyClasses;

    public ProxyConfig() {
        proxyClasses = new ArrayList<ProxyClassConfig>();
    }

    public ProxyConfig(List<ProxyClassConfig> proxyClasses) {
        this.proxyClasses = proxyClasses;
    }

    /**
     * Adds a {@link ProxyClassConfig} if not null
     */
    public void addProxyClassConfig(ProxyClassConfig config) {
        if (config != null) {
            proxyClasses.add(config);
        }
    }
}
