package devs.pikachu.protect.transformer.impl.flow;

import devs.pikachu.protect.Main;
import devs.pikachu.protect.transformer.TransVisitor;
import devs.pikachu.protect.utility.ASMUtils;
import devs.pikachu.protect.utility.ClassWriter1;
import java.util.Random;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.LdcInsnNode;

public class ControlFlowTransformer extends TransVisitor {
    public ControlFlowTransformer(ClassReader cr, ClassWriter1 cw, int api) {
        super(cr, cw, api);
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        super.visit(version, access, name, signature, superName, interfaces);

        ClassReader reader = new ClassReader(this.cr.b);
        reader.accept(new ClassVisitor(Main.int1, this){
            @Override
            public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
                MethodVisitor mv = super.visitMethod(access, name, descriptor, signature, exceptions);
                return new MethodVisitor(Main.int1, mv){
                    @Override
                    public void visitCode() {
                        super.visitCode();
                        InsnList instructions = new InsnList();
                        Random random = new Random();
                        for (AbstractInsnNode insn : instructions.toArray()) {
                            if (ASMUtils.isInteger(insn)) {
                                int intDec = random.nextInt();
                                int intDec2 = random.nextInt();
                                int origInt = ASMUtils.getInteger(insn);
                                int forPaste = origInt ^ intDec ^ intDec2;
                                super.visitLdcInsn(new LdcInsnNode(Integer.toString(intDec)));
                                super.visitMethodInsn(184, "java/lang/Integer", "parseInt", "(Ljava/lang/String;)I", false);
                                super.visitLdcInsn(new InsnNode(130));
                                super.visitLdcInsn(new LdcInsnNode(intDec2));
                                super.visitLdcInsn(new InsnNode(130));
                                instructions.insert(insn, instructions);
                                instructions.set(insn, new LdcInsnNode(forPaste));
                                continue;
                            }
                            if (insn.getOpcode() < 153 || insn.getOpcode() > 166) continue;
                            JumpInsnNode jumpInsnNode = (JumpInsnNode)insn;
                            LabelNode offset = new LabelNode();
                            InsnList insnList = new InsnList();
                            insnList.add(new JumpInsnNode(167, jumpInsnNode.label));
                            insnList.add(offset);
                            jumpInsnNode.setOpcode(ControlFlowTransformer.this.reverseJump(insn.getOpcode()));
                            jumpInsnNode.label = offset;
                            instructions.insert(jumpInsnNode, insnList);
                        }
                        instructions.accept(this.mv);
                    }
                };
            }
        }, 0);
    }

    private int reverseJump(int opcode) {
        return switch (opcode) {
            case 154 -> 153;
            case 153 -> 154;
            case 156 -> 155;
            case 157 -> 158;
            case 158 -> 157;
            case 155 -> 156;
            case 199 -> 198;
            case 198 -> 199;
            case 165 -> 166;
            case 166 -> 165;
            case 159 -> 160;
            case 160 -> 159;
            case 162 -> 161;
            case 163 -> 164;
            case 164 -> 163;
            case 161 -> 162;
            default -> throw new IllegalStateException(String.format("Unable to reverse jump opcode: %d", opcode));
        };
    }
}