package hexlet.code.controller;

import hexlet.code.model.Url;
import hexlet.code.model.UrlCheck;
import hexlet.code.model.query.QUrl;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import io.javalin.http.NotFoundResponse;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import kong.unirest.UnirestException;

import javax.persistence.EntityNotFoundException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

/**
 * @author andreiserov
 */
public class UrlController {

    private static final QUrl Q_URL = new QUrl();

    public static final Handler GET_URLS = ctx -> {
        List<Url> urls = Q_URL.findList();
        ctx.attribute("urls", urls);
        ctx.render("urls/urls.html");
    };

    public static final Handler GET_URL_BY_ID = ctx -> {

        UUID id = getId(ctx);

        Url url = Q_URL.id.eq(id).findOne();

        ctx.attribute("url", url);
        ctx.render("urls/url.html");
    };


    public static final Handler CREATE_URL = ctx -> {
        final URL formName;
        try {
            formName = new URL(Objects.requireNonNull(ctx.formParam("url")));
        } catch (MalformedURLException | NullPointerException e) {
            ctx.sessionAttribute("flash", "Некорректно указан URL");
            ctx.sessionAttribute("flash-type", "danger");
            ctx.redirect("/");

            throw new RuntimeException("url name doesn't match pattern or null");
        }

        final Url url = new Url(formName.getProtocol() + "://" + formName.getAuthority());

        try {
            url.save();

            ctx.sessionAttribute("flash", "Страница успешно добавлена");
            ctx.sessionAttribute("flash-type", "success");
        } catch (Exception e) {
            ctx.sessionAttribute("flash", "Страница уже существует");
            ctx.sessionAttribute("flash-type", "info");
        }


        ctx.redirect("/urls");
    };

    public static final Handler CHECK_URL = ctx -> {
        UUID id = getId(ctx);
        Url url = Optional.ofNullable(Q_URL.id.eq(id).findOne())
            .orElseThrow(EntityNotFoundException::new);

        try {
            HttpResponse<String> response = Unirest.get(url.getName()).asString();

            final UrlCheck urlCheck = new UrlCheck.Builder()
                .statusCode(response.getStatus())
                .url(url)
                .build();

            urlCheck.save();

            ctx.sessionAttribute("flash", "Страница успешно проверена");
            ctx.sessionAttribute("flash-type", "success");

        } catch (UnirestException ex) {
            ctx.sessionAttribute("flash", "Не удалось проверить страницу");
            ctx.sessionAttribute("flash-type", "danger");
        }

        ctx.redirect("/urls/" + id);
    };

    private static UUID getId(Context ctx) {
        return Optional.ofNullable(ctx.pathParamAsClass("id", UUID.class).getOrDefault(null))
            .orElseThrow(NotFoundResponse::new);
    }
}
