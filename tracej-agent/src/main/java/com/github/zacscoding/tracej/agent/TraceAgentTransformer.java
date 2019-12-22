package com.github.zacscoding.tracej.agent;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;

import com.github.zacscoding.tracej.agent.asm.ProxyClassVisitor;
import com.github.zacscoding.tracej.agent.config.Config;
import com.github.zacscoding.tracej.agent.config.LogConfig.DumpConfig;
import com.github.zacscoding.tracej.agent.config.ProxyClassConfig;
import com.github.zacscoding.tracej.agent.util.DumpUtil;
import com.github.zacscoding.tracej.agent.util.OpcodesUtil;

/**
 * Agent transformer implemented {@link ClassFileTransformer}
 *
 * @GitHub : https://github.com/zacscoding
 */
public class TraceAgentTransformer implements ClassFileTransformer {

    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined,
                            ProtectionDomain protectionDomain, byte[] classfileBuffer)
            throws IllegalClassFormatException {
        if (className == null) {
            return null;
        }

        try {
            // if have a error of configs, then does not transform class
            if (Config.INSTANCE.hasError()) {
                return classfileBuffer;
            }

            // settings class descriptions
            final ClassDesc classDesc = new ClassDesc();
            ClassReader classReader = new ClassReader(classfileBuffer);
            classReader.accept(new ClassVisitor(Opcodes.ASM5) {
                public void visit(int version, int access, String name, String signature, String superName,
                                  String[] interfaces) {
                    classDesc.setDescriptions(version, access, name, signature, superName, interfaces);
                    super.visit(version, access, name, signature, superName, interfaces);
                }

                @Override
                public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
                    classDesc.annotation += desc;
                    return super.visitAnnotation(desc, visible);
                }
            }, 0);

            // skip to transform if interface
            if (OpcodesUtil.isInterface(classDesc.access)) {
                return null;
            }

            ProxyClassConfig classConfig = Config.INSTANCE.getProxyClassConfig(className);
            // if matched class name, then transform class file
            if (classConfig != null) {
                System.err.println("## find target class : " + className);
                ClassWriter cw = new ClassWriter(classReader,
                                                 ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);
                classReader.accept(new ProxyClassVisitor(cw, className, classConfig),
                                   ClassReader.EXPAND_FRAMES);

                byte[] bytes = cw.toByteArray();

                // dump class file
                final DumpConfig dumpConfig = Config.INSTANCE.getLogConfig().getDumpConfig();
                if (dumpConfig.isEnable() && !dumpConfig.isError()) {
                    DumpUtil.writeByteCode(dumpConfig.getPath(), bytes, classDesc.name);
                }

                return bytes;
            }
        } catch (Throwable t) {
            LOGGER.error("failed to transform", t);
        }

        return classfileBuffer;
    }
}
