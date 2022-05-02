package hexlet.code.controller;

import io.javalin.http.Handler;

/**
 * @author andreiserov
 */
public class IndexController {

    public static final Handler INDEX = ctx -> ctx.render("index.html");
}
