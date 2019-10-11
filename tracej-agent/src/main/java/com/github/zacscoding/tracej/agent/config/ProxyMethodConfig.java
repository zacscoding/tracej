package com.github.zacscoding.tracej.agent.config;

import java.util.Objects;
import java.util.regex.Pattern;

/**
 *
 * @GitHub : https://github.com/zacscoding
 */
public class ProxyMethodConfig {

    private String name;
    private FilterType filterType;
    private boolean invoker;
    private Pattern pattern;

    public ProxyMethodConfig() {
    }

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

    public boolean isInvoker() {
        return invoker;
    }

    public void setInvoker(boolean invoker) {
        this.invoker = invoker;
    }

    public Pattern getPattern() {
        return pattern;
    }

    public void setPattern(Pattern pattern) {
        this.pattern = pattern;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) { return true; }
        if (!(o instanceof ProxyMethodConfig)) { return false; }
        final ProxyMethodConfig that = (ProxyMethodConfig) o;
        return Objects.equals(getName(), that.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName());
    }

    @Override
    public String toString() {
        return "ProxyMethodConfig{" +
               "name='" + name + '\'' +
               ", filterType=" + filterType +
               ", invoker=" + invoker +
               '}';
    }
}
