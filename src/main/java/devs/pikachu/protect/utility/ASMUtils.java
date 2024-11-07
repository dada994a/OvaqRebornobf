package devs.pikachu.protect.utility;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.IntInsnNode;
import org.objectweb.asm.tree.LdcInsnNode;

public class ASMUtils {
    public static boolean isInteger(AbstractInsnNode node) {
        if (node == null) {
            return false;
        }
        int opcode = node.getOpcode();
        return opcode >= 2 && opcode <= 8 || opcode == 16 || opcode == 17 || node instanceof LdcInsnNode && ((LdcInsnNode)node).cst instanceof Integer;
    }

    public static int getInteger(AbstractInsnNode node) {
        int opcode = node.getOpcode();
        if (opcode >= 2 && opcode <= 8) {
            return opcode - 3;
        }
        if (node instanceof IntInsnNode && node.getOpcode() != 188) {
            return ((IntInsnNode)node).operand;
        }
        if (node instanceof LdcInsnNode && ((LdcInsnNode)node).cst instanceof Integer) {
            return (Integer)((LdcInsnNode)node).cst;
        }
        throw new IllegalArgumentException();
    }
}
