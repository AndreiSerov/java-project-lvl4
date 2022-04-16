package hexlet.code;

import io.javalin.Javalin;
import io.javalin.http.Handler;

import static hexlet.code.controller.UrlController.CREATE_URL;
import static hexlet.code.controller.UrlController.GET_URLS;
import static hexlet.code.controller.UrlController.GET_URL_BY_ID;

/**
 * @author andreiserov
 */
public class Router {


    private static final Handler INDEX = ctx -> ctx.render("index.html");

    public static void register(Javalin app) {
        app
            .get("/", INDEX)
            .post("/urls", CREATE_URL)
            .get("/urls", GET_URLS)
            .get("/urls/{id}", GET_URL_BY_ID);
    }
}
