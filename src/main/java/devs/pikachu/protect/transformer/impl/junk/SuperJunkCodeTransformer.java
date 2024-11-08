package devs.pikachu.protect.transformer.impl.junk;

import devs.pikachu.protect.Main;
import devs.pikachu.protect.transformer.TransVisitor;
import devs.pikachu.protect.utility.ClassWriter1;
import devs.pikachu.protect.utility.Utils;
import org.objectweb.asm.*;

import java.util.*;

public class SuperJunkCodeTransformer extends TransVisitor {
    private Map<String, String> map = new HashMap<>();
    private List<String> strings = new ArrayList<>();
    boolean isInterface;

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        super.visit(version, access, name, signature, superName, interfaces);
        this.isInterface = (access & 0x200) != 0;
    }

    public SuperJunkCodeTransformer(ClassReader cr, ClassWriter1 cw, int api) {
        super(cr, cw, api);
        ClassReader reader = new ClassReader(cr.b);
        reader.accept(new ClassVisitor(Main.int1){

            @Override
            public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
                return new MethodVisitor(Main.int1, super.visitMethod(access, name, descriptor, signature, exceptions)){

                    @Override
                    public void visitLdcInsn(Object value) {
                        if (value instanceof String && !SuperJunkCodeTransformer.this.strings.contains(value)) {
                            SuperJunkCodeTransformer.this.strings.add(value.toString());
                        }
                        super.visitLdcInsn(value);
                    }
                };
            }
        }, 0);
    }

    @Override
    public FieldVisitor visitField(int access, String name, String descriptor, String signature, Object value) {
        if ((access | 8) != access) {
            this.map.put(descriptor, name);
        }
        return super.visitField(access, name, descriptor, signature, value);
    }

    @Override
    public MethodVisitor visitMethod(final int access, final String name, String descriptor, String signature, String[] exceptions) {
        if (this.isIn && !this.isEx && this.isInterface) {
            return new MethodVisitor(this.api, super.visitMethod(access, name, descriptor, signature, exceptions)){
                private List<Label> junkThrowLabel;
                private final Label endL;
                private boolean isStatic;
                private boolean shouldObf;
                {
                    this.junkThrowLabel = new ArrayList<>();
                    this.endL = new Label();
                    if ((access | 8) == access) {
                        this.isStatic = true;
                    }
                    this.shouldObf = !name.contains("init>");
                }

                @Override
                public void visitCode() {
                    super.visitCode();
                }

                @Override
                public void visitLdcInsn(Object value) {
                    if (value instanceof String) {
                        this.visitString(value.toString(), true);
                    } else if (value instanceof Integer) {
                        this.visitNumberInt((Integer)value);
                    } else if (value instanceof Long) {
                        this.visitNumberLong((Long)value);
                    } else {
                        super.visitLdcInsn(value);
                    }
                }

                private void visitNumberLong(Long value) {
                    if (!this.shouldObf) {
                        this.mv.visitLdcInsn(value);
                        return;
                    }
                    int str1 = SuperJunkCodeTransformer.this.r.nextInt(3);
                    boolean flag = SuperJunkCodeTransformer.this.r.nextBoolean();
                    int str2 = flag ? (str1 == 0 || str1 == 1 ? SuperJunkCodeTransformer.this.r.nextInt(2) : 2) : (str1 == 0 || str1 == 1 ? 2 : SuperJunkCodeTransformer.this.r.nextInt(2));
                    super.visitFieldInsn(178, "devs/pikachu/protect/PikachuObf", "xsd_" + (str1 + 1), "Ljava/lang/String;");
                    super.visitFieldInsn(178, "devs/pikachu/protect/PikachuObf", "xsd_" + (str2 + 1), "Ljava/lang/String;");
                    super.visitMethodInsn(182, "java/lang/String", "equals", "(Ljava/lang/Object;)Z", false);
                    Label labelIf = new Label();
                    Label l = new Label();
                    Runnable ornginal = () -> this.mv.visitLdcInsn(value);
                    Runnable obf = () -> {
                        if (SuperJunkCodeTransformer.this.r.nextBoolean()) {
                            this.mv.visitLdcInsn(SuperJunkCodeTransformer.this.r.nextLong());
                        } else {
                            Map.Entry entry = (Map.Entry) Utils.getRandomMember(SuperJunkCodeTransformer.this.map.entrySet().stream().filter(a -> ((String)a.getKey()).equals("J")).toArray());
                            if (entry != null && !this.isStatic) {
                                this.mv.visitVarInsn(25, 0);
                                this.mv.visitFieldInsn(180, SuperJunkCodeTransformer.this.cr.getClassName().replace(".", "/"), (String)entry.getValue(), (String)entry.getKey());
                            } else {
                                this.mv.visitLdcInsn(SuperJunkCodeTransformer.this.r.nextLong());
                            }
                            super.visitTypeInsn(187, "exceptions/BadException");
                            super.visitInsn(89);
                            super.visitLdcInsn("SbUwU");
                            super.visitMethodInsn(183, "exceptions/BadException", "<init>", "(Ljava/lang/String;)V", false);
                            super.visitInsn(191);
                        }
                    };
                    super.visitJumpInsn(153, labelIf);
                    if (flag) {
                        ornginal.run();
                    } else {
                        obf.run();
                    }
                    super.visitJumpInsn(167, l);
                    this.mv.visitLabel(labelIf);
                    if (!flag) {
                        ornginal.run();
                    } else {
                        obf.run();
                    }
                    this.mv.visitLabel(l);
                }

                private void visitNumberInt(Integer value) {
                    if (!this.shouldObf || SuperJunkCodeTransformer.this.strings.size() > 40 && Main.bigBrainNumberObf) {
                        this.mv.visitLdcInsn(value);
                        return;
                    }
                    int str1 = SuperJunkCodeTransformer.this.r.nextInt(3);
                    boolean flag = SuperJunkCodeTransformer.this.r.nextBoolean();
                    int str2 = flag ? (str1 == 0 || str1 == 1 ? SuperJunkCodeTransformer.this.r.nextInt(2) : 2) : (str1 == 0 || str1 == 1 ? 2 : SuperJunkCodeTransformer.this.r.nextInt(2));
                    super.visitFieldInsn(178, "PikachuObf", "xsd_" + (str1 + 1), "Ljava/lang/String;");
                    super.visitFieldInsn(178, "PikachuObf", "xsd_" + (str2 + 1), "Ljava/lang/String;");
                    super.visitMethodInsn(182, "java/lang/String", "equals", "(Ljava/lang/Object;)Z", false);
                    Label labelIf = new Label();
                    Label l = new Label();
                    Runnable ornginal = () -> this.mv.visitLdcInsn(value);
                    Runnable obf = () -> {
                        if (SuperJunkCodeTransformer.this.r.nextBoolean()) {
                            if (SuperJunkCodeTransformer.this.r.nextBoolean() && !this.junkThrowLabel.isEmpty()) {
                                super.visitJumpInsn(167, (Label)Utils.getRandomMember(this.junkThrowLabel.toArray()));
                            } else {
                                super.visitLdcInsn(SuperJunkCodeTransformer.this.r.nextInt());
                            }
                        } else {
                            Map.Entry entry = (Map.Entry)Utils.getRandomMember(SuperJunkCodeTransformer.this.map.entrySet().stream().filter(a -> ((String)a.getKey()).equals("I")).toArray());
                            if (entry != null && !this.isStatic) {
                                this.mv.visitVarInsn(25, 0);
                                super.visitFieldInsn(180, SuperJunkCodeTransformer.this.cr.getClassName(), (String)entry.getValue(), "I");
                            } else {
                                super.visitLdcInsn(SuperJunkCodeTransformer.this.r.nextInt());
                            }
                            super.visitTypeInsn(187, "exceptions/BadException");
                            super.visitInsn(89);
                            super.visitLdcInsn("SbUwU");
                            super.visitMethodInsn(183, "exceptions/BadException", "<init>", "(Ljava/lang/String;)V", false);
                            super.visitInsn(191);
                        }
                    };
                    super.visitJumpInsn(153, labelIf);
                    if (flag) {
                        ornginal.run();
                    } else {
                        obf.run();
                    }
                    super.visitJumpInsn(167, l);
                    super.visitLabel(labelIf);
                    if (!flag) {
                        ornginal.run();
                    } else {
                        obf.run();
                    }
                    super.visitLabel(l);
                }

                private void visitString(String string, boolean b) {
                    if (!this.shouldObf || SuperJunkCodeTransformer.this.strings.size() > 40 && Main.bigBrainNumberObf) {
                        this.mv.visitLdcInsn(string);
                        return;
                    }
                    int str1 = SuperJunkCodeTransformer.this.r.nextInt(3);
                    boolean flag = SuperJunkCodeTransformer.this.r.nextBoolean();
                    int str2 = flag ? (str1 == 0 || str1 == 1 ? SuperJunkCodeTransformer.this.r.nextInt(2) : 2) : (str1 == 0 || str1 == 1 ? 2 : SuperJunkCodeTransformer.this.r.nextInt(2));
                    super.visitFieldInsn(178, "devs/pikachu/protect/PikachuObf", "xsd_" + (str1 + 1), "Ljava/lang/String;");
                    super.visitFieldInsn(178, "devs/pikachu/protect/PikachuObf", "xsd_" + (str2 + 1), "Ljava/lang/String;");
                    super.visitMethodInsn(182, "java/lang/String", "equals", "(Ljava/lang/Object;)Z", false);
                    String[] strs = SuperJunkCodeTransformer.this.strings.toArray(new String[0]);
                    Label labelIf = new Label();
                    Label l = new Label();
                    Runnable ornginal = () -> this.mv.visitLdcInsn(string);
                    Runnable obf = () -> {
                        if (SuperJunkCodeTransformer.this.r.nextBoolean()) {
                            super.visitLdcInsn(Utils.getRandomMember(strs));
                        } else {
                            Map.Entry entry = (Map.Entry)Utils.getRandomMember(SuperJunkCodeTransformer.this.map.entrySet().stream().filter(a -> a.getKey().equals("Ljava/lang/String;")).toArray());
                            if (entry != null && !this.isStatic) {
                                this.mv.visitVarInsn(25, 0);
                                super.visitFieldInsn(180, SuperJunkCodeTransformer.this.cr.getClassName(), (String)entry.getValue(), "Ljava/lang/String;");
                            } else {
                                super.visitLdcInsn(Utils.getRandomMember(strs));
                            }
                            super.visitTypeInsn(187, "exceptions/BadException");
                            super.visitInsn(89);
                            super.visitLdcInsn("SbUwU");
                            super.visitMethodInsn(183, "exceptions/BadException", "<init>", "(Ljava/lang/String;)V", false);
                            super.visitInsn(191);
                        }
                    };
                    super.visitJumpInsn(153, labelIf);
                    if (flag) {
                        ornginal.run();
                    } else {
                        obf.run();
                    }
                    super.visitJumpInsn(167, l);
                    super.visitLabel(labelIf);
                    if (!flag) {
                        ornginal.run();
                    } else {
                        obf.run();
                    }
                    super.visitLabel(l);
                }

                @Override
                public void visitInsn(int opcode) {
                    Integer in = null;
                    Long l = null;
                    if (opcode >= 2 && opcode <= 8) {
                        in = opcode - 3;
                    } else if (opcode >= 9 && opcode <= 10) {
                        l = opcode - 9L;
                    }
                    if (in != null) {
                        this.visitNumberInt(in);
                    } else if (l != null) {
                        this.visitNumberLong(l);
                    } else {
                        List<Integer> intOpcodes = Arrays.asList(96, 100, 104, 108, 112);
                        if (intOpcodes.contains(opcode) && (SuperJunkCodeTransformer.this.r.nextInt(10) == 5 || !Main.bigBrainNumberObf)) {
                            int str1 = SuperJunkCodeTransformer.this.r.nextInt(3);
                            boolean flag = SuperJunkCodeTransformer.this.r.nextBoolean();
                            int str2 = flag ? (str1 == 0 || str1 == 1 ? SuperJunkCodeTransformer.this.r.nextInt(2) : 2) : (str1 == 0 || str1 == 1 ? 2 : SuperJunkCodeTransformer.this.r.nextInt(2));
                            super.visitFieldInsn(178, "devs/pikachu/protect/PikachuObf", "xsd_" + (str1 + 1), "Ljava/lang/String;");
                            super.visitFieldInsn(178, "devs/pikachu/protect/PikachuObf", "xsd_" + (str2 + 1), "Ljava/lang/String;");
                            super.visitMethodInsn(182, "java/lang/String", "equals", "(Ljava/lang/Object;)Z", false);
                            Label labelIf = new Label();
                            Label l2 = new Label();
                            Runnable ornginal = () -> super.visitInsn(opcode);
                            Runnable obf = () -> super.visitInsn((Integer) Utils.getRandomMember(intOpcodes.toArray()));
                            super.visitJumpInsn(153, labelIf);
                            if (flag) {
                                ornginal.run();
                            } else {
                                obf.run();
                            }
                            super.visitJumpInsn(167, l2);
                            super.visitLabel(labelIf);
                            if (!flag) {
                                ornginal.run();
                            } else {
                                obf.run();
                            }
                            super.visitLabel(l2);
                        } else {
                            super.visitInsn(opcode);
                        }
                    }
                }

                @Override
                public void visitMaxs(int maxStack, int maxLocals) {
                    super.visitLabel(this.endL);
                    super.visitMaxs(maxStack, maxLocals);
                }

                @Override
                public void visitIincInsn(int varIndex, int increment) {
                    int str1 = SuperJunkCodeTransformer.this.r.nextInt(3);
                    boolean flag = SuperJunkCodeTransformer.this.r.nextBoolean();
                    int str2 = flag ? (str1 == 0 || str1 == 1 ? SuperJunkCodeTransformer.this.r.nextInt(2) : 2) : (str1 == 0 || str1 == 1 ? 2 : SuperJunkCodeTransformer.this.r.nextInt(2));
                    super.visitFieldInsn(178, "devs/pikachu/protect/PikachuObf", "xsd_" + (str1 + 1), "Ljava/lang/String;");
                    super.visitFieldInsn(178, "devs/pikachu/protect/PikachuObf", "xsd_" + (str2 + 1), "Ljava/lang/String;");
                    super.visitMethodInsn(182, "java/lang/String", "equals", "(Ljava/lang/Object;)Z", false);
                    Label labelIf = new Label();
                    Label l = new Label();
                    Runnable ornginal = () -> super.visitIincInsn(varIndex, increment);
                    Runnable obf = () -> super.visitIincInsn(varIndex, -increment);
                    super.visitJumpInsn(153, labelIf);
                    if (flag) {
                        ornginal.run();
                    } else {
                        obf.run();
                    }
                    super.visitJumpInsn(167, l);
                    super.visitLabel(labelIf);
                    if (!flag) {
                        ornginal.run();
                    } else {
                        obf.run();
                    }
                    super.visitLabel(l);
                }

                @Override
                public void visitIntInsn(int opcode, int operand) {
                    if (opcode == 16 || opcode == 17) {
                        this.visitLdcInsn(operand);
                    } else {
                        super.visitIntInsn(opcode, operand);
                    }
                }

                private void addJunkThrow() {
                    Label templ = new Label();
                    Label l = new Label();
                    this.junkThrowLabel.add(l);
                    super.visitJumpInsn(167, templ);
                    super.visitLabel(l);
                    super.visitFrame(-1, 0, new Object[0], 1, new Object[]{"java/lang/Throwable"});
                    super.visitTypeInsn(187, "exceptions/BadException");
                    super.visitInsn(89);
                    super.visitLdcInsn("SbUwU");
                    super.visitMethodInsn(183, "exceptions/BadException", "<init>", "(Ljava/lang/String;)V", false);
                    super.visitInsn(191);
                    super.visitJumpInsn(167, this.endL);
                    super.visitLabel(templ);
                }
            };
        }
        return super.visitMethod(access, name, descriptor, signature, exceptions);
    }
}