package devs.pikachu.protect.transformer.impl.ref;

import devs.pikachu.protect.Main;
import devs.pikachu.protect.transformer.TransVisitor;
import devs.pikachu.protect.utility.ClassWriter1;
import devs.pikachu.protect.utility.Utils;
import exceptions.BadError;
import org.objectweb.asm.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class InvokeDynamicTransformer2 extends TransVisitor {
    public final String fieldName1;
    public final String fieldName1Desc = "[Ldevs/pikachu/protect/PikachuObf;";
    public Map<MethodInfo, Integer> methods = new HashMap<>();
    private MethodVisitor clinitmv;
    private boolean startedFlag;
    private boolean hasClinitMethod;
    private String encodeStringKey = Utils.spawnRandomChar(1, false);

    public InvokeDynamicTransformer2(ClassReader cr, ClassWriter1 cw, int api) {
        super(cr, cw, api);
        this.fieldName1 = Main.DIYRemapStringsFile == null ? Utils.spawnRandomChar(20, false) : Utils.getRandomMember(Main.remapStrings);
    }

    @Override
    public void visitEnd() {
        if (!this.hasClinitMethod && this.clinitmv != null) {
            this.clinitmv.visitInsn(177);
            this.clinitmv.visitMaxs(1, 1);
        }
        super.visitEnd();
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        if ((access | 0x1000) == access || (access | 0x4000) == access) {
            this.isEx = true;
        }
        if (version <= 51 && version != -65536 && Main.fixVersion && !this.isEx && this.isIn) {
            version = 52;
        }
        super.visit(version, access, name, signature, superName, interfaces);
        if (Main.useInvokeDynamicObfT && this.isIn && !this.isEx) {
            ClassReader reader = new ClassReader(this.cr.b);
            reader.accept(new ClassVisitor(Main.int1){

                @Override
                public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
                    return new MethodVisitor(Main.int1, super.visitMethod(access, name, descriptor, signature, exceptions)){

                        @Override
                        public void visitMethodInsn(int opcode, String owner, String name, String descriptor) {
                            this.visitMethodInsn(opcode, owner, name, descriptor, false);
                        }

                        @Override
                        public void visitMethodInsn(int opcode, String owner, String name, String descriptor, boolean isInterface) {
                            super.visitMethodInsn(opcode, owner, name, descriptor, isInterface);
                            if (opcode != 183) {
                                MethodInfo mi = new MethodInfo();
                                mi.isStatic = 184 == opcode;
                                mi.owner = owner;
                                mi.name = name;
                                mi.des = descriptor;
                                if (!InvokeDynamicTransformer2.this.methods.containsKey(mi)) {
                                    InvokeDynamicTransformer2.this.methods.put(mi, InvokeDynamicTransformer2.this.methods.size());
                                }
                            }
                        }
                    };
                }
            }, 0);
            this.clinitmv = this.visitMethod(8 | (Main.addSyntheticFlag ? 4096 : 0), "<clinit>", "()V", null, null);
            this.initArray();
        }
        this.startedFlag = true;
    }

    private void initArray() {
        if (!this.methods.isEmpty()) {
            this.visitField(9 | (Main.addSyntheticFlag ? 4096 : 0), this.fieldName1, "[LPikachuObf;", null, null);
            this.clinitmv.visitLdcInsn(this.methods.size() + 1);
            this.clinitmv.visitTypeInsn(189, "PikachuObf");
            ArrayList<MethodInfo> al = new ArrayList<>(this.methods.keySet());
            for (int i = 0; i < al.size(); ++i) {
                MethodInfo str2;
                MethodInfo str1 = al.get(i);
                if (Objects.equals(str1, str2 = al.get(this.r.nextInt(al.size())))) continue;
                int iii1 = this.methods.get(str1);
                int iii2 = this.methods.get(str2);
                this.methods.put(str1, iii2);
                this.methods.put(str2, iii1);
            }
            for (Map.Entry<MethodInfo, Integer> entry : this.methods.entrySet()) {
                MethodInfo s = entry.getKey();
                this.clinitmv.visitInsn(89);
                this.clinitmv.visitLdcInsn(entry.getValue());
                this.clinitmv.visitTypeInsn(187, "PikachuObf");
                this.clinitmv.visitInsn(89);
                this.clinitmv.visitLdcInsn(entry.getKey().isStatic ? "SbUwU" : "Bakaa");
                for (String str : new String[]{entry.getKey().owner.replace("/", "."), entry.getKey().name, entry.getKey().des}) {
                    String className = Utils.getRandomMember(Main.exceptions).getName();
                    Label l = new Label();
                    int key = this.r.nextInt(126) + 1;
                    this.clinitmv.visitLabel(l);
                    this.clinitmv.visitLineNumber(key, l);
                    this.clinitmv.visitTypeInsn(187, className.replace(".", "/"));
                    this.clinitmv.visitInsn(89);
                    int cc = 0;
                    char[] chars = str.toCharArray();
                    for (int i = 0; i < chars.length; ++i) {
                        int cc1 = chars[i];
                        chars[i] = (char)(chars[i] ^ (cc ^ key * (chars.length - i) ^ className.charAt(i % className.length())) % 65536);
                        cc = cc1;
                    }
                    this.clinitmv.visitLdcInsn(new String(chars));
                    this.clinitmv.visitMethodInsn(183, className.replace(".", "/"), "<init>", "(Ljava/lang/String;)V", false);
                }
                this.clinitmv.visitMethodInsn(183, "PikachuObf", "<init>", "(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)V", false);
                this.clinitmv.visitInsn(83);
            }
            this.clinitmv.visitFieldInsn(179, this.cr.getClassName(), this.fieldName1, "[LPikachuObf;");
        }
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        if (name.equals("<clinit>") && this.clinitmv != null) {
            this.hasClinitMethod = true;
            return this.clinitmv;
        }
        MethodVisitor temp = this.visitMethod2(access, name, descriptor, signature, exceptions);
        if (name.equals("<clinit>") && this.clinitmv == null) {
            this.clinitmv = temp;
        }
        return temp;
    }

    private MethodVisitor visitMethod2(int access, String name, String descriptor, String signature, String[] exceptions) {
        if (this.isEx) {
            return super.visitMethod(access, name, descriptor, signature, exceptions);
        }
        if (this.isIn) {
            return new MethodVisitor(this.api, super.visitMethod(access, name, descriptor, signature, exceptions)){

                private String encodeString(String value) {
                    String key = InvokeDynamicTransformer2.this.encodeStringKey;
                    char keychar = key.charAt(0);
                    char[] chars = value.toCharArray();
                    for (int i = 0; i < chars.length; ++i) {
                        chars[i] = (char)(chars[i] ^ keychar * (i + 1));
                    }
                    return key + new String(chars);
                }

                @Override
                public void visitMaxs(int maxStack, int maxLocals) {
                    super.visitMaxs(maxStack, maxLocals);
                }

                @Override
                public void visitMethodInsn(int opcode, String owner, String name, String descriptor, boolean isInterface) {
                    switch (opcode) {
                        case 184: {
                            MethodInfo mi = new MethodInfo();
                            mi.des = descriptor;
                            mi.name = name;
                            mi.owner = owner;
                            mi.isStatic = true;
                            Integer index = InvokeDynamicTransformer2.this.methods.get(mi);
                            if (index == null) {
                                throw new BadError("使用方法: " + mi);
                            }
                            int indexId = index;
                            int hash1 = InvokeDynamicTransformer2.this.cr.getClassName().replace("/", ".").hashCode();
                            int hash2 = InvokeDynamicTransformer2.this.fieldName1.hashCode();
                            int hash3 = Utils.r.nextInt();
                            int hash4 = indexId ^ hash2 ^ hash1 ^ hash3;
                            super.visitInvokeDynamicInsn("PikachuObf", descriptor.replace("(", "("), new Handle(6, "PikachuObf", "invoke", "(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;", false), this.encodeString(InvokeDynamicTransformer2.this.cr.getClassName().replace("/", ".")), this.encodeString(InvokeDynamicTransformer2.this.fieldName1), hash3, hash4);
                            break;
                        }
                        case 182: 
                        case 185: {
                            if (!descriptor.contains("[L")) {
                                MethodInfo mi = new MethodInfo();
                                mi.des = descriptor;
                                mi.name = name;
                                mi.owner = owner;
                                mi.isStatic = false;
                                Integer index = InvokeDynamicTransformer2.this.methods.get(mi);
                                if (index == null) {
                                    throw new BadError("使用方法: " + mi);
                                }
                                int indexId = index;
                                int hash1 = InvokeDynamicTransformer2.this.cr.getClassName().replace("/", ".").hashCode();
                                int hash2 = InvokeDynamicTransformer2.this.fieldName1.hashCode();
                                int hash3 = Utils.r.nextInt();
                                int hash4 = indexId ^ hash2 ^ hash1 ^ hash3;
                                super.visitInvokeDynamicInsn("PikachuObf", descriptor.replace("(", "(Ljava/lang/Object;"), new Handle(6, "PikachuObf", "invoke", "(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;", false), this.encodeString(InvokeDynamicTransformer2.this.cr.getClassName().replace("/", ".")), this.encodeString(InvokeDynamicTransformer2.this.fieldName1), hash3, hash4);
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

    class MethodInfo {
        boolean isStatic;
        String owner;
        String name;
        String des;

        MethodInfo() {
        }

        public String toString() {
            return (this.isStatic ? "static:" : "VIRTUAL:".toLowerCase()) + this.owner + ":" + this.name + this.des;
        }

        public boolean equals(Object o) {
            return o != null && this.toString().equals(o.toString());
        }

        public int hashCode() {
            return this.toString().hashCode();
        }
    }
}