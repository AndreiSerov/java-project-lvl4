package hexlet.code;

import hexlet.code.config.AppConfig;
import hexlet.code.domain.Url;
import io.ebean.DB;
import io.ebean.Transaction;
import io.javalin.Javalin;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


class AppTest {

    private static Javalin app;
    private static String baseUrl;
    private static String urlsUrl;
    private static Url exampleUrl;
    private static Transaction transaction;

    private static MockWebServer mockWebServer;


    private static final String STUB_URL = "https://hexlet.io";

    @BeforeAll
    public static void beforeAll() throws IOException {
        app = AppConfig.setup().start(0);

        int port = app.port();
        baseUrl = "http://localhost:" + port;
        urlsUrl = baseUrl + "/urls/";

        exampleUrl = new Url("https://www.kalia-balia.com");
        exampleUrl.save();

        mockWebServer = new MockWebServer();
        mockWebServer.enqueue(new MockResponse().setBody("MOCK RESPONSE"));
        mockWebServer.start();
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
            var response = Unirest.get(baseUrl + "/urls").asString();

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
            var postResponse = postUrl(baseUrl + "/urls", STUB_URL);

            assertThat(postResponse.getStatus()).isEqualTo(302);

            var response = Unirest.get(urlsUrl).asString();

            assertThat(response.getStatus()).isEqualTo(200);
            assertThat(response.getBody()).contains("Страница успешно добавлена");
            assertThat(Url.FIND.byName(STUB_URL).getId()).isNotNull();

            postUrl(baseUrl + "/urls", STUB_URL);

            response = Unirest.get(urlsUrl).asString();
            assertThat(response.getBody()).contains("Страница уже существует");
        }

        @Test
        void testAddUrlwhenInvalidUrl() {
            postUrl(baseUrl + "/urls", "INVALID_URL");

            var response = Unirest.get(urlsUrl).asString();

            assertThat(response.getStatus()).isEqualTo(200);
            assertThat(response.getBody()).contains("Некорректно указан URL");
        }

        @Test
        void testCheckUrl() {

            String urlForCheck = mockWebServer.url("").toString();
            urlForCheck = urlForCheck.substring(0, urlForCheck.length() - 1);
            postUrl(baseUrl + "/urls", urlForCheck);

            var postResponse = postUrl(baseUrl + "/urls/" + Url.FIND.byName(urlForCheck).getId() + "/checks", STUB_URL);

            assertThat(postResponse.getStatus()).isEqualTo(302);

            var response = Unirest.get(urlsUrl).asString();

            assertThat(response.getStatus()).isEqualTo(200);
            assertThat(response.getBody()).contains("Страница успешно проверена");
        }
    }

    @SuppressWarnings("rawtypes")
    private HttpResponse postUrl(String baseUrl, String stubUrl) {
        return Unirest
            .post(baseUrl)
            .field("url", stubUrl)
            .asEmpty();
    }
}
