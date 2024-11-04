
import exceptions.BadException;

import java.lang.invoke.ConstantCallSite;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

//New Modern Native Loader For Pikachu Obfuscator
public class PikachuObf {
    public static final boolean _________;
    public static final String xsd_1;
    public static final String xsd_2;
    public static final String xsd_3;
    private final Object type;
    private final Object clazz;
    private final Object method;
    private final Object des;
    private Object cache;
    public static Map<String, PikachuObf[]> map;
    public static Map.Entry[] entries;

    public static Object BROZOZOTOWN(Object lookup, Object idk, Object mt, Object type, Object key, Object clazz, Object method, Object des) {
        try {
            int i;
            int key1 = (Integer)key;
            char[] charKey = new char[key1 % 10 + 10];
            for (int i2 = 0; i2 < charKey.length; ++i2) {
                key1 += key1 * (key1 >> 5) + (key1 << 5) + 114514;
                charKey[i2] = (char)(key1 % 65536);
            }
            char[] charOwner = clazz.toString().toCharArray();
            char[] charName = method.toString().toCharArray();
            char[] charDescriptor = des.toString().toCharArray();
            for (i = 0; i < charOwner.length; ++i) {
                charOwner[i] = (char)(charOwner[i] ^ charKey[i % charKey.length]);
            }
            for (i = 0; i < charName.length; ++i) {
                charName[i] = (char)(charName[i] ^ charKey[i % charKey.length]);
            }
            for (i = 0; i < charDescriptor.length; ++i) {
                charDescriptor[i] = (char)(charDescriptor[i] ^ charKey[i % charKey.length]);
            }
            MethodHandle mh;
            switch (type.hashCode()) {
                case -2051695357: {
                    mh = ((MethodHandles.Lookup)lookup).findStatic(Class.forName(new String(charOwner)), new String(charName), MethodType.fromMethodDescriptorString(new String(charDescriptor), PikachuObf.class.getClassLoader()));
                    break;
                }
                case 859384035: {
                    mh = ((MethodHandles.Lookup)lookup).findVirtual(Class.forName(new String(charOwner)), new String(charName), MethodType.fromMethodDescriptorString(new String(charDescriptor), PikachuObf.class.getClassLoader()));
                    break;
                }
                default: {
                    throw new BadException("SbUwU");
                }
            }
            mh = mh.asType((MethodType)mt);
            ConstantCallSite obj = new ConstantCallSite(mh);
            return obj;
        }
        catch (Throwable e) {
            throw new BadException("obfuscation error: ", e);
        }
    }

    public static Object BROZOZOTOWN(Object lookup, Object idk, Object mt, Object type, Object clazz, Object method, Object des) {
        try {
            MethodHandle mh;
            switch (type.hashCode()) {
                case -2051695357: {
                    mh = ((MethodHandles.Lookup)lookup).findStatic(Class.forName(clazz.toString()), method.toString(), MethodType.fromMethodDescriptorString(des.toString(), PikachuObf.class.getClassLoader()));
                    break;
                }
                case 859384035: {
                    mh = ((MethodHandles.Lookup)lookup).findVirtual(Class.forName(clazz.toString()), method.toString(), MethodType.fromMethodDescriptorString(des.toString(), PikachuObf.class.getClassLoader()));
                    break;
                }
                default: {
                    throw new BadException("SbUwU");
                }
            }
            mh = mh.asType((MethodType)mt);
            return new ConstantCallSite(mh);
        }
        catch (Throwable e) {
            throw new BadException("obfuscation error: ", e);
        }
    }

    public static String Kami(Object s) {
        char[] newchars;
        if (s instanceof Throwable) {
            int key = ((Throwable)s).getStackTrace()[0].getLineNumber();
            int key2 = 0;
            int key3;
            char[] chars = ((Throwable)s).getLocalizedMessage().toCharArray();
            char[] chars2 = s.getClass().getName().toCharArray();
            newchars = new char[chars.length - 1];
            for (int i = 0; i < chars.length; ++i) {
                if (i == 0) {
                    key2 = chars[i];
                    continue;
                }
                key2 <<= key2 + key2 % 4 + 123456789;
                key3 = key2 / 9 >> 2;
                newchars[i - 1] = (char)(chars[i] ^ (key * i ^ key2 + key3 ^ chars2[(i - 1) % chars2.length]) % 65536);
            }
        } else {
            char key = '\u0000';
            char[] chars = s.toString().toCharArray();
            newchars = new char[chars.length - 1];
            for (int i = 0; i < chars.length; ++i) {
                if (i == 0) {
                    key = chars[i];
                    continue;
                }
                newchars[i - 1] = (char)(chars[i] ^ key * i);
            }
        }
        return new String(newchars);
    }

    public static long Kami(long l1, long l2, long l3) {
        if (_________) {
            return l1 ^ l2 ^ l3 ^ 0xFFFFFFFFFFFFFFFFL;
        }
        return l1 ^ l2 ^ (l3 ^ 0xFFFFFFFFFFFFFFFFL);
    }

    public static int Kami(int i1, int i2, int i3) {
        if (_________) {
            return ~(i1 ^ i2 ^ i3);
        }
        return i1 ^ i2 ^ ~i3;
    }

    public static void log(Object obj) {
        if (!_________) {
            System.out.println(obj);
        }
    }

    public static String test(String s) {
        System.out.println(s);
        return s;
    }

    public PikachuObf(Object type, Object clazz, Object method, Object des) {
        this.type = type;
        this.clazz = clazz;
        this.method = method;
        this.des = des;
    }

    public static Object invoke(Object lookup, Object idk, Object mt, Object var1, Object var2, Object var3, Object var4) throws Throwable {
        var1 = Kami(var1);
        var2 = Kami(var2);
        boolean shouldAppend = false;
        PikachuObf[] x = null;
        for (int i = 0; i < entries.length; ++i) {
            String ss = (String)entries[i].getKey();
            PikachuObf[] xx = (PikachuObf[])entries[i].getValue();
            if (xx == null) {
                shouldAppend = true;
                break;
            }
            if (ss == null) {
                shouldAppend = true;
                break;
            }
            if (!ss.equals(var1)) continue;
            x = xx;
            break;
        }
        if (shouldAppend || x == null) {
            MethodHandle mh = ((MethodHandles.Lookup)lookup).findStaticGetter(Class.forName(var1.toString()), var2.toString(), PikachuObf[].class);
            x = (PikachuObf[]) mh.invokeExact();
            map.put(var1.toString(), x);
            entries = map.entrySet().toArray(new Map.Entry[0]);
        }
        return x[var1.hashCode() ^ var2.hashCode() ^ var3.hashCode() ^ var4.hashCode()].invoke0(lookup, mt);
    }

    public Object invoke0(Object lookup, Object mt) {
        if (this.cache == null) {
            try {
                Throwable t = (Throwable)this.clazz;
                int cc = 0;
                char[] chars = t.getLocalizedMessage().toCharArray();
                int key = t.getStackTrace()[0].getLineNumber();
                for (int i = 0; i < chars.length; ++i) {
                    chars[i] = (char)(chars[i] ^ (cc ^ key * (chars.length - i) ^ t.getClass().getName().charAt(i % t.getClass().getName().length())) % 65536);
                    cc = chars[i];
                }
                String charOwner = new String(chars);
                t = (Throwable)this.method;
                cc = 0;
                chars = t.getLocalizedMessage().toCharArray();
                key = t.getStackTrace()[0].getLineNumber();
                for (int i = 0; i < chars.length; ++i) {
                    chars[i] = (char)(chars[i] ^ (cc ^ key * (chars.length - i) ^ t.getClass().getName().charAt(i % t.getClass().getName().length())) % 65536);
                    cc = chars[i];
                }
                String charName = new String(chars);
                t = (Throwable)this.des;
                cc = 0;
                chars = t.getLocalizedMessage().toCharArray();
                key = t.getStackTrace()[0].getLineNumber();
                for (int i = 0; i < chars.length; ++i) {
                    chars[i] = (char)(chars[i] ^ (cc ^ key * (chars.length - i) ^ t.getClass().getName().charAt(i % t.getClass().getName().length())) % 65536);
                    cc = chars[i];
                }
                String charDescriptor = new String(chars);
                MethodHandle mh;
                switch (this.type.hashCode()) {
                    case -2051695357: {
                        mh = ((MethodHandles.Lookup)lookup).findStatic(Class.forName(charOwner), charName, MethodType.fromMethodDescriptorString(charDescriptor, PikachuObf.class.getClassLoader()));
                        break;
                    }
                    case 859384035: {
                        mh = ((MethodHandles.Lookup)lookup).findVirtual(Class.forName(charOwner), charName, MethodType.fromMethodDescriptorString(charDescriptor, PikachuObf.class.getClassLoader()));
                        break;
                    }
                    default: {
                        throw new BadException("SbUwU");
                    }
                }
                try {
                    mh = mh.asType((MethodType)mt);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                this.cache = new ConstantCallSite(mh);
            }
            catch (Throwable e) {
                throw new BadException("obfuscation error: ", e);
            }
        }
        return this.cache;
    }

    static {
        boolean isErrored;
        String xsd3;
        String xsd2;
        String xsd1;
        boolean b;
        block6: {
            byte[] a = new byte[11];
            b = false;
            try {
                PikachuObf.class.getResourceAsStream("PikachuObf.txt").read(a);
                b = new String(a).equals("PikachuObf");
            }
            catch (Exception exception) {

            }
            xsd1 = "Example1";
            xsd2 = "Example2";
            xsd3 = "Example3";
            isErrored = false;
            try {
                Field f = xsd1.getClass().getDeclaredField("value");
                f.setAccessible(true);
                Object obj = f.get(xsd1);
                if (obj instanceof byte[]) {
                    Field f2 = xsd1.getClass().getDeclaredField("coder");
                    f2.setAccessible(true);
                    f2.set(xsd1, f2.get(xsd2));
                    f.set(xsd1, f.get(xsd2));
                    break block6;
                }
                if (obj instanceof char[]) {
                    f.set(xsd1, xsd2.toCharArray());
                    break block6;
                }
                throw new RuntimeException("idk");
            }
            catch (Throwable e) {
                isErrored = true;
            }
        }
        xsd_1 = xsd1;
        xsd_2 = isErrored ? xsd1 : xsd2;
        xsd_3 = xsd3;
        _________ = b;
        System.out.println(" =============================== -------------- ======================");
        System.out.println("                                           Protected By Pikachu Obfuscator    ");
        System.out.println("        Made by 2P2FJP Development Team");
        System.out.println(" =============================== -------------- ======================");
        map = new ConcurrentHashMap<>();
        entries = map.entrySet().toArray(new Map.Entry[0]);
    }
}