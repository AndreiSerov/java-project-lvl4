package hexlet.code.model;

import javax.persistence.Entity;

/**
 * @author andreiserov
 */
@Entity
public class Url extends BaseModel {
    private String name;

    public Url(String name) {
        super();
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
