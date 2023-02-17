package biz.digissance.graalvmdemo.jpa;

import jakarta.persistence.PostLoad;
import jakarta.persistence.PostPersist;
import jakarta.persistence.PostRemove;
import jakarta.persistence.PostUpdate;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreRemove;
import jakarta.persistence.PreUpdate;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Configurable;

@Slf4j
@Configurable
public class BaseEntityListener {

    private final ApplicationEventPublisherSupplier handler = ApplicationEventPublisherSupplier.INSTANCE;

    @PrePersist
    @PreUpdate
    @PreRemove
    private void beforeAnyUpdate(BaseEntity baseEntity) {
        if (Objects.isNull(baseEntity.getId())) {
            log.info("[ENTITY AUDIT] About to add a entity" +this);
        } else {
            log.info("[ENTITY AUDIT] About to update/delete entity: " + baseEntity.getId());
        }
    }

    //    @PostPersist
    @PostUpdate
    @PostRemove
    private void afterAnyUpdate(BaseEntity baseEntity) {
        log.info("[ENTITY AUDIT] add/update/delete complete for message: " + baseEntity.getId());
    }

    @PostPersist
    private void afterPersist(BaseEntity baseEntity) {
        log.info("[ENTITY AUDIT] About to publish event..." +this);
        handler.getPublisher().publishEvent(new EntityCreated(baseEntity));
    }

    @PostLoad
    private void afterLoad(BaseEntity baseEntity) {
        log.info("[ENTITY AUDIT] entity loaded from database: " + baseEntity.getId());
    }
}
