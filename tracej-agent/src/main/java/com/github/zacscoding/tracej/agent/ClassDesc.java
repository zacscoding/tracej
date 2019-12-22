package com.github.zacscoding.tracej.agent;

/**
 * Description of class
 *
 * @GitHub : https://github.com/zacscoding
 */
public class ClassDesc {
    protected int version;
    protected int access;
    protected String name;
    protected String signature;
    protected String superName;
    protected String[] interfaces;
    protected String annotation;
    protected Class classBeingRedefined;
    protected boolean isMapImpl;

    public void setDescriptions(int version, int access, String name, String signature, String superName,
                                String[] interfaces) {
        this.version = version;
        this.access = access;
        this.name = name;
        this.signature = signature;
        this.superName = superName;
        this.interfaces = interfaces;
    }
}
