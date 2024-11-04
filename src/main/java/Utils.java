import org.objectweb.asm.ClassWriter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.CodingErrorAction;
import java.nio.charset.StandardCharsets;
import java.util.Random;

public class Utils {
    public static Random r = new Random();

    public static String spawnRandomChar(int charCount, char range1, char range2) {
        char[] chars = new char[charCount];
        for (int i = 0; i < chars.length; ++i) {
            chars[i] = (char)((r.nextInt(Integer.MAX_VALUE) >> r.nextInt(4) * r.nextInt(Integer.MAX_VALUE) << r.nextInt(4) ^ r.nextInt(Integer.MAX_VALUE)) % (range2 - range1) + range1);
        }
        return new String(chars);
    }

    public static String spawnRandomChar(int charCount, boolean skipCheck) {
        char[] chars = new char[charCount];
        for (int i = 0; i < chars.length; ++i) {
            chars[i] = (char)((r.nextInt(Integer.MAX_VALUE) >> r.nextInt(4) * r.nextInt(Integer.MAX_VALUE) << r.nextInt(4) ^ r.nextInt(Integer.MAX_VALUE)) % 57343 + 8192);
        }
        String s = new String(chars);
        if (!skipCheck) {
            try {
                Utils.getBytes(chars);
            }
            catch (IllegalArgumentException e) {
                return Utils.spawnRandomChar(charCount, skipCheck);
            }
        }
        return s;
    }

    public static String spawnRandomChar() {
        return Utils.spawnRandomChar(r.nextInt(3) + 10, false);
    }

    private static byte[] getBytes(char[] s) {
        try {
            ByteBuffer bb = StandardCharsets.UTF_8.newEncoder().onMalformedInput(CodingErrorAction.REPORT).onUnmappableCharacter(CodingErrorAction.REPORT).encode(CharBuffer.wrap(s));
            int pos = bb.position();
            int limit = bb.limit();
            if (bb.hasArray() && pos == 0 && limit == bb.capacity()) {
                return bb.array();
            }
            byte[] bytes = new byte[bb.limit() - bb.position()];
            bb.get(bytes);
            return bytes;
        }
        catch (CharacterCodingException x) {
            throw new IllegalArgumentException(x);
        }
    }

    public static void CWvisit(ClassWriter cw, int version, int flags, String classname, String sign, String superclass, String[] implementss) {
        cw.visit(version, flags, classname, sign, superclass, implementss);
    }

    public static int[] spiltNumber(int i) {
        int[] il = new int[2];
        if (i > 0) {
            int temp = r.nextInt(i + 1);
            int temp2 = i - temp;
            il[0] = temp;
            il[1] = temp2;
        }
        return il;
    }

    public static String awa() {
        String s = "";
        for (int i = 0; i < 15; ++i) {
            s = s + (r.nextBoolean() ? "可" : "莉");
        }
        return s;
    }

    public static String getRandomMember(String[] s) {
        if (s == null) {
            return null;
        }
        if (s.length == 0) {
            return "";
        }
        return s[r.nextInt(s.length)];
    }

    public static <T> T getRandomMember(T[] s) {
        if (s == null) {
            return null;
        }
        if (s.length == 0) {
            return null;
        }
        return s[r.nextInt(s.length)];
    }

    public static byte[] ohmyGyatt(InputStream is) throws IOException {
        return Utils.ohmyGyatt(is, true);
    }

    public static byte[] ohmyGyatt(InputStream is, boolean close) throws IOException {
        int b;
        if (is == null) {
            return new byte[0];
        }
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        while ((b = is.read()) != -1) {
            os.write(b);
        }
        byte[] bl = os.toByteArray();
        try {
            os.close();
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
        try {
            if (close) {
                is.close();
            }
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
        return bl;
    }
}