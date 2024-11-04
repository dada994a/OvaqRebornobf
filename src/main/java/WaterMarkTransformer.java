//TODO Test
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class WaterMarkTransformer extends TransVisitor {
    private final List<ClassNode> classes;
    private final JarFile jarFile;

    public WaterMarkTransformer(ClassReader cr, ClassWriter1 cw, int api, JarFile jarFile) {
        super(cr, cw, api);
        this.jarFile = jarFile;
        this.classes = new ArrayList<>();
        loadClasses();
    }

    private void loadClasses() {
        try {
            for (JarEntry entry : Collections.list(jarFile.entries())) {
                if (entry.getName().endsWith(".class")) {
                    try (InputStream is = jarFile.getInputStream(entry)) {
                        ClassReader cr = new ClassReader(is);
                        ClassNode cn = new ClassNode();
                        cr.accept(cn, 0);
                        classes.add(cn);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private byte[] getFile(String path) {
        try {
            JarEntry entry = jarFile.getJarEntry(path);
            if (entry != null) {
                try (InputStream is = jarFile.getInputStream(entry)) {
                    return is.readAllBytes();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        if (Main.useWaterMark) {
            String mainClass = null;

            byte[] data = getFile("META-INF/MANIFEST.MF");
            if (data != null) {
                String manifest = new String(data);
                manifest = manifest.substring(0, manifest.length() - 2);
                String[] list = manifest.split("\n");

                for (String str : list) {
                    if (str.startsWith("Main-Class")) mainClass = str.replace("Main-Class: ", "");
                }
                if (mainClass != null) {
                    String finalMainClass = mainClass;
                    classes.forEach(classNode -> {
                        if (finalMainClass.contains(classNode.name.replace("/", "."))) {
                            classNode.methods.stream()
                                    .filter(methodNode -> methodNode.name.equals("main"))
                                    .filter(methodNode -> methodNode.desc.equals("([Ljava/lang/String;)V"))
                                    .forEach(methodNode -> {
                                        InsnList insnList = new InsnList();
                                        insnList.add(new FieldInsnNode(Opcodes.GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;"));
                                        insnList.add(new LdcInsnNode("Protected by Pikachu Obfuscation " + Main.version + " :)"));
                                        insnList.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V"));

                                        AbstractInsnNode insn = methodNode.instructions.get(0);
                                        methodNode.instructions.insertBefore(insn, insnList);
                                    });
                        }
                    });
                }
            }
        }
        super.visit(version, access, name, signature, superName, interfaces);
    }
}
