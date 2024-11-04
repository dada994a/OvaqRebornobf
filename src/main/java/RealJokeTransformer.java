import org.objectweb.asm.ClassReader;
import org.objectweb.asm.MethodVisitor;

public class RealJokeTransformer extends TransVisitor {
    private final String newPackageName;

    public RealJokeTransformer(ClassReader cr, ClassWriter1 cw, int api) {
        super(cr, cw, api);
        this.newPackageName = GetALifeAhhMove();
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        if (Main.useRealjoke) {
            if (shouldObf()) {
                name = newPackageName.replace('.', '/') + "/" + name.substring(name.lastIndexOf('/') + 1);
            }
        }
        super.visit(version, access, name, signature, superName, interfaces);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        return new MethodVisitor(this.api, super.visitMethod(access, name, descriptor, signature, exceptions)){
            @Override
            public void visitMethodInsn(int opcode, String owner, String name, String descriptor) {
                owner = newPackageName.replace('.', '/') + "/" + owner.substring(owner.lastIndexOf('/') + 1);
                this.visitMethodInsn(opcode, owner, name, descriptor, false);
            }

            @Override
            public void visitMethodInsn(int opcode, String owner, String name, String descriptor, boolean isInterface) {
                owner = newPackageName.replace('.', '/') + "/" + owner.substring(owner.lastIndexOf('/') + 1);
                super.visitMethodInsn(opcode, owner, name, descriptor, isInterface);
            }
        };
    }

    private String GetALifeAhhMove() {
        String[] randomWords = {"foo", "bar", "baz", "qux", "quux", "corge", "grault", "garply", "waldo", "fred", "plugh", "xyzzy", "thud"};
        StringBuilder packageName = new StringBuilder();
        for (int i = 0; i < 3; i++) {
            packageName.append(randomWords[Utils.r.nextInt(randomWords.length)]).append(".");
        }
        return packageName.substring(0, packageName.length() - 1);
    }
}