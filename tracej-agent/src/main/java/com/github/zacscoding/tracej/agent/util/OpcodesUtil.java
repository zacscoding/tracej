package com.github.zacscoding.tracej.agent.util;

import org.objectweb.asm.Opcodes;

/**
 *
 * @GitHub : https://github.com/zacscoding
 */
public class OpcodesUtil {

    /**
     * Checks interface or not given access flags
     */
    public static boolean isInterface(int access) {
        return (access & Opcodes.ACC_INTERFACE) != 0;
    }

    private OpcodesUtil() {
    }
}
