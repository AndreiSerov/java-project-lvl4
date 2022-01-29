package hexlet.code;

import io.javalin.Javalin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author andreiserov
 */
public class App {

    private static final Logger LOG = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) {
        Javalin app = getApp();
        app.get("/", ctx -> ctx.result("Hello World"))
            .start(getHerokuAssignedPort());
    }

    private static Javalin getApp() {
        return Javalin.create(config -> {
            config.enableDevLogging();
            config.requestLogger((ctx, ms) -> {
                LOG.info("Incoming request body is: {}.", ctx.body());
                LOG.info("Request processed in {} ms.", ms);
            });
        });
    }

    private static int getHerokuAssignedPort() {
        String herokuPort = System.getenv("PORT");
        if (herokuPort != null) {
            return Integer.parseInt(herokuPort);
        }
        return 7000;
    }



}
