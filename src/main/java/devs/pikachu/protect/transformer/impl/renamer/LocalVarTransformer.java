package devs.pikachu.protect.transformer.impl.renamer;

import devs.pikachu.protect.Main;
import devs.pikachu.protect.transformer.TransVisitor;
import devs.pikachu.protect.utility.ClassWriter1;
import devs.pikachu.protect.utility.Utils;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;

import java.util.HashSet;

public class LocalVarTransformer extends TransVisitor {
    public LocalVarTransformer(ClassReader cr, ClassWriter1 cw, int api) {
        super(cr, cw, api);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        if (this.isEx) {
            return super.visitMethod(access, name, descriptor, signature, exceptions);
        }
        if (this.isIn) {
            return new MethodVisitor(this.api, super.visitMethod(access, name, descriptor, signature, exceptions)){
                private final HashSet<String> usedNames;
                {
                    this.usedNames = new HashSet<>();
                }

                @Override
                public void visitLocalVariable(String name, String descriptor, String signature, Label start, Label end, int index) {
                    if (Main.delLocalVar && !Main.obfLocalVar) {
                        return;
                    }
                    if (Main.obfLocalVar) {
                        int tries = 0;
                        do {
                            name = Utils.getRandomMember(Main.remapStrings);
                            for (int i = 0; i < tries / 50; ++i) {
                                name = name + "_" + Utils.getRandomMember(Main.remapStrings);
                            }
                            ++tries;
                        } while (this.usedNames.contains(name));
                        this.usedNames.add(name);
                    }
                    super.visitLocalVariable(name, descriptor, signature, start, end, index);
                }
            };
        }
        return super.visitMethod(access, name, descriptor, signature, exceptions);
    }
}