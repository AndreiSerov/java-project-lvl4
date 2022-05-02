package hexlet.code;

import hexlet.code.config.AppConfig;
import io.javalin.Javalin;

/**
 * @author andreiserov
 */
public class App {

    public static void main(String[] args) {
        getApp().start();
    }

    public static Javalin getApp() {
        return AppConfig.setup();
    }
}
