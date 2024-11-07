package devs.pikachu.protect.utility;

import devs.pikachu.protect.Main;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;

public class ClassWriter1 extends ClassWriter {
    public ClassWriter1(ClassReader var1, int var2) {
        super(var1, var2);
    }

    @Override
    protected String getCommonSuperClass(String var1, String var2) {
        Class<?> var5;
        Class<?> var4;
        try {
            var4 = Main.urlcl.loadClass(var1.replace('/', '.'));
        }
        catch (Throwable var7) {
            var7.printStackTrace();
            if (var7 instanceof ClassNotFoundException) {
                Main.urlcl.defineClass(this.getClass(var7.getLocalizedMessage().replace(".", "/")));
                return this.getCommonSuperClass(var1, var2);
            }
            return "java/lang/Object";
        }
        try {
            var5 = Main.urlcl.loadClass(var2.replace('/', '.'));
        }
        catch (Throwable var7) {
            var7.printStackTrace();
            if (var7 instanceof ClassNotFoundException) {
                Main.urlcl.defineClass(this.getClass(var7.getLocalizedMessage().replace(".", "/")));
                return this.getCommonSuperClass(var1, var2);
            }
            return "java/lang/Object";
        }
        if (var4.isAssignableFrom(var5)) {
            return var1;
        }

        if (var5.isAssignableFrom(var4)) {
            return var2;
        }

        if (!var4.isInterface() && !var5.isInterface()) {
            while (!(var4 = var4.getSuperclass()).isAssignableFrom(var5)) {
            }
            return var4.getName().replace('.', '/');
        }

        return "java/lang/Object";
    }

    private byte[] getClass(String name) {
        ClassWriter cw = new ClassWriter(0);
        cw.visit(52, 33, name, null, "java/lang/Object", null);
        return cw.toByteArray();
    }
}