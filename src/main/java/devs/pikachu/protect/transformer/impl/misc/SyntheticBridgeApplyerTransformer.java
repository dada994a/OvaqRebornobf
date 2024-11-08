package devs.pikachu.protect.transformer.impl.misc;

import devs.pikachu.protect.Main;
import devs.pikachu.protect.transformer.TransVisitor;
import devs.pikachu.protect.utility.ClassWriter1;
import org.objectweb.asm.ClassReader;

public class SyntheticBridgeApplyerTransformer extends TransVisitor {
    public static final String[][] joking = new String[][]{
            {"あれ", "ここには何もないみたい", "他の場所を見てみましょう"},
            {"おっと", "スキッダーがコードを食す"},
            {"コード盗撮犯が殺害されました", "AntiLeakProtect", "RomのUIDは-1です！"},
            {"ニコニコ本社爆破予告"},
            {"ニャンパス", "ディスコードニトロ2025"}
    };
    public SyntheticBridgeApplyerTransformer(ClassReader cr, ClassWriter1 cw, int api) {
        super(cr, cw, api);
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        if (this.shouldObf()) {
            String[] temp;
            for (String temp2 : temp = joking[this.r.nextInt(joking.length)]) {
                super.visitField(25, temp2, "Lexceptions/BadException;", null, null);
            }
        }
        super.visit(version, access, name, signature, superName, interfaces);
    }

    @Override
    public boolean shouldObf() {
        return super.shouldObf() && Main.addSyntheticFlag;
    }
}