
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Label;

public abstract class TransVisitor extends ClassVisitor {
    public Random r = new Random();
    public ClassReader cr;
    public boolean flag = true;
    public boolean isEx;
    public boolean isIn;
    public List<Label> invaildLabel = new ArrayList<>();

    public TransVisitor(ClassReader cr, ClassWriter1 cw, int api) {
        super(api, cw);
        this.cr = cr;
        for (String in : Main.includeClass) {
            if (!cr.getClassName().matches(in) && !cr.getClassName().startsWith("PikachuObf")) continue;
            this.isIn = true;
            break;
        }
        for (String ex : Main.excludeClass) {
            if (!cr.getClassName().matches(ex)) continue;
            this.isEx = true;
            return;
        }
    }

    public boolean shouldObf() {
        return this.isIn && !this.isEx || this.cr.getClassName().startsWith("PikachuObf");
    }
}