package com.github.zacscoding.tracej.agent.asm;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import com.github.zacscoding.tracej.agent.config.ProxyClassConfig;
import com.github.zacscoding.tracej.agent.config.ProxyMethodConfig;
import com.github.zacscoding.tracej.agent.util.StringUtil;

/**
 * Proxy class visitor
 *
 * @GitHub : https://github.com/zacscoding
 */
public class ProxyClassVisitor extends ClassVisitor implements Opcodes {

    private ProxyClassConfig classConfig;
    private String className;

    public ProxyClassVisitor(ClassVisitor cv, String className, ProxyClassConfig classConfig) {
        super(ASM5, cv);
        this.classConfig = classConfig;
        this.className = className;
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String desc,
                                     String signature, String[] exceptions) {
        MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions);

        if (mv == null) {
            return mv;
        }

        if ("<init>".equals(name)) {
            return mv;
        }

        if ("<clinit>".equals(name)) {
            return mv;
        }

        ProxyMethodConfig methodConfig = classConfig.getProxyMethodConfig(name);

        if (methodConfig == null) {
            return mv;
        }

        return new ProxyMethodVisitor(access, desc, mv, getMethodId(className, name, desc), methodConfig);
    }

    /**
     * Returns a method's unique id "{className}::{methodName}{methodDesc}"
     */
    private String getMethodId(String className, String methodName, String methodDesc) {
        String trimClassName = StringUtil.trimClassName(className);
        return trimClassName + "::" + methodName + methodDesc;
    }
}
