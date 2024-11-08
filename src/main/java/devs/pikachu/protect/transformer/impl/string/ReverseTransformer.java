package devs.pikachu.protect.transformer.impl.string;

import devs.pikachu.protect.transformer.TransVisitor;
import devs.pikachu.protect.utility.ClassWriter1;
import devs.pikachu.protect.utility.Utils;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.MethodVisitor;

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
                if (opcode >= 2 && opcode <= 8) {
                    int value = opcode - 3;
                    String string = Utils.reverse(value);
                    mv.visitLdcInsn(string);
                    mv.visitMethodInsn(182, "java/lang/String", "hashCode", "()I", false);
                } else {
                    super.visitInsn(opcode);
                }
            }
        };
    }
}