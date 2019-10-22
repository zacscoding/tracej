package com.github.zacscoding.tracej.agent.asm;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.commons.LocalVariablesSorter;

import com.github.zacscoding.tracej.agent.config.ProxyMethodConfig;

/**
 * @GitHub : https://github.com/zacscoding
 */
public class ProxyMethodVisitor extends LocalVariablesSorter implements Opcodes {

    private String desc;
    private String methodId;
    private ProxyMethodConfig methodConfig;

    protected ProxyMethodVisitor(int access, String desc, MethodVisitor mv, String methodId,
                                 ProxyMethodConfig methodConfig) {
        super(ASM5, access, desc, mv);
        this.desc = desc;
        this.methodId = methodId;
        this.methodConfig = methodConfig;
    }
}
