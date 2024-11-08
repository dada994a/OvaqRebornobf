package devs.pikachu.protect.transformer.impl.junk;

import devs.pikachu.protect.Main;
import devs.pikachu.protect.transformer.TransVisitor;
import devs.pikachu.protect.utility.ClassWriter1;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;

import java.util.HashMap;
import java.util.Map;

public class More2JunkCodeTransformer extends TransVisitor {
    public More2JunkCodeTransformer(ClassReader cr, ClassWriter1 cw, int api) {
        super(cr, cw, api);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        if (this.isEx || name.contains("init>")) {
            return super.visitMethod(access, name, descriptor, signature, exceptions);
        }
        if (this.isIn) {
            return new MethodVisitor(this.api, super.visitMethod(access, name, descriptor, signature, exceptions)){
                private final Label constant;
                private final Map<Label, Long> accessSwitchToken;
                private final Map<Label, Integer> key;
                private final Map<Label, Label> gotoLabel;
                private final Map<Label, Label> gotoLabelThrow;
                private int maxVarIndex;
                {
                    this.constant = new Label();
                    this.accessSwitchToken = new HashMap<>();
                    this.key = new HashMap<>();
                    this.gotoLabel = new HashMap<>();
                    this.gotoLabelThrow = new HashMap<>();
                    this.maxVarIndex = 0;
                }

                @Override
                public void visitCode() {
                    ClassReader reader = new ClassReader(More2JunkCodeTransformer.this.cr.b);
                    reader.accept(new ClassVisitor(Main.int1){

                        @Override
                        public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
                            return new MethodVisitor(Main.int1, super.visitMethod(access, name, descriptor, signature, exceptions)){

                                @Override
                                public void visitVarInsn(int opcode, int varIndex) {
                                    maxVarIndex = Math.max(maxVarIndex, varIndex);
                                }
                            };
                        }
                    }, 0);
                    super.visitCode();
                }

                @Override
                public void visitInsn(int opcode) {
                    Integer in = null;
                    if (opcode >= 2 && opcode <= 8) {
                        in = opcode - 3;
                    }
                    if (in != null) {
                        this.visitNumberInt(in);
                    } else {
                        super.visitInsn(opcode);
                    }
                }

                private void visitNumberInt(Integer in) {
                    int key = More2JunkCodeTransformer.this.r.nextInt();
                    long accessSwitchToken = More2JunkCodeTransformer.this.r.nextLong();
                    Label jumpBack = new Label();
                    this.key.put(jumpBack, key);
                    this.accessSwitchToken.put(jumpBack, accessSwitchToken);
                    super.visitLdcInsn(in ^ key);
                    Label gotoL = new Label();
                    this.gotoLabel.put(jumpBack, gotoL);
                    super.visitJumpInsn(167, gotoL);
                    super.visitLabel(jumpBack);
                    this.gotoLabelThrow.put(jumpBack, new Label());
                }

                @Override
                public void visitLdcInsn(Object value) {
                    if (value instanceof Integer) {
                        this.visitNumberInt((Integer)value);
                    } else {
                        super.visitLdcInsn(value);
                    }
                }

                @Override
                public void visitMaxs(int maxStack, int maxLocals) {
                    for (Label jumpLabel : this.accessSwitchToken.keySet().toArray(new Label[0])) {
                        super.visitLabel(this.gotoLabel.get(jumpLabel));
                        super.visitLdcInsn(Type.getType("Ldevs/pikachu/protect/PikachuObf;"));
                        super.visitJumpInsn(198, this.gotoLabelThrow.get(jumpLabel));
                        super.visitLdcInsn(this.key.get(jumpLabel));
                        super.visitInsn(130);
                        super.visitJumpInsn(167, jumpLabel);
                        super.visitLabel(this.gotoLabelThrow.get(jumpLabel));
                        super.visitTypeInsn(187, "exceptions/BadException");
                        super.visitInsn(89);
                        super.visitLdcInsn("SbUwU");
                        super.visitMethodInsn(183, "exceptions/BadException", "<init>", "(Ljava/lang/String;)V", false);
                        super.visitInsn(191);
                        super.visitJumpInsn(167, this.gotoLabelThrow.get(jumpLabel));
                    }
                    super.visitMaxs(maxStack, maxLocals);
                }
            };
        }
        return super.visitMethod(access, name, descriptor, signature, exceptions);
    }
}