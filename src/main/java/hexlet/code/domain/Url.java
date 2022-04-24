package hexlet.code.domain;

import hexlet.code.domain.finder.UrlFinder;
import io.ebean.annotation.Index;
import io.ebean.annotation.Platform;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.List;

/**
 * @author andreiserov
 */
@Entity
@Index(platforms = Platform.H2, unique = true, columnNames = {"name"})
@Index(platforms = Platform.POSTGRES, unique = true, name = "uq_printer_name", columnNames = {"lower(name)"})
public final class Url extends BaseModel {

    private String name;

    @OneToMany(cascade = CascadeType.ALL)
    private List<UrlCheck> checks;

    public static final UrlFinder find = new UrlFinder();

    public Url(String name) {
        super();
        this.name = name;
    }

    public List<UrlCheck> getChecks() {
        return checks;
    }

    public void setChecks(List<UrlCheck> checks) {
        this.checks = checks;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
