package hexlet.code.controller;

import hexlet.code.domain.Url;
import hexlet.code.domain.UrlCheck;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import io.javalin.http.NotFoundResponse;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import kong.unirest.UnirestException;
import org.jetbrains.annotations.Nullable;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

import static java.util.Objects.requireNonNull;

/**
 * @author andreiserov
 */
public class UrlController {

    public static final Handler GET_URLS = ctx -> {
        List<Url> urls = Url.FIND.all();

        ctx.attribute("urls", urls);
        ctx.render("urls/urls.html");
    };

    public static final Handler GET_URL_BY_ID = ctx -> {
        Url url = Url.FIND.byId(getId(ctx));

        ctx.attribute("url", url);
        ctx.render("urls/url.html");
    };


    public static final Handler CREATE_URL = ctx -> {
        final URL formName;
        try {
            formName = new URL(requireNonNull(ctx.formParam("url")));
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
        Url url = requireNonNull(
            Url.FIND.byId(getId(ctx)),
            "Url does not exist in db");

        try {
            HttpResponse<String> response = Unirest
                .get(url.getName())
                .asString();

            Document document = Jsoup.parse(response.getBody());

            final UrlCheck urlCheck = new UrlCheck.Builder()
                .statusCode(response.getStatus())
                .tittle(document.title())
                .h1(getElementValue(document, "h1", Element::text))
                .description(getElementValue(document, "meta[name=description]", it -> it.attr("content")))
                .url(url)
                .build();

            urlCheck.save();

            ctx.sessionAttribute("flash", "Страница успешно проверена");
            ctx.sessionAttribute("flash-type", "success");
        } catch (UnirestException ex) {
            ctx.sessionAttribute("flash", "Не удалось проверить страницу");
            ctx.sessionAttribute("flash-type", "danger");
        }

        ctx.redirect("/urls/" + url.getId());
    };

    @Nullable
    private static String getElementValue(Document document, String elem, Function<Element, String> mapTo) {
        return Optional.ofNullable(document.selectFirst(elem)).map(mapTo).orElse(null);
    }

    private static UUID getId(Context ctx) {
        return Optional.ofNullable(ctx.pathParamAsClass("id", UUID.class).getOrDefault(null))
            .orElseThrow(NotFoundResponse::new);
    }
}

