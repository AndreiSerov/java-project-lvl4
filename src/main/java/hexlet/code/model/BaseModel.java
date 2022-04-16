package hexlet.code.model;

import io.ebean.Model;
import io.ebean.annotation.WhenCreated;

import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.time.Instant;
import java.util.UUID;

/**
 * @author andreiserov
 */
@MappedSuperclass
public class BaseModel extends Model {
    @Id
    private UUID id = UUID.randomUUID();
    @WhenCreated
    private Instant createdAt = Instant.now();

    public final  UUID getId() {
        return id;
    }

    public final void setId(UUID id) {
        this.id = id;
    }

    public final Instant getCreatedAt() {
        return createdAt;
    }

    public final void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
}
