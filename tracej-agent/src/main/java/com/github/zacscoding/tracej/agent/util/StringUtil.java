package com.github.zacscoding.tracej.agent.util;

/**
 *
 */
public class StringUtil {

    /**
     * Trim class name
     *
     * - origin : com/github/zacscoding/tracej/agent/util/StringUtil
     * - replaced : c.g.z.t.a.u.StringUtil
     */
    public static String trimClassName(String className) {
        return trimClassName(className, '/', '.');
    }

    public static String trimClassName(String className, char separator, char replacedSeparator) {
        int pos = 0;
        int separatorPos;

        StringBuilder result = new StringBuilder();

        // change package name
        while ((separatorPos = className.indexOf(separator, pos)) != -1) {
            result.append(className.charAt(pos)).append(replacedSeparator);
            pos = separatorPos + 1;
        }

        for (int i = pos; i < className.length(); i++) {
            result.append(className.charAt(i));
        }

        return result.toString();
    }

    private StringUtil() {
    }
}
