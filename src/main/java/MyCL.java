public class MyCL extends ClassLoader {
    public MyCL() {
    }

    public Class<?> define(byte[] b) {
        return super.defineClass(b, 0, b.length);
    }
}