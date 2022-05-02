package hexlet.code.domain;

import io.ebean.Model;
import io.ebean.annotation.WhenCreated;

import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.time.Instant;

/**
 * @author andreiserov
 */
@MappedSuperclass
public class BaseModel extends Model {
    @Id
    private Long id;

    @WhenCreated
    private Instant createdAt = Instant.now();

    public final Long getId() {
        return id;
    }

    public final void setId(Long id) {
        this.id = id;
    }

    public final Instant getCreatedAt() {
        return createdAt;
    }

    public final void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
}
