package devs.pikachu.protect.utility;

//プログラミングのテスト
public class TestUtils {
    public static int vmInteger() {
        int x = 69;
        int y = 2 | x;
        return y & 1;
    }

    //table switch
    public static long vmLong() {
        int a = 0;
        int b = 3;
        switch (a + b) {
            case 3 -> {
                return 10L;
            }
            case 4 -> {
                return 120L;
            }
            case 5 -> {
                return 240L;
            }
            default -> {
                return 9L;
            }
        }
    }
}
