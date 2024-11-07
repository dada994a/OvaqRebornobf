package devs.pikachu.protect.transformer.impl;

import devs.pikachu.protect.transformer.TransVisitor;
import devs.pikachu.protect.utility.ClassWriter1;
import devs.pikachu.protect.utility.Utils;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class ReverseTransformer extends TransVisitor {
    public ReverseTransformer(ClassReader cr, ClassWriter1 cw, int api) {
        super(cr, cw, api);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        MethodVisitor mv = super.visitMethod(access, name, descriptor, signature, exceptions);
        return new MethodVisitor(api, mv) {
            @Override
            public void visitInsn(int opcode) {
                if (opcode >= Opcodes.ICONST_M1 && opcode <= Opcodes.ICONST_5) {
                    int value = opcode - Opcodes.ICONST_0;
                    String string = Utils.reverse(value);
                    mv.visitLdcInsn(string);
                    mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/String", "hashCode", "()I", false);
                } else {
                    super.visitInsn(opcode);
                }
            }
        };
    }
}