package devs.pikachu.protect.transformer.impl.number;

import devs.pikachu.protect.Main;
import devs.pikachu.protect.transformer.TransVisitor;
import devs.pikachu.protect.utility.ClassWriter1;
import devs.pikachu.protect.utility.Utils;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;

import java.util.Iterator;

public class StringNumberTransformer extends TransVisitor {
    private final boolean visitLongLCMP;
    public int randomFlag = Utils.r.nextInt();
    public boolean isClassNameContains;

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        super.visit(version, access, name, signature, superName, interfaces);
        this.isClassNameContains = name.contains("$");
    }

    public StringNumberTransformer(ClassReader cr, ClassWriter1 cw, int api, boolean visitLongLCMP) {
        super(cr, cw, api);
        this.visitLongLCMP = visitLongLCMP;
    }

    @Override
    public MethodVisitor visitMethod(int access, final String name, String descriptor, String signature, String[] exceptions) {
        if (this.isEx || name.equals("Kami")) {
            return super.visitMethod(access, name, descriptor, signature, exceptions);
        }
        if (this.isIn) {
            return new MethodVisitor(this.api, super.visitMethod(access, name, descriptor, signature, exceptions)){
                private int negNumberVisited;
                int junkCount;
                boolean isClinit;
                {
                    this.junkCount = 0;
                    this.isClinit = name.contains("init>");
                }

                @Override
                public void visitIincInsn(int varIndex, int increment) {
                    this.addJunkLabelThrow();
                    super.visitIincInsn(varIndex, increment);
                }

                @Override
                public void visitMaxs(int maxStack, int maxLocals) {
                    Iterator it = StringNumberTransformer.this.invaildLabel.iterator();
                    while (it.hasNext()) {
                        Label label = (Label)it.next();
                        it.remove();
                        this.mv.visitLabel(label);
                        super.visitTypeInsn(187, "exceptions/BadException");
                        super.visitInsn(89);
                        super.visitLdcInsn("SbUwU");
                        super.visitMethodInsn(183, "exceptions/BadException", "<init>", "(Ljava/lang/String;)V", false);
                        super.visitInsn(191);
                    }
                    StringNumberTransformer.this.invaildLabel.clear();
                    super.visitTypeInsn(187, "exceptions/BadException");
                    super.visitInsn(89);
                    super.visitLdcInsn("SbUwU");
                    super.visitMethodInsn(183, "exceptions/BadException", "<init>", "(Ljava/lang/String;)V", false);
                    super.visitInsn(191);
                    super.visitMaxs(maxStack, maxLocals);
                }

                private void visitNumberLong(long l) {
                    int j;
                    long T1_1 = StringNumberTransformer.this.r.nextLong();
                    long T1_2 = l ^ T1_1;
                    long T2_1 = StringNumberTransformer.this.r.nextLong();
                    long T2_2 = T1_1 ^ T2_1;
                    super.visitLdcInsn(T2_1);
                    for (j = 0; j < StringNumberTransformer.this.r.nextInt(5) + (this.isClinit || !Main.bigBrainNumberObf ? -1000 : 1); ++j) {
                        super.visitInsn(117);
                        super.visitInsn(117);
                    }
                    super.visitLdcInsn(T2_2);
                    for (j = 0; j < StringNumberTransformer.this.r.nextInt(5) + (this.isClinit || !Main.bigBrainNumberObf ? -1000 : 1); ++j) {
                        super.visitInsn(117);
                        super.visitInsn(117);
                    }
                    super.visitLdcInsn(T1_2 ^ 0xFFFFFFFFFFFFFFFFL);
                    for (j = 0; j < StringNumberTransformer.this.r.nextInt(5) + (this.isClinit || !Main.bigBrainNumberObf ? -1000 : 1); ++j) {
                        super.visitInsn(117);
                        super.visitInsn(117);
                    }
                    super.visitMethodInsn(184, "devs/pikachu/protect/PikachuObf", "Kami", "(JJJ)J", false);
                    for (j = 0; j < (this.isClinit || !Main.bigBrainNumberObf ? -1000 : 1); ++j) {
                        super.visitInsn(117);
                        super.visitInsn(117);
                    }
                }

                private void visitNumberInt(int i) {
                    if (i != 0 || !StringNumberTransformer.this.visitLongLCMP) {
                        int T1_1 = StringNumberTransformer.this.r.nextInt();
                        int T1_2 = i ^ T1_1;
                        int T2_1 = StringNumberTransformer.this.r.nextInt();
                        int T2_2 = T1_1 ^ T2_1;
                        super.visitLdcInsn(T2_1);
                        this.visitNegNumber(StringNumberTransformer.this.r.nextInt(5) + (this.isClinit || !Main.bigBrainNumberObf ? -1000 : 1));
                        super.visitLdcInsn(T2_2);
                        this.visitNegNumber(StringNumberTransformer.this.r.nextInt(5) + (this.isClinit || !Main.bigBrainNumberObf ? -1000 : 1));
                        super.visitLdcInsn(~T1_2);
                        this.visitNegNumber(StringNumberTransformer.this.r.nextInt(5) + (this.isClinit || !Main.bigBrainNumberObf ? -1000 : 1));
                        super.visitMethodInsn(184, "devs/pikachu/protect/PikachuObf", "Kami", "(III)I", false);
                    } else {
                        long l1 = StringNumberTransformer.this.r.nextLong();
                        this.visitNumberLong(l1);
                        long l2 = StringNumberTransformer.this.r.nextLong();
                        this.visitNumberLong(l2);
                        super.visitInsn(148);
                        if (l1 > l2) {
                            this.visitNumberInt(-1);
                        } else if (l1 < l2) {
                            this.visitNumberInt(1);
                        }
                        if (l1 != l2) {
                            super.visitInsn(96);
                        }
                    }
                }

                private void visitString(String value, boolean flag) {
                    if (!Main.useStringObf) {
                        super.visitLdcInsn(value);
                        return;
                    }
                    String value1 = value;
                    String key = Utils.spawnRandomChar(1, false);
                    char keychar = key.charAt(0);
                    char[] chars = value1.toCharArray();
                    for (int i = 0; i < chars.length; ++i) {
                        chars[i] = (char)(chars[i] ^ keychar * (i + 1));
                    }
                    super.visitLdcInsn(key + new String(chars));
                    super.visitMethodInsn(184, "devs/pikachu/protect/PikachuObf", "Kami", "(Ljava/lang/Object;)Ljava/lang/String;", false);
                    if (flag) {
                        this.addJunkLabelThrow();
                    }
                }

                private void addJunkLabelThrow() {
                    if (this.isClinit && StringNumberTransformer.this.r.nextBoolean()) {
                        return;
                    }
                    if (!Main.useJunkCode) {
                        return;
                    }
                    Label labelIf = new Label();
                    int token = StringNumberTransformer.this.r.nextInt();
                    this.visitLdcInsn(token);
                    if (token > 0) {
                        if (!StringNumberTransformer.this.r.nextBoolean() || !Main.useMoreJunkCode) {
                            this.visitInsn(3);
                            super.visitJumpInsn(162, labelIf);
                            super.visitTypeInsn(187, "exceptions/BadException");
                            super.visitInsn(89);
                            super.visitLdcInsn("SbUwU");
                            super.visitMethodInsn(183, "exceptions/BadException", "<init>", "(Ljava/lang/String;)V", false);
                            super.visitInsn(191);
                            this.mv.visitLabel(labelIf);
                        } else {
                            Label templ = new Label();
                            Label l = new Label();
                            Label l2 = new Label();
                            Label l3 = new Label();
                            Label l4 = new Label();
                            super.visitJumpInsn(167, templ);
                            super.visitLabel(l);
                            token = StringNumberTransformer.this.r.nextInt();
                            this.visitLdcInsn(token);
                            this.visitInsn(3);
                            super.visitJumpInsn(token > 0 ? 161 : 162, l);
                            super.visitTypeInsn(187, "exceptions/BadException");
                            this.visitLdcInsn(token);
                            this.visitInsn(3);
                            super.visitJumpInsn(token > 0 ? 161 : 162, l2);
                            super.visitInsn(89);
                            super.visitLdcInsn("SbUwU");
                            super.visitMethodInsn(183, "exceptions/BadException", "<init>", "(Ljava/lang/String;)V", false);
                            super.visitInsn(191);
                            super.visitLabel(l2);
                            this.visitInsn(87);
                            super.visitJumpInsn(167, l3);
                            super.visitLabel(templ);
                            this.visitInsn(3);
                            super.visitJumpInsn(161, l);
                            super.visitLabel(l3);
                            token = StringNumberTransformer.this.r.nextInt();
                            this.visitLdcInsn(token);
                            this.visitLdcInsn(StringNumberTransformer.this.r.nextInt());
                            this.visitInsn(87);
                            this.visitInsn(3);
                            super.visitJumpInsn(token > 0 ? 161 : 162, l);
                        }
                    } else if (token < 0) {
                        this.visitInsn(3);
                        super.visitJumpInsn(162, labelIf);
                        StringNumberTransformer.this.invaildLabel.add(0, labelIf);
                    } else {
                        this.visitInsn(3);
                        super.visitJumpInsn(159, labelIf);
                        super.visitTypeInsn(187, "exceptions/BadException");
                        super.visitInsn(89);
                        super.visitLdcInsn("SbUwU");
                        super.visitMethodInsn(183, "exceptions/BadException", "<init>", "(Ljava/lang/String;)V", false);
                        super.visitInsn(191);
                        this.mv.visitLabel(labelIf);
                    }
                }

                @Override
                public void visitLdcInsn(Object value) {
                    if (value instanceof String && Main.useStringObf) {
                        this.visitString(value.toString(), true);
                    } else if (value instanceof Integer && Main.useNumberObf) {
                        this.visitNumberInt((Integer)value);
                    } else if (value instanceof Long && Main.useNumberObf) {
                        this.visitNumberLong((Long)value);
                    } else {
                        super.visitLdcInsn(value);
                    }
                }

                @Override
                public void visitInsn(int opcode) {
                    Integer in = null;
                    Long l = null;
                    if (opcode >= 2 && opcode <= 8 && Main.useNumberObf) {
                        in = opcode - 3;
                    } else if (opcode >= 9 && opcode <= 10 && Main.useNumberObf) {
                        l = opcode - 9L;
                    }
                    if (in != null) {
                        this.visitNumberInt(in);
                    } else if (l != null) {
                        this.visitNumberLong(l);
                    } else {
                        super.visitInsn(opcode);
                    }
                }

                @Override
                public void visitIntInsn(int opcode, int operand) {
                    if ((opcode == 16 || opcode == 17) && Main.useNumberObf) {
                        this.visitLdcInsn(operand);
                    } else {
                        super.visitIntInsn(opcode, operand);
                    }
                }

                @Override
                public void visitMethodInsn(int opcode, String owner, String name2, String descriptor, boolean isInterface) {
                    super.visitMethodInsn(opcode, owner, name2, descriptor, isInterface);
                }

                @Override
                public void visitTypeInsn(int opcode, String type) {
                    super.visitTypeInsn(opcode, type);
                    if (opcode == 187) {
                        this.addJunkLabelThrow();
                    }
                }

                public void visitNegNumber(int count) {
                    if (this.negNumberVisited++ > 800) {
                        return;
                    }
                    for (int j = 0; j < count; ++j) {
                        Label l = new Label();
                        super.visitInsn(116);
                        super.visitInsn(89);
                        super.visitInsn(87);
                        l = new Label();
                        super.visitInsn(116);
                        super.visitInsn(89);
                        super.visitInsn(87);
                    }
                }
            };
        }
        return super.visitMethod(access, name, descriptor, signature, exceptions);
    }
}