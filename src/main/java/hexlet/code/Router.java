package hexlet.code;

import io.javalin.Javalin;
import io.javalin.http.Handler;

/**
 * @author andreiserov
 */
public class Router {

    private static final Handler INDEX = ctx -> ctx.render("index.html");
    private static final Handler TEST = ctx -> ctx.render("test.html");
//    private static final Handler CREATE_URL = ctx -> {
//        ctx.formParam("")
//    } TODO

    public static void register(Javalin app) {
        app
            .get("/hello", ctx -> ctx.result("Hello World"))
            .get("/", INDEX)
            .get("/test", TEST);
//            .post
    }
}
