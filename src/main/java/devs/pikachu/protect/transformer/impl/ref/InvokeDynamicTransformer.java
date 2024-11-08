package devs.pikachu.protect.transformer.impl.ref;

import devs.pikachu.protect.Main;
import devs.pikachu.protect.transformer.TransVisitor;
import devs.pikachu.protect.utility.ClassWriter1;
import org.objectweb.asm.Attribute;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.Handle;
import org.objectweb.asm.MethodVisitor;

public class InvokeDynamicTransformer extends TransVisitor {
    public InvokeDynamicTransformer(ClassReader cr, ClassWriter1 cw, int api) {
        super(cr, cw, api);
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        if ((access | 0x1000) == access || (access | 0x4000) == access) {
            this.isEx = true;
        }
        super.visit(version, access, name, signature, superName, interfaces);
    }

    @Override
    public void visitAttribute(Attribute var1) {
        super.visitAttribute(var1);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        if (this.isEx) {
            return super.visitMethod(access, name, descriptor, signature, exceptions);
        }
        if (this.isIn) {
            return new MethodVisitor(this.api, super.visitMethod(access, name, descriptor, signature, exceptions)){

                @Override
                public void visitMethodInsn(int opcode, String owner, String name, String descriptor, boolean isInterface) {
                    int i;
                    int key2;
                    int key = key2 = InvokeDynamicTransformer.this.r.nextInt();
                    char[] charKey = new char[key % 10 + 10];
                    for (i = 0; i < charKey.length; ++i) {
                        key += key * (key >> 5) + (key << 5) + 114514;
                        charKey[i] = (char)(key % 65536);
                    }
                    char[] charOwner = owner.replace("/", ".").toCharArray();
                    char[] charName = name.toCharArray();
                    char[] charDescriptor = descriptor.toCharArray();
                    for (i = 0; i < charOwner.length; ++i) {
                        charOwner[i] = (char)(charOwner[i] ^ charKey[i % charKey.length]);
                    }
                    for (i = 0; i < charName.length; ++i) {
                        charName[i] = (char)(charName[i] ^ charKey[i % charKey.length]);
                    }
                    for (i = 0; i < charDescriptor.length; ++i) {
                        charDescriptor[i] = (char)(charDescriptor[i] ^ charKey[i % charKey.length]);
                    }
                    switch (opcode) {
                        case 184: {
                            if (!Main.dontEncode) {
                                super.visitInvokeDynamicInsn("devs/pikachu/protect/PikachuObf", descriptor, new Handle(6, "devs/pikachu/protect/PikachuObf", "BROZOZOTOWN", "(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;", false), "SbUwU", key2, new String(charOwner), new String(charName), new String(charDescriptor));
                                break;
                            }
                            super.visitInvokeDynamicInsn("devs/pikachu/protect/PikachuObf", descriptor, new Handle(6, "devs/pikachu/protect/PikachuObf", "BROZOZOTOWN", "(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;", false), "SbUwU", owner, name, descriptor);
                            break;
                        }
                        case 182: 
                        case 185: {
                            if (!descriptor.contains("[L")) {
                                if (!Main.dontEncode) {
                                    super.visitInvokeDynamicInsn("devs/pikachu/protect/PikachuObf", descriptor.replace("(", "(Ljava/lang/Object;"), new Handle(6, "devs/pikachu/protect/PikachuObf", "BROZOZOTOWN", "(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;", true), "Bakaa", key2, new String(charOwner), new String(charName), new String(charDescriptor));
                                    break;
                                }
                                super.visitInvokeDynamicInsn("devs/pikachu/protect/PikachuObf", descriptor.replace("(", "(Ljava/lang/Object;"), new Handle(6, "devs/pikachu/protect/PikachuObf", "BROZOZOTOWN", "(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;", true), "Bakaa", owner, name, descriptor);
                                break;
                            }
                            super.visitMethodInsn(opcode, owner.replace(".", "/"), name, descriptor, isInterface);
                            break;
                        }
                        default: {
                            super.visitMethodInsn(opcode, owner, name, descriptor, isInterface);
                        }
                    }
                }
            };
        }
        return super.visitMethod(access, name, descriptor, signature, exceptions);
    }
}