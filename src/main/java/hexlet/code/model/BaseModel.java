package hexlet.code.model;

import io.ebean.annotation.WhenCreated;

import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.time.Instant;
import java.util.UUID;

/**
 * @author andreiserov
 */
@MappedSuperclass
public class BaseModel {
    @Id
    private UUID id = UUID.randomUUID();
    @WhenCreated
    private Instant createdAt = Instant.now();

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
}
