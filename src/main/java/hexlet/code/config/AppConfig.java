package hexlet.code.config;

import hexlet.code.Router;
import io.javalin.Javalin;
import io.javalin.core.validation.JavalinValidation;
import io.javalin.plugin.rendering.template.JavalinThymeleaf;
import nz.net.ultraq.thymeleaf.layoutdialect.LayoutDialect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.extras.java8time.dialect.Java8TimeDialect;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import java.util.Objects;
import java.util.UUID;

/**
 * @author andreiserov
 */
public class AppConfig {

    private static final Logger LOG = LoggerFactory.getLogger(AppConfig.class);


    private static final String HEROKU_PORT = System.getenv("PORT");
    private static final int PORT = (HEROKU_PORT != null) ? Integer.parseInt(HEROKU_PORT) : 7000;

    private static final Javalin APP =  Javalin.create(config -> {
        config.enableDevLogging();

        config.enableWebjars();
        JavalinThymeleaf.configure(getTemplateEngine());

        config.requestLogger((ctx, ms) -> {
            LOG.info("Incoming request body is: {}.", ctx.body());
            LOG.info("Request processed in {} ms.", ms);
        });
    });


    public static Javalin setup() {
        Objects.requireNonNull(APP.jettyServer()).setServerPort(PORT);
        Router.register(APP);
        JavalinValidation.register(UUID.class, UUID::fromString);

        APP.before(ctx -> ctx.attribute("ctx", ctx));

        return APP;
    }

    private static TemplateEngine getTemplateEngine() {
        TemplateEngine templateEngine = new TemplateEngine();

        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setPrefix("/templates/");

        templateEngine.addTemplateResolver(templateResolver);
        templateEngine.addDialect(new LayoutDialect());
        templateEngine.addDialect(new Java8TimeDialect());

        return templateEngine;
    }
}
