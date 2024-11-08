package devs.pikachu.protect.transformer.impl.renamer;

import devs.pikachu.protect.Main;
import devs.pikachu.protect.transformer.TransVisitor;
import devs.pikachu.protect.utility.ClassWriter1;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.MethodVisitor;

//dev
public class PikachuStrReplacerObfVisitor extends TransVisitor {
    public PikachuStrReplacerObfVisitor(ClassReader cr, ClassWriter1 cw, int api) {
        super(cr, cw, api);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        if (this.isIn && !this.isEx) {
            return new MethodVisitor(this.api, super.visitMethod(access, name, descriptor, signature, exceptions)){

                @Override
                public void visitLdcInsn(Object value) {
                    if (value instanceof String) {
                        int i = -1;
                        switch (value.toString()) {
                            case "Example1": {
                                i = 0;
                                break;
                            }
                            case "Example2": {
                                i = 1;
                                break;
                            }
                            case "Example3": {
                                i = 2;
                            }
                        }
                        if (i != -1) {
                            super.visitLdcInsn(Main.pikachuStrs[i]);
                        } else {
                            super.visitLdcInsn(value);
                        }
                    } else {
                        super.visitLdcInsn(value);
                    }
                }
            };
        }
        return super.visitMethod(access, name, descriptor, signature, exceptions);
    }
}