/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mk.training.dynamic;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.invoke.CallSite;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import org.objectweb.asm.AnnotationVisitor;
import static org.objectweb.asm.Opcodes.*;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.Handle;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

/**
 *
 * @author StillWaterrz
 */
abstract class AbstractDynamicInvokerGenerator implements Opcodes {

    public byte[] dump(String dynamicInvokerClassName, String dynamicLinkageClassName, String bootstrapMethodName, String targetMethodDescriptor)
            throws Exception {

        ClassWriter cw = new ClassWriter(0);
        FieldVisitor fv;
        MethodVisitor mv;
        AnnotationVisitor av0;

        cw.visit(V1_7, ACC_PUBLIC + ACC_SUPER, dynamicInvokerClassName, null, "java/lang/Object", null);

        {
            mv = cw.visitMethod(ACC_PUBLIC, "<init>", "()V", null, null);
            mv.visitCode();
            mv.visitVarInsn(ALOAD, 0);
            mv.visitMethodInsn(INVOKESPECIAL, "java/lang/Object", "<init>", "()V");
            mv.visitInsn(RETURN);
            mv.visitMaxs(1, 1);
            mv.visitEnd();
        }
        {
            mv = cw.visitMethod(ACC_PUBLIC + ACC_STATIC, "main", "([Ljava/lang/String;)V", null, null);
            mv.visitCode();
            MethodType mt = MethodType.methodType(CallSite.class, MethodHandles.Lookup.class, String.class,
                    MethodType.class);
            Handle bootstrap = new Handle(Opcodes.H_INVOKESTATIC, dynamicLinkageClassName, bootstrapMethodName,
                    mt.toMethodDescriptorString());
            int maxStackSize = addMethodParameters(mv);
            mv.visitInvokeDynamicInsn("foo", targetMethodDescriptor, bootstrap);
            mv.visitInsn(RETURN);
            mv.visitMaxs(maxStackSize, 1);
            mv.visitEnd();
        }
        cw.visitEnd();

        return cw.toByteArray();
    }

    protected abstract int addMethodParameters(MethodVisitor mv);
}

public class SimpleDynamicInvokerGenerator extends AbstractDynamicInvokerGenerator {

    @Override
    protected int addMethodParameters(MethodVisitor mv) {
        return 0;
    }

    public static void main(String[] args) throws IOException, Exception {
        String dynamicInvokerClassName = "org/mk/training/dynamic/SimpleDynamicInvoker";
        FileOutputStream fos = new FileOutputStream("target/classes/" + dynamicInvokerClassName + ".class");
        fos.write(new SimpleDynamicInvokerGenerator().dump(dynamicInvokerClassName, "org/mk/training/dynamic/SimpleDynamicLinkageExample", 
                "bootstrapDynamic", "()V"));
       
    }
}
