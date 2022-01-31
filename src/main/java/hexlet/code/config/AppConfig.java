package hexlet.code.config;

import hexlet.code.Router;
import io.javalin.Javalin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

/**
 * @author andreiserov
 */
public class AppConfig {
    private static final Logger LOG = LoggerFactory.getLogger(AppConfig.class);


    private static final String HEROKU_PORT = System.getenv("PORT");
    private static final int PORT = (HEROKU_PORT != null) ? Integer.parseInt(HEROKU_PORT) : 7000;

    private static final Javalin APP =  Javalin.create(config -> {
        config.enableDevLogging();
        config.requestLogger((ctx, ms) -> {
            LOG.info("Incoming request body is: {}.", ctx.body());
            LOG.info("Request processed in {} ms.", ms);
        });
    });

    public static Javalin setup() {
        Objects.requireNonNull(APP.jettyServer()).setServerPort(PORT);
        Router.register(APP);
        return APP;
    }
}
