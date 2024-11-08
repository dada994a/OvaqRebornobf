package devs.pikachu.protect.transformer.impl.string;

import devs.pikachu.protect.transformer.TransVisitor;
import devs.pikachu.protect.utility.ClassWriter1;
import org.objectweb.asm.ClassReader;

public class NukeStringObfTransformer extends TransVisitor {
    public NukeStringObfTransformer(ClassReader cr, ClassWriter1 cw, int api) {
        super(cr, cw, api);
    }
}
