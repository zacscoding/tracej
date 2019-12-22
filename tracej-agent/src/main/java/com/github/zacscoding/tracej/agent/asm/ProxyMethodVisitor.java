package com.github.zacscoding.tracej.agent.asm;

import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.LocalVariablesSorter;

import com.github.zacscoding.tracej.agent.config.ProxyMethodConfig;
import com.github.zacscoding.tracej.agent.trace.TransactionStack;
import com.github.zacscoding.tracej.agent.util.OpcodesUtil;

/**
 * @GitHub : https://github.com/zacscoding
 */
public class ProxyMethodVisitor extends LocalVariablesSorter implements Opcodes {

    private Label startFinally = new Label();

    private String desc;
    private String methodId;
    private ProxyMethodConfig methodConfig;

    private String trace = TransactionStack.class.getName().replace('.', '/');

    protected ProxyMethodVisitor(int access, String desc, MethodVisitor mv, String methodId,
                                 ProxyMethodConfig methodConfig) {
        super(ASM5, access, desc, mv);
        this.desc = desc;
        this.methodId = methodId;
        this.methodConfig = methodConfig;
    }

    @Override
    public void visitCode() {
        // added code : TransactionStack.pushTransaction(methodId)
        mv.visitLdcInsn(methodId);
        mv.visitMethodInsn(INVOKESTATIC, trace, "pushTransaction", "(Ljava/lang/String;)V", false);
        addParameter();

        // added code "try {" block
        mv.visitLabel(startFinally);
        mv.visitCode();
    }

    @Override
    public void visitInsn(int opcode) {
        if ((opcode >= IRETURN && opcode <= RETURN)) {
            if (opcode == RETURN) {
                mv.visitLdcInsn("void");
            } else {
                mv.visitInsn(DUP);
                Type returnType = Type.getReturnType(desc);
                String wrapper = OpcodesUtil.getWrapperClass(returnType.getSort());
                if (wrapper != null) {
                    mv.visitMethodInsn(INVOKESTATIC, wrapper, "valueOf",
                                       "(" + returnType.getDescriptor() + ")L" + wrapper + ";",
                                       false);
                }
            }
            // added code : TransactionStack.appendReturnValue(returnValue)
            mv.visitMethodInsn(Opcodes.INVOKESTATIC, trace,
                               "appendReturnValue",
                               "(Ljava/lang/Object;)V",
                               false);

            // added code : TransactionStack.popTransaction()
            mv.visitMethodInsn(Opcodes.INVOKESTATIC, trace,
                               "popTransaction",
                               "()V", false);
        }
        mv.visitInsn(opcode);
    }

    @Override
    public void visitMaxs(int maxStack, int maxLocals) {
        // added code : catch(Throwable t) {
        Label labelCatch = new Label();
        mv.visitTryCatchBlock(startFinally, labelCatch, labelCatch, null);
        mv.visitLabel(labelCatch);

        // copy top from stack
        mv.visitInsn(DUP);
        int errIdx = newLocal(Type.getType(Throwable.class));
        mv.visitVarInsn(Opcodes.ASTORE, errIdx);
        mv.visitVarInsn(Opcodes.ALOAD, errIdx);
        // added code : TransactionStack.appendReturnValue(returnValue)
        mv.visitMethodInsn(Opcodes.INVOKESTATIC, trace,
                           "appendException",
                           "(Ljava/lang/Throwable;)V",
                           false);

        // added code : TransactionStack.popTransaction()
        mv.visitMethodInsn(Opcodes.INVOKESTATIC, trace,
                           "popTransaction",
                           "()V", false);

        // added code : throw t
        mv.visitInsn(ATHROW);

        mv.visitMaxs(maxStack, maxLocals + 2);
    }

    private void addParameter() {
        Type[] params = Type.getArgumentTypes(desc);
        if (params.length > 0) {
            for (int i = 0; i < params.length; i++) {
                int idx = i + 1;
                String wrapper = OpcodesUtil.getWrapperClass(params[i].getSort());
                if (wrapper == null) {
                    mv.visitVarInsn(Opcodes.ALOAD, idx);
                } else {
                    int opcodes = OpcodesUtil.getLoadOrStore(params[i].getSort(), true);
                    mv.visitVarInsn(opcodes, idx);
                    mv.visitMethodInsn(INVOKESTATIC, wrapper, "valueOf",
                                       "(" + params[i].getDescriptor() + ")L" + wrapper + ";", false);
                }

                mv.visitMethodInsn(INVOKESTATIC, trace, "appendParam", "(Ljava/lang/Object;)V", false);
            }
        }
    }
}
