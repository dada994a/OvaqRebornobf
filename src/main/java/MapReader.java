import java.util.HashSet;

public class MapReader {
    public static String[] read(String s) {
        char[] chars = s.toCharArray();
        HashSet<String> names = new HashSet<>();
        StringBuffer buffer = new StringBuffer();
        for (char c : chars) {
            boolean flag;
            boolean bl = flag = buffer.length() == 0 ? Character.isJavaIdentifierStart(c) : Character.isJavaIdentifierPart(c);
            if (flag && c != '\n' && c != '\r') {
                buffer.append(c);
                continue;
            }
            if (buffer.length() <= 0) continue;
            names.add(buffer.toString());
            buffer.setLength(0);
        }
        return names.toArray(new String[0]);
    }
}