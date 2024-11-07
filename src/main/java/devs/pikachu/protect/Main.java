package devs.pikachu.protect;

import devs.pikachu.protect.transformer.impl.*;
import devs.pikachu.protect.utility.*;
import exceptions.BadError;
import exceptions.BadException;
import exceptions.Exclude;
import exceptions.NotInclude;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Handle;
import org.objectweb.asm.MethodVisitor;

import java.io.*;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

//                           /\_/\
//                          ( o.o )
//                           > ^ <
//
//         .............................................
//                  Pikachu            Obfuscation Tool
//                           Authors
//                  Rom - Head Developer of Pikachu Obfuscation Tool Project, Developer of OvaqReborn Project
//                  Hypinohaizin - Head Developer of OvaqReborn Project, Developer of Pikachu Obfuscation Tool Project
//                  Naa_Naa - Developer of OvaqReborn Project
//
//                  外部への無断譲渡を禁止します。
//                  Projectの権限は2P2FJP Development Teamが所有しています。

public class Main {
    //public static final RuntimeException notInclude = new NotInclude();
    //public static final RuntimeException exclude = new Exclude();
    public static List<String> errors = new ArrayList<>();
    public static final String version = "2.0-Rewrite";
    public static final String[] pikachuStrs;
    public static MyURLClassLoader urlcl;
    public static File mapFolder;
    public static Random r;
    public static List<String> excludeClass;
    public static List<String> includeClass;
    public static boolean dontEncode;
    public static boolean useJunkCode;
    public static boolean useMoreJunkCode;
    public static boolean useMoreJunkCode2;
    public static boolean useSuperJunkCode;
    public static boolean useInvokeDynamicObf;
    public static boolean useInvokeDynamicObfT;
    public static boolean useStringObf;
    public static boolean useStringObfT;
    public static boolean useNumberObf;
    public static boolean useReverse;
    public static boolean obfLocalVar;
    public static boolean delLocalVar;
    public static boolean fixVersion;
    public static boolean addSyntheticFlag;
    public static boolean dontverify;
    public static boolean classToFolder;
    public static boolean bigBrainNumberObf;
    public static int int1;
    public static File DIYRemapStringsFile;
    public static String[] remapStrings;
    public static Class<?>[] exceptions;

    public static void printUsage() {
        System.out.println();
        System.out.println();
        System.out.println("                /\\_/\\");
        System.out.println("               ( o.o )");
        System.out.println("                > ^ <      Pika~!");
        System.out.println("========= Pikachu Obfuscation Tool by 2P2FJP Development Team ========= " + version);
        System.out.println();
        System.out.println("使用方法:java -jar x.jar <InputJar> <OutputJar> [その他関数...]");
        System.out.println("注意：InputJarとOutputJarは必須です！");
        System.out.println();
        System.out.println("関数一覧: ");
        System.out.println();
        System.out.println("      -?                    |  ヘルプを表示");
        System.out.println();
        System.out.println("      -inClass <classes> ...|  混淆するクラスを入力, -inClassの後のすべてのパラメータは混淆するクラスであり、-で終了します, 正規表現を使用してマッチング");
        System.out.println("      -exClass <classes> ...|  inClassから除外するクラスを入力, -で終了します, 正規表現を使用してマッチング");
        System.out.println();
        System.out.println("      -useInvokeDynamicObf  |* InvokeDynamicバイトコード混淆を使用");
        System.out.println("      -useInvokeDynamicObfT |* InvokeDynamicの基礎上で再混淆");
        System.out.println("      -useStringObf         |* すべての文字列を混淆");
        System.out.println("      -useStringObfT        |* 文字列混淆の基礎上で再混淆");
        System.out.println("      -useNumberObf         |* すべての整数を混淆");
        System.out.println("      -reverse              |  一部のString, IntegerをhashCode(Example: 'あ'.hashCode)にして、理解を困難にします。");
        System.out.println("      -bigBrainNumberObf    |  芸術的な数字混淆 (注意: Minecraft Modと混淆する場合、これを使用しないでください)");
        System.out.println("      -applymap <File>      |  mapファイルをこの混淆器のルートディレクトリのmapフォルダに置き、<File>はxxx.txtです");
        System.out.println("      -obfLocalVar          |  -applymapで設定したmap内容を使用してメソッド内のローカル変数を混淆");
        System.out.println("      -delLocalVar          |* メソッド内のローカル変数を混淆");
        System.out.println("      -dontEncode           |  invokeDynamicの文字を暗号化しない、jarパッケージのサイズを縮小");
        System.out.println("      -useJunkCode          |* コードにジャンクコードを挿入");
        System.out.println("      -useMoreJunkCode      |* コードにより多くのジャンクコードを挿入 (注意: Minecraft Modと混淆する場合、これを使用しないでください)");
        System.out.println("      -useSuperJunkCode     |  コードにさらに多くの超ジャンクコードを挿入 (注意: 使用後はJava 1.8でのみ実行可能、Java 1.9以降のバージョンで実行した場合のバグは自己責任)");
        System.out.println("      -addSyntheticFlag     |  フィールドとメソッドに「合成」タグを付けて、逆コンパイラがソースコードを直接表示できないようにする");
        System.out.println("      -full                 |  この説明で*が付いたパラメータをすべて有効にする");
        System.out.println("      -asmVer (4-10)        |  指定されたASMバージョンで混淆 (範囲は4-10)");
        System.out.println("      -classToFolder        |  classファイルをフォルダに隠して直接認識できないようにする");
        System.out.println("      -fixVersion           |  クラスバージョンエラーを修正する");
        System.out.println();
        System.out.println();
        System.out.println("Example: ");
        System.out.println("java -jar xxx.jar \"D:\\a.jar\" \"D:\\b.jar\" -inClass (.*) -exClass org(.*) -useJunkCode");
    }

    public static void main(String[] args) throws Exception {
        String lastArg = "";
        boolean exF = false;
        boolean inF = false;
        if (args.length < 2) {
            Main.printUsage();
            System.exit(-1);
        }
        int i = 0;
        for (String arg : args) {
            block19:
            {
                block42:
                {
                    block41:
                    {
                        block40:
                        {
                            block39:
                            {
                                block38:
                                {
                                    block37:
                                    {
                                        block36:
                                        {
                                            block35:
                                            {
                                                block34:
                                                {
                                                    block33:
                                                    {
                                                        block32:
                                                        {
                                                            block31:
                                                            {
                                                                block30:
                                                                {
                                                                    block29:
                                                                    {
                                                                        block28:
                                                                        {
                                                                            block27:
                                                                            {
                                                                                block26:
                                                                                {
                                                                                    block25:
                                                                                    {
                                                                                        block24:
                                                                                        {
                                                                                            block23:
                                                                                            {
                                                                                                block22:
                                                                                                {
                                                                                                    block21:
                                                                                                    {
                                                                                                        block20:
                                                                                                        {
                                                                                                            block18:
                                                                                                            {
                                                                                                                if (arg.isEmpty() || ++i <= 2 || arg.startsWith("#"))
                                                                                                                    continue;
                                                                                                                if (!arg.equals("-exClass"))
                                                                                                                    break block18;
                                                                                                                exF = true;
                                                                                                                inF = false;
                                                                                                                break block19;
                                                                                                            }
                                                                                                            if (!arg.equals("-inClass"))
                                                                                                                break block20;
                                                                                                            exF = false;
                                                                                                            inF = true;
                                                                                                            break block19;
                                                                                                        }
                                                                                                        if (arg.startsWith("-")) {
                                                                                                            inF = false;
                                                                                                            exF = false;
                                                                                                        }
                                                                                                        if (!arg.equals("-full"))
                                                                                                            break block21;
                                                                                                        useJunkCode = true;
                                                                                                        delLocalVar = true;
                                                                                                        useNumberObf = true;
                                                                                                        useStringObfT = true;
                                                                                                        useStringObf = true;
                                                                                                        useInvokeDynamicObfT = true;
                                                                                                        useInvokeDynamicObf = true;
                                                                                                        break block19;
                                                                                                    }
                                                                                                    if (!arg.equals("-?"))
                                                                                                        break block22;
                                                                                                    Main.printUsage();
                                                                                                    break block19;
                                                                                                }
                                                                                                if (!arg.equals("-dontEncode"))
                                                                                                    break block23;
                                                                                                dontEncode = true;
                                                                                                break block19;
                                                                                            }
                                                                                            if (!arg.equals("-dontVerify"))
                                                                                                break block24;
                                                                                            dontverify = true;
                                                                                            break block19;
                                                                                        }
                                                                                        if (!arg.equals("-useJunkCode"))
                                                                                            break block25;
                                                                                        useJunkCode = true;
                                                                                        break block19;
                                                                                    }
                                                                                    if (!arg.equals("-useSuperJunkCode"))
                                                                                        break block26;
                                                                                    useSuperJunkCode = true;
                                                                                    break block19;
                                                                                }
                                                                                if (!arg.equals("-useMoreJunkCode"))
                                                                                    break block27;
                                                                                useMoreJunkCode = true;
                                                                                break block19;
                                                                            }
                                                                            if (!arg.equals("-useMoreJunkCode2"))
                                                                                break block28;
                                                                            useMoreJunkCode2 = true;
                                                                            break block19;
                                                                        }
                                                                        if (!arg.equals("-useInvokeDynamicObf"))
                                                                            break block29;
                                                                        useInvokeDynamicObf = true;
                                                                        break block19;
                                                                    }
                                                                    if (!arg.equals("-useInvokeDynamicObfT"))
                                                                        break block30;
                                                                    useInvokeDynamicObfT = true;
                                                                    break block19;
                                                                }
                                                                if (!arg.equals("-useStringObf")) break block31;
                                                                useStringObf = true;
                                                                break block19;
                                                            }
                                                            if (!arg.equals("-useStringObfT")) break block32;
                                                            useStringObfT = true;
                                                            break block19;
                                                        }
                                                        if (!arg.equals("-useNumberObf")) break block33;
                                                        useNumberObf = true;
                                                        break block19;
                                                    }
                                                    if (!arg.equals("-obfLocalVar")) break block34;
                                                    obfLocalVar = true;
                                                    break block19;
                                                }
                                                if (!arg.equals("-delLocalVar")) break block35;
                                                delLocalVar = true;
                                                break block19;
                                            }
                                            if (!arg.equals("-fixVersion")) break block36;
                                            fixVersion = true;
                                            break block19;
                                        }
                                        if (!arg.equals("-addSyntheticFlag")) break block37;
                                        addSyntheticFlag = true;
                                        break block19;
                                    }
                                    if (!arg.equals("-classToFolder")) break block38;
                                    classToFolder = true;
                                    break block19;
                                }
                                if (!arg.equals("-reverse")) break block39;
                                useReverse = true;
                                break block19;
                            }
                            if (!arg.equals("-bigBrainNumberObf")) break block40;
                            bigBrainNumberObf = true;
                            break block19;
                        }
                        if (arg.equals("-applymap")) break block19;
                        if (!lastArg.equals("-applymap")) break block41;
                        DIYRemapStringsFile = new File(mapFolder, arg);
                        if (!DIYRemapStringsFile.exists()) {
                            File f = new File(mapFolder, "maps.txt");
                            f.createNewFile();
                            throw new BadException("ファイルが見つかりません" + arg + ": " + mapFolder + ": " + DIYRemapStringsFile);
                        }
                        remapStrings = MapReader.read(new String(Utils.ohmyGyatt(Files.newInputStream(DIYRemapStringsFile.toPath())), StandardCharsets.UTF_8));
                        break block19;
                    }
                    if (arg.equals("-asmVer")) break block19;
                    if (!lastArg.equals("-asmVer")) break block42;
                    switch (Integer.parseInt(arg)) {
                        case 4: {
                            int1 = 262144;
                            break block19;
                        }
                        case 5: {
                            int1 = 327680;
                            break block19;
                        }
                        case 6: {
                            int1 = 393216;
                            break block19;
                        }
                        case 7: {
                            int1 = 458752;
                            break block19;
                        }
                        case 8: {
                            int1 = 524288;
                            break block19;
                        }
                        case 9: {
                            int1 = 589824;
                            break block19;
                        }
                        case 10: {
                            int1 = 0x10A0000;
                            break block19;
                        }
                        default: {
                            throw new BadException("-asmVerの後のパラメータは、x∈{x|4≤x≤10, x∈N}（xは4から10の整数）である必要があります。あなたが入力したのは: " + arg);
                        }
                    }
                }
                if (exF) {
                    excludeClass.add(arg);
                } else if (inF) {
                    includeClass.add(arg);
                } else {
                    throw new BadException("argエラー " + arg);
                }
            }
            lastArg = arg;
        }
        if (DIYRemapStringsFile == null && obfLocalVar) {
            throw new BadException("-obfLocalVar error. -applyMap <map>");
        }
        read(args[0]);
        Main.startObf(args[0], args[1]);
        System.out.println();
        System.out.println("難読化が終了しました！");
        System.out.println();
        System.exit(0);
        final ClassWriter cw = new ClassWriter(3);
        cw.visit(52, 33, "test", null, "java/lang/Object", null);
        MethodVisitor mv = cw.visitMethod(9, "print", "(I)V", null, new String[]{"java/lang/Exception"});
        mv.visitFieldInsn(178, "java/lang/System", "out", "Ljava/io/PrintStream;");
        mv.visitTypeInsn(187, "java/lang/StringBuilder");
        mv.visitInsn(89);
        mv.visitMethodInsn(183, "java/lang/StringBuilder", "<init>", "()V", false);
        mv.visitLdcInsn("WhereAMom: ");
        mv.visitMethodInsn(182, "java/lang/StringBuilder", "append", "(Ljava/lang/String;)Ljava/lang/StringBuilder;", false);
        mv.visitVarInsn(21, 0);
        mv.visitMethodInsn(182, "java/lang/StringBuilder", "append", "(I)Ljava/lang/StringBuilder;", false);
        mv.visitMethodInsn(182, "java/lang/StringBuilder", "toString", "()Ljava/lang/String;", false);
        mv.visitMethodInsn(182, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
        mv.visitInsn(177);
        mv.visitMaxs(0, 0);
        mv.visitEnd();
        mv = cw.visitMethod(9, "run", "()V", null, new String[]{"java/lang/Throwable"});
        mv.visitInsn(5);
        mv.visitInvokeDynamicInsn(Utils.spawnRandomChar(), "(I)V", new Handle(6, "PikachuObf", "BROZOZOTOWN", "(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;", false), "SbUwU", "Main", "print", "(I)V");
        mv.visitInsn(177);
        mv.visitMaxs(0, 0);
        mv.visitEnd();
        new ClassLoader() {

            public Class<?> define(byte[] b) {
                return super.defineClass(null, cw.toByteArray(), 0, cw.toByteArray().length);
            }
        }.define(cw.toByteArray()).getDeclaredMethod("run").invoke(null);
    }

    private static void startObf(String in, String out) throws IOException {
        ZipEntry a;
        ClassWriter1 cw;
        ClassReader cr;
        ZipInputStream zipfile = new ZipInputStream(Files.newInputStream(Paths.get(in)));
        FileOutputStream fileout = new FileOutputStream(out);
        ZipOutputStream zzzz = new ZipOutputStream(fileout);
        ArrayList<ClassWriter1> writerList = new ArrayList<>();
        ArrayList<ClassReader> readerList = new ArrayList<>();
        String current = null;
        boolean temp1 = useStringObf;
        boolean temp2 = useStringObfT;
        boolean temp3 = useNumberObf;
        boolean temp4 = useJunkCode;
        boolean temp5 = addSyntheticFlag;
        boolean temp6 = delLocalVar;
        boolean temp7 = bigBrainNumberObf;
        boolean temp8 = useMoreJunkCode;
        useMoreJunkCode = true;
        bigBrainNumberObf = true;
        delLocalVar = true;
        addSyntheticFlag = true;
        useJunkCode = true;
        useNumberObf = true;
        useStringObfT = true;
        useStringObf = true;
        ArrayList<InputStream> isl = new ArrayList<>();
        isl.add(PikachuObf.class.getResourceAsStream("PikachuObf.class"));
        boolean flag = true;
        for (InputStream is : isl) {
            if (flag) {
                flag = false;
                cr = new ClassReader(is);
                cw = new ClassWriter1(cr, 3);
                cr.accept(new LocalVarTransformer(cr, cw, int1), 0);
                cr = new ClassReader(cw.toByteArray());
                cw = new ClassWriter1(cr, 3);
                cr.accept(new StringObfTransformer(cr, cw, int1), 0);
                cr = new ClassReader(cw.toByteArray());
                cw = new ClassWriter1(cr, 3);
                cr.accept(new StringNumberTransformer(cr, cw, int1, true), 0);
                cr = new ClassReader(cw.toByteArray());
                cw = new ClassWriter1(cr, 3);
                cr.accept(new SyntheticBridgeApplyerTransformer(cr, cw, int1), 0);
                cr = new ClassReader(cw.toByteArray());
                cw = new ClassWriter1(cr, 3);
                cr.accept(new PikachuStrReplacerObfVisitor(cr, cw, int1), 0);
            } else {
                cr = new ClassReader(is);
                cw = new ClassWriter1(cr, 3);
                cr.accept(cw, 0);
            }
            writerList.add(cw);
            readerList.add(cr);
        }
        useStringObf = temp1;
        useStringObfT = temp2;
        useNumberObf = temp3;
        useJunkCode = temp4;
        addSyntheticFlag = temp5;
        delLocalVar = temp6;
        bigBrainNumberObf = temp7;
        useMoreJunkCode = temp8;
        cr = new ClassReader(BadException.class.getResourceAsStream("BadException.class"));
        cw = new ClassWriter1(cr, 3);
        cr.accept(cw, 0);
        writerList.add(cw);
        readerList.add(cr);
        MyCL mycl2 = new MyCL();
        MyCL mycl3 = new MyCL();
        while ((a = zipfile.getNextEntry()) != null) {
            try {
                int b;
                ByteArrayOutputStream os = new ByteArrayOutputStream();
                while ((b = zipfile.read()) != -1) {
                    os.write(b);
                }
                ClassReader oldcr = null;
                ClassReader startcr = null;
                ClassWriter1 oldcw = null;
                ClassWriter1 startcw = null;
                if (a.getName().endsWith(".class")) {
                    int acc;
                    boolean isIn = false;
                    boolean isEx = false;
                    for (String inn : includeClass) {
                        if (!cr.getClassName().matches(inn) && !cr.getClassName().startsWith("PikachuObf")) continue;
                        isIn = true;
                        break;
                    }
                    for (String exx : excludeClass) {
                        if (!cr.getClassName().matches(exx)) continue;
                        isEx = true;
                        break;
                    }
                    System.out.println(cr.getClassName());
                    try {
                        startcr = cr = new ClassReader(os.toByteArray());
                        startcw = cw = new ClassWriter1(cr, 3);
                        oldcr = cr;
                        oldcw = cw;
                        cr.accept(new LocalVarTransformer(cr, cw, int1), 0);
                        Main.verify(new MyCL(), cw.toByteArray());
                    }
                    catch (Exclude | NotInclude e) {
                        cr = oldcr;
                        cw = oldcw;
                        System.out.println(" - Skip: " + e.getClass().getSimpleName());
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                        errors.add("[!] LocalVarTransformer: " + cr.getClassName() + " : " + e);
                        cr = oldcr;
                        cw = oldcw;
                    }
                    try {
                        oldcr = cr;
                        oldcw = cw;
                        cr = new ClassReader(cw.toByteArray());
                        cw = new ClassWriter1(cr, 3);
                        cr.accept(new SyntheticBridgeApplyerTransformer(cr, cw, int1), 0);
                        Main.verify(new MyCL(), cw.toByteArray());
                    }
                    catch (Exclude | NotInclude e) {
                        cr = oldcr;
                        cw = oldcw;
                        System.out.println(" - Skip: " + e.getClass().getSimpleName());
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                        errors.add("[!] SyntheticBridgeApplyerTransformer: " + cr.getClassName() + " : " + e);
                        cr = oldcr;
                        cw = oldcw;
                    }
                    if (useMoreJunkCode2) {
                        try {
                            oldcr = cr;
                            oldcw = cw;
                            cr = new ClassReader(cw.toByteArray());
                            cw = new ClassWriter1(cr, 3);
                            cr.accept(new More2JunkCodeTransformer(cr, cw, int1), 0);
                            Main.verify(new MyCL(), cw.toByteArray());
                        }
                        catch (Exclude | NotInclude e) {
                            cr = oldcr;
                            cw = oldcw;
                            System.out.println(" - Skip: " + e.getClass().getSimpleName());
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                            errors.add("[!] More2JunkCodeTransformer: " + cr.getClassName() + " : " + e);
                            cr = oldcr;
                            cw = oldcw;
                        }
                    }
                    if (useStringObfT && useStringObf) {
                        try {
                            oldcr = cr;
                            oldcw = cw;
                            cr = new ClassReader(cw.toByteArray());
                            cw = new ClassWriter1(cr, 3);
                            cr.accept(new StringObfTransformer(cr, cw, int1), 0);
                            Main.verify(new MyCL(), cw.toByteArray());
                        }
                        catch (Exclude | NotInclude e) {
                            cr = oldcr;
                            cw = oldcw;
                            System.out.println(" - Skip: " + e.getClass().getSimpleName());
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                            errors.add("[!] StringObfTransformer: " + cr.getClassName() + " : " + e);
                            cr = oldcr;
                            cw = oldcw;
                        }
                    }
                    if (useReverse) {
                        try {
                            oldcr = cr;
                            oldcw = cw;
                            cr = new ClassReader(cw.toByteArray());
                            cw = new ClassWriter1(cr, 3);
                            cr.accept(new ReverseTransformer(cr, cw, int1), 0);
                            Main.verify(new MyCL(), cw.toByteArray());
                        } catch (Exclude | NotInclude e) {
                            cr = oldcr;
                            cw = oldcw;
                            System.out.println(" - Skip: " + e.getClass().getSimpleName());
                        } catch (Exception e) {
                            e.printStackTrace();
                            errors.add("[!] ReverseTransformer: " + cr.getClassName() + " : " + e);
                            cr = oldcr;
                            cw = oldcw;
                        }
                    }
                    if (useSuperJunkCode) {
                        try {
                            oldcr = cr;
                            oldcw = cw;
                            cr = new ClassReader(cw.toByteArray());
                            cw = new ClassWriter1(cr, 3);
                            cr.accept(new SuperJunkCodeTransformer(cr, cw, int1), 0);
                            Main.verify(new MyCL(), cw.toByteArray());
                        }
                        catch (Exclude | NotInclude e) {
                            cr = oldcr;
                            cw = oldcw;
                            System.out.println(" - Skip: " + e.getClass().getSimpleName());
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                            errors.add("[!] SuperJunkCodeTransformer: " + cr.getClassName() + " : " + e);
                            cr = oldcr;
                            cw = oldcw;
                        }
                    }
                    try {
                        oldcr = cr;
                        oldcw = cw;
                        cr = new ClassReader(cw.toByteArray());
                        cw = new ClassWriter1(cr, 3);
                        cr.accept(new StringNumberTransformer(cr, cw, int1, true), 0);
                        Main.verify(new MyCL(), cw.toByteArray());
                    }
                    catch (Exclude | NotInclude e) {
                        cr = oldcr;
                        cw = oldcw;
                        System.out.println(" - Skip: " + e.getClass().getSimpleName());
                    }
                    catch (Throwable e1) {
                        cr = oldcr;
                        cw = oldcw;
                        try {
                            oldcr = cr;
                            oldcw = cw;
                            cr = new ClassReader(cw.toByteArray());
                            cw = new ClassWriter1(cr, 3);
                            cr.accept(new StringNumberTransformer(cr, cw, int1, false), 0);
                            Main.verify(new MyCL(), cw.toByteArray());
                        }
                        catch (Exclude | NotInclude e) {
                            cr = oldcr;
                            cw = oldcw;
                            System.out.println(" - Skip: " + e.getClass().getSimpleName());
                        }
                        catch (Throwable e) {
                            e.printStackTrace();
                            errors.add("[!] StringNumberTransformer: " + cr.getClassName() + " : " + e);
                            cr = oldcr;
                            cw = oldcw;
                        }
                    }
                    if (useInvokeDynamicObf) {
                        int acc2 = cr.getAccess();
                        if ((acc2 | 0x200) != acc2 && useInvokeDynamicObfT) {
                            try {
                                oldcr = cr;
                                oldcw = cw;
                                cr = new ClassReader(cw.toByteArray());
                                cw = new ClassWriter1(cr, 3);
                                cr.accept(new InvokeDynamicTransformer2(cr, cw, int1), 0);
                                Main.verify(new MyCL(), cw.toByteArray());
                            }
                            catch (Exclude | NotInclude e) {
                                cr = oldcr;
                                cw = oldcw;
                                System.out.println(" - Skip: " + e.getClass().getSimpleName());
                            }
                            catch (Exception e) {
                                e.printStackTrace();
                                errors.add("[!] InvokeDynamicTransformer: " + cr.getClassName() + " : " + e);
                                cr = oldcr;
                                cw = oldcw;
                            }
                        } else {
                            try {
                                oldcr = cr;
                                oldcw = cw;
                                cr = new ClassReader(cw.toByteArray());
                                cw = new ClassWriter1(cr, 3);
                                cr.accept(new InvokeDynamicTransformer(cr, cw, int1), 0);
                                Main.verify(new MyCL(), cw.toByteArray());
                            }
                            catch (Exclude | NotInclude e) {
                                cr = oldcr;
                                cw = oldcw;
                                System.out.println(" - Skip: " + e.getClass().getSimpleName());
                            }
                            catch (Exception e) {
                                e.printStackTrace();
                                errors.add("[!] InvokeDynamicTransformer: " + cr.getClassName() + " : " + e);
                                cr = oldcr;
                                cw = oldcw;
                            }
                        }
                    }
                    try {
                        Main.verify(mycl2, cw.toByteArray());
                    }
                    catch (Error | Exception e) {
                        e.printStackTrace();
                        errors.add("[!] Cant Obf " + cr.getClassName() + " : " + e);
                        cr = startcr;
                        cw = startcw;
                    }
                    writerList.add(cw);
                    readerList.add(cr);
                    continue;
                }
                try {
                    zzzz.putNextEntry(new ZipEntry(a.getName()));
                    zzzz.write(os.toByteArray());
                }
                catch (ZipException e) {
                    e.printStackTrace();
                }
            }
            catch (Exception e) {
                try {
                    zipfile.close();
                    zzzz.close();
                    fileout.close();
                }
                catch (IOException e1) {
                    e1.printStackTrace();
                }
                throw new RuntimeException(current, e);
            }
        }
        for (String errorss : errors) {
            System.out.println(errorss);
        }
        MyCL mycl = new MyCL();
        try {
            for (ClassWriter1 classWriter1 : writerList) {
                cr = new ClassReader(classWriter1.toByteArray());
                byte[] bytes = classWriter1.toByteArray();
                Main.verify(mycl, bytes);
                try {
                    zzzz.putNextEntry(new ZipEntry(cr.getClassName() + ".class" + (classToFolder ? "/" : "")));
                    zzzz.write(bytes);
                }
                catch (ZipException e1) {
                    e1.printStackTrace();
                }
            }
        }
        finally {
            try {
                zipfile.close();
                zzzz.close();
                fileout.close();
            }
            catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    private static void read(String string) {
        ZipInputStream zipfile = null;
        File file = new File(string);
        if (!file.exists()) {
            throw new RuntimeException(new FileNotFoundException(file.getAbsolutePath()));
        }
        try {
            urlcl = new MyURLClassLoader(new URL[]{file.toURL()}, Main.class.getClassLoader());
        }
        catch (Throwable e) {
            throw new RuntimeException(e);
        }
        finally {
            try {
                zipfile.close();
            }
            catch (Exception exception) {}
        }
    }

    public static void verify(MyCL mycl, byte[] bytes) {
        block12: {
            try {
                Method[] ms;
                block11: {
                    Class<?> clazz = mycl.define(bytes);
                    ms = clazz.getDeclaredMethods();
                    Object obj = null;
                    try {
                        obj = clazz.newInstance();
                    }
                    catch (Throwable e) {
                        if (e instanceof ClassFormatError || e instanceof VerifyError) {
                            if (e.getLocalizedMessage().startsWith("クラスファイルのバージョンはサポートされていません！")) {
                                throw new BadError("-fixVersionを追加すれば治るかも？", e);
                            }
                            throw (Error)e;
                        }
                        if (!(e instanceof BadException) && !(e instanceof IndexOutOfBoundsException)) break block11;
                        throw new BadException("unknown bad error: ", e);
                    }
                }
                for (Method m : ms) {
                    m.setAccessible(true);
                }
            }
            catch (Throwable e) {
                if (dontverify) {
                    e.printStackTrace();
                    return;
                }
                if (e instanceof BootstrapMethodError) {
                    e.printStackTrace();
                }
                if (e instanceof ClassFormatError || e instanceof VerifyError) {
                    if (e.getLocalizedMessage().startsWith("クラスファイルのバージョンはサポートされていません！")) {
                        throw new BadError("-fixVersionを追加すれば治るかも？", e);
                    }
                    throw (Error)e;
                }
                if (!(e instanceof BadException) && !(e instanceof IndexOutOfBoundsException)) break block12;
                throw new BadError("unknown bad error: ", e);
            }
        }
    }

    static {

        ArrayList<String> sl = new ArrayList<>();
        String[] sl2 = new String[]{"\uE794", "\uF229", "\uF770", "\u1A2C", "\uFD4B"};
        for (int i = 0; i < 3; ++i) {
            StringBuilder ss = new StringBuilder("PikachuObf");
            for (int j = 0; j < 150; ++j) {
                ss.append(Utils.getRandomMember(sl2));
            }
            sl.add(ss.toString());
        }
        pikachuStrs = sl.toArray(new String[0]);
        mapFolder = new File(new File(URLDecoder.decode(Main.class.getProtectionDomain().getCodeSource().getLocation().getPath(), StandardCharsets.UTF_8)).getParentFile(), "maps");
        if (!mapFolder.exists()) {
            mapFolder.mkdirs();
        }
        r = new Random();
        excludeClass = new ArrayList<>();
        includeClass = new ArrayList<>();
        dontEncode = false;
        useJunkCode = false;
        useMoreJunkCode = false;
        useMoreJunkCode2 = false;
        useSuperJunkCode = false;
        useInvokeDynamicObf = false;
        useInvokeDynamicObfT = false;
        useStringObf = false;
        useStringObfT = false;
        useNumberObf = false;
        useReverse = false;
        obfLocalVar = false;
        delLocalVar = false;
        fixVersion = false;
        addSyntheticFlag = false;
        dontverify = false;
        classToFolder = false;
        bigBrainNumberObf = false;
        int1 = 327680;
        exceptions = new Class[]{NullPointerException.class, OutOfMemoryError.class, StackOverflowError.class, RuntimeException.class, BadException.class, VerifyError.class, IllegalAccessError.class, IndexOutOfBoundsException.class, ClassFormatError.class, ConcurrentModificationException.class, IllegalClassFormatException.class, NoSuchElementException.class, ArithmeticException.class, UnsupportedOperationException.class};
    }
}