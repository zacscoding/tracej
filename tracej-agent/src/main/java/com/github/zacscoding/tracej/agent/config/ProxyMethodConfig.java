package com.github.zacscoding.tracej.agent.config;

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

    /**
     * Returns matches or not given a method name.
     */
    public boolean matches(String methodName) {
        if (methodName == null || methodName.isEmpty()) {
            return false;
        }

        switch (filterType) {
            case EQUALS:
                return name.equals(methodName);
            case STARTSWITH:
                return invoker ? name.startsWith(methodName) : methodName.startsWith(name);
            case ENDSWITH:
                return invoker ? name.endsWith(methodName) : methodName.endsWith(name);
            case CONTAINS:
                return invoker ? name.contains(methodName) : methodName.contains(name);
            case REGEX:
                return pattern.matcher(methodName).matches();
            case ALL:
                return true;
            case UNKNOWN:
                return false;
        }

        return false;
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
        return name.equals(that.getName());
    }

    @Override
    public int hashCode() {
        return name.hashCode();
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
