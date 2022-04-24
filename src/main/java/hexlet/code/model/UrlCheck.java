package hexlet.code.model;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * @author andreiserov
 */
@Entity
public final  class UrlCheck extends BaseModel {

    private Integer statusCode;

    private String tittle;

    private String h1;

    private String description;

    @ManyToOne
    @JoinColumn(name = "url_id")
    private Url url;

    public UrlCheck(Integer statusCode, String tittle, String h1, String description) {
        super();
        this.statusCode = statusCode;
        this.tittle = tittle;
        this.h1 = h1;
        this.description = description;
    }

    public Url getUrl() {
        return url;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public String getTittle() {
        return tittle;
    }

    public void setTittle(String tittle) {
        this.tittle = tittle;
    }

    public String getH1() {
        return h1;
    }

    public void setH1(String h1) {
        this.h1 = h1;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setUrl(Url url) {
        this.url = url;
    }
}
