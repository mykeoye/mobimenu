package io.mobimenu.persistence.entity;

import lombok.ToString;
import java.time.Instant;
import io.quarkus.mongodb.panache.reactive.ReactivePanacheMongoEntity;

@ToString
public class BaseEntity extends ReactivePanacheMongoEntity {
    public Instant created = Instant.now();
    public Instant updated;
    public boolean deleted;
}
