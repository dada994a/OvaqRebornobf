import java.net.URL;
import java.net.URLClassLoader;

public class MyURLClassLoader extends URLClassLoader {
    public MyURLClassLoader(URL[] urls, ClassLoader parent) {
        super(urls, parent);
    }

    @Override
    public void addURL(URL url) {
        super.addURL(url);
    }

    public Class<?> defineClass(byte[] b) {
        return super.defineClass(b, 0, b.length);
    }
}
