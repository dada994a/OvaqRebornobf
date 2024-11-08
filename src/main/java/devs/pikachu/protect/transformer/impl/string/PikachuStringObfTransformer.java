package devs.pikachu.protect.transformer.impl.string;

import devs.pikachu.protect.Main;
import devs.pikachu.protect.transformer.TransVisitor;
import devs.pikachu.protect.utility.ClassWriter1;
import devs.pikachu.protect.utility.Utils;
import exceptions.BadError;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class PikachuStringObfTransformer extends TransVisitor {
    public final String fieldName1;
    public final String fieldName1Desc = "[Ljava/lang/Object;";
    public Map<String, Integer> arrayIndex = new HashMap<>();
    private MethodVisitor clinitmv;
    private boolean startedFlag;
    private boolean hasClinitMethod;

    public PikachuStringObfTransformer(ClassReader cr, ClassWriter1 cw, int api) {
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
        if ((access | 0x1000) == access || (access | 0x4000) == access || (access | 0x200) == access) {
            this.isEx = true;
        }
        if (version <= 51 && version != -65536 && Main.fixVersion && !this.isEx && this.isIn) {
            version = 52;
        }
        super.visit(version, access, name, signature, superName, interfaces);
        if (Main.useStringObfT && this.isIn && !this.isEx) {
            ClassReader reader = new ClassReader(this.cr.b);
            reader.accept(new ClassVisitor(Main.int1){

                @Override
                public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
                    return new MethodVisitor(Main.int1, super.visitMethod(access, name, descriptor, signature, exceptions)){

                        @Override
                        public void visitLdcInsn(Object value) {
                            if (value instanceof String) {
                                for (int jj = 0; jj < (r.nextBoolean() ? r.nextInt(2) : 0); ++jj) {
                                    arrayIndex.put(Utils.spawnRandomChar(r.nextInt(30) + 1, true), PikachuStringObfTransformer.this.arrayIndex.size());
                                }
                                if (!arrayIndex.containsKey(value)) {
                                    arrayIndex.put(value.toString(), arrayIndex.size());
                                }
                            }
                            super.visitLdcInsn(value);
                        }
                    };
                }
            }, 0);
            this.clinitmv = this.visitMethod(8 | (Main.addSyntheticFlag ? 4096 : 0), "<clinit>", "()V", null, null);
            this.initArray();
        }
        this.startedFlag = true;
    }

    void initArray() {
        if (!Main.useStringObfT) {
            return;
        }
        if (!this.arrayIndex.isEmpty()) {
            this.visitField(0xA | (Main.addSyntheticFlag ? 4096 : 0), this.fieldName1, "[Ljava/lang/Object;", null, null);
            ArrayList<String> al = new ArrayList<>(this.arrayIndex.keySet());
            for (int i = 0; i < al.size(); ++i) {
                String str2;
                String str1 = al.get(i);
                if (Objects.equals(str1, str2 = al.get(this.r.nextInt(al.size())))) continue;
                int iii1 = this.arrayIndex.get(str1);
                int iii2 = this.arrayIndex.get(str2);
                this.arrayIndex.put(str1, iii2);
                this.arrayIndex.put(str2, iii1);
            }
            this.clinitmv.visitLdcInsn(this.arrayIndex.size() + 1);
            this.clinitmv.visitTypeInsn(189, "java/lang/Object");
            for (Map.Entry<String, Integer> entry : this.arrayIndex.entrySet()) {
                String s = entry.getKey();
                String className = Utils.getRandomMember(Main.exceptions).getName();
                char[] chars = s.toCharArray();
                char[] chars2 = className.toCharArray();
                int key = this.r.nextInt(126) + 1;
                int key3 = 0;
                int key2 = Utils.spawnRandomChar().charAt(0);
                char key2_ = (char)key2;
                char[] newchars = new char[chars.length];
                for (int i = 0; i < chars.length; ++i) {
                    key2 <<= key2 + key2 % 4 + 123456789;
                    key3 = key2 / 9 >> 2;
                    newchars[i] = (char)(chars[i] ^ (key * (i + 1) ^ key2 + key3 ^ chars2[i % chars2.length]) % 65536);
                }
                String encodedString = key2_ + new String(newchars);
                Label l = new Label();
                this.clinitmv.visitLabel(l);
                this.clinitmv.visitLineNumber(key, l);
                this.clinitmv.visitInsn(89);
                this.clinitmv.visitLdcInsn(entry.getValue());
                this.clinitmv.visitTypeInsn(187, className.replace(".", "/"));
                this.clinitmv.visitInsn(89);
                this.clinitmv.visitLdcInsn(encodedString);
                this.clinitmv.visitMethodInsn(183, className.replace(".", "/"), "<init>", "(Ljava/lang/String;)V", false);
                this.clinitmv.visitInsn(83);
            }
            this.clinitmv.visitFieldInsn(179, this.cr.getClassName(), this.fieldName1, "[Ljava/lang/Object;");
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

                @Override
                public void visitLdcInsn(Object value) {
                    if (value instanceof String) {
                        if (arrayIndex.containsKey(value)) {
                            if (!arrayIndex.containsKey(value)) {
                                throw new BadError(value + " Stringが見つかりませんでした。 通常これは発生しません。 Romに伝えてください！");
                            }
                            int arrayII = arrayIndex.get(value);
                            super.visitFieldInsn(178, cr.getClassName(), fieldName1, "[Ljava/lang/Object;");
                            this.visitLdcInsn(arrayII);
                            super.visitInsn(50);
                            super.visitMethodInsn(184, "devs/pikachu/protect/PikachuObf", "Kami", "(Ljava/lang/Object;)Ljava/lang/String;", false);
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