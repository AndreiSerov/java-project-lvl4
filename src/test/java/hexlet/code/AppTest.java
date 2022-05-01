package hexlet.code;

import hexlet.code.config.AppConfig;
import hexlet.code.domain.Url;
import hexlet.code.domain.query.QUrl;
import io.ebean.DB;
import io.ebean.Transaction;
import io.javalin.Javalin;
import kong.unirest.Unirest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


class AppTest {

    private static Javalin app;
    private static String baseUrl;
    private static String urlsUrl;
    private static Url exampleUrl;
    private static Transaction transaction;


    private static final String STUB_URL = "https://hexlet.io";

    @BeforeAll
    public static void beforeAll() {
        app = AppConfig.setup().start(0);

        int port = app.port();
        baseUrl = "http://localhost:" + port;
        urlsUrl = baseUrl + "/urls/";

        exampleUrl = new Url("https://www.example.com");
        exampleUrl.save();
    }

    @AfterAll
    public static void afterAll() {
        app.stop();
    }

    // В данном случае тесты не влияют друг на друга,
    // но при использовании БД запускать каждый тест в транзакции -
    // это хорошая практика
    @BeforeEach
    void beforeEach() {
        transaction = DB.beginTransaction();
    }

    @AfterEach
    void afterEach() {
        transaction.rollback();
    }

    @Nested
    class RootTest {

        @Test
        void testIndex() {
            var response = Unirest.get(baseUrl).asString();

            assertThat(response.getStatus()).isEqualTo(200);
            assertThat(response.getBody()).contains("Анализатор страниц");
        }
    }

    @Nested
    class UrlTest {

        @Test
        void testUrlList() {
            var response = Unirest.get(baseUrl).asString();

            assertThat(response.getStatus()).isEqualTo(200);
            assertThat(response.getBody()).contains(exampleUrl.getName());
        }

        @Test
        void testSingleUrlGet() {
            var response = Unirest.get(urlsUrl + exampleUrl.getId()).asString();

            assertThat(response.getStatus()).isEqualTo(200);
            assertThat(response.getBody())
                .contains(exampleUrl.getName())
                .contains(exampleUrl.getId().toString());
        }

        @Test
        void testAddUrl() {
            var postResponse = Unirest
                .post(baseUrl + "/urls")
                .field("url", STUB_URL)
                .asEmpty();

            assertThat(postResponse.getStatus()).isEqualTo(302);

            var response = Unirest.get(urlsUrl).asString();

            assertThat(response.getStatus()).isEqualTo(200);
            assertThat(response.getBody()).contains("Страница успешно добавлена");
            assertThat(new QUrl().name.eq(STUB_URL)).isNotNull();

            Unirest
                .post(baseUrl + "/urls")
                .field("url", STUB_URL)
                .asEmpty();

            response = Unirest.get(urlsUrl).asString();
            assertThat(response.getBody()).contains("Страница уже существует");
        }

        @Test
        void testAddUrlwhenInvalidUrl() {
            Unirest
                .post(baseUrl + "/urls")
                .field("url", "INVALID_URL")
                .asEmpty();

            var response = Unirest.get(urlsUrl).asString();

            assertThat(response.getStatus()).isEqualTo(200);
            assertThat(response.getBody()).contains("Некорректно указан URL");
        }

        @Test
        void testCheckUrl() {
            var postResponse = Unirest
                .post(baseUrl + "/urls/" + exampleUrl.getId() + "/checks")
                .field("url", STUB_URL)
                .asEmpty();

            assertThat(postResponse.getStatus()).isEqualTo(302);

            var response = Unirest.get(urlsUrl).asString();

            assertThat(response.getStatus()).isEqualTo(200);
            assertThat(response.getBody()).contains("Страница успешно проверена");
            assertThat(new QUrl().name.eq(STUB_URL)).isNotNull();

            Unirest
                .post(baseUrl + "/urls/" + exampleUrl.getId() + "/checks")
                .field("url", "NOT URL")
                .asEmpty();

            assertThat(Unirest.get(urlsUrl).asString().getBody()).contains("Не удалось проверить страницу");

        }
    }
}
