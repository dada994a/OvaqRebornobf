package devs.pikachu.protect.utility;

import lombok.Getter;
import lombok.SneakyThrows;
import org.simpleyaml.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ConfigUtils {
    private static final YamlConfiguration config = new YamlConfiguration();

    @SneakyThrows
    public static void loadCFG(String fileName) {
        try {
            config.load(new File(fileName));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void saveCFG() {
        try {
            InputStream cfg = ConfigUtils.class.getResourceAsStream("../../config.yml");
            FileOutputStream outputStream = new FileOutputStream("config.yml");
            outputStream.write(cfg.read());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static YamlConfiguration getConfig() {
        return config;
    }
}
