package hexlet.code.model;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;

/**
 * @author andreiserov
 */
@Entity
public final  class UrlCheck extends BaseModel {

    private final Integer statusCode;

    private final String title;

    private final String h1;

    @Lob
    private final String description;

    @ManyToOne
    @JoinColumn(name = "url_id")
    private final Url url;

    public UrlCheck(Integer statusCode, String title, String h1, String description, Url url) {
        super();
        this.statusCode = statusCode;
        this.title = title;
        this.h1 = h1;
        this.description = description;
        this.url = url;
    }

    public Url getUrl() {
        return url;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public String getTitle() {
        return title;
    }

    public String getH1() {
        return h1;
    }

    public String getDescription() {
        return description;
    }


    public static class Builder {

        private Integer statusCode;
        private String title;
        private String h1;
        private String description;
        private Url url;

        public Builder statusCode(Integer statusCode) {
            this.statusCode = statusCode;
            return this;
        }

        public Builder tittle(String tittle) {
            this.title = tittle;
            return this;
        }

        public Builder h1(String h1) {
            this.h1 = h1;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder url(Url url) {
            this.url = url;
            return this;
        }

        public UrlCheck build() {
            return new UrlCheck(statusCode, title, h1, description, url);
        }
    }
}
