package com.github.zacscoding.tracej.agent.config;

import java.util.EnumSet;

/**
 * Filter types about classes or methods name
 *
 * @GitHub : https://github.com/zacscoding
 */
public enum FilterType {

    EQUALS,
    STARTSWITH,
    ENDSWITH,
    CONTAINS,
    REGEX,
    ALL,
    UNKNOWN;

    private static EnumSet<FilterType> TYPES = EnumSet.allOf(FilterType.class);

    /**
     * Returns a {@link FilterType} with not case sensitive given name.
     */
    public static FilterType getType(String name) {
        for (FilterType type : TYPES) {
            if (type.name().equalsIgnoreCase(name)) {
                return type;
            }
        }

        return UNKNOWN;
    }
}
