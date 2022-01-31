package hexlet.code;

import hexlet.code.config.AppConfig;

/**
 * @author andreiserov
 */
public class App {

    public static void main(String[] args) {
        AppConfig.setup().start();
    }
}
