package biz.digissance.graalvmdemo.jpa.base;

import biz.digissance.graalvmdemo.jpa.event.ApplicationEventPublisherSupplier;
import biz.digissance.graalvmdemo.jpa.event.EntityCreated;
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
    private void beforeAnyUpdate(BaseEntity baseEntity) {
        if (Objects.isNull(baseEntity.getId())) {
            log.info("[ENTITY AUDIT] About to create entity: " + baseEntity);
        } else {
            log.info("[ENTITY AUDIT] About to update entity: " + baseEntity);
        }
    }

    @PreRemove
    private void beforeAnyDelete(BaseEntity baseEntity) {
        log.info("[ENTITY AUDIT] About to delete entity: " + baseEntity);
    }

    @PostPersist
    private void afterPersist(BaseEntity baseEntity) {
        log.info("[ENTITY AUDIT] create complete for entity: " + baseEntity);
        log.info("[ENTITY AUDIT] About to publish event..." + this);
        handler.getPublisher().publishEvent(new EntityCreated(baseEntity));
    }

    @PostUpdate
    private void afterAnyUpdate(BaseEntity baseEntity) {
        log.info("[ENTITY AUDIT] update complete for entity: " + baseEntity);
    }

    @PostRemove
    private void afterAnyRemove(BaseEntity baseEntity) {
        log.info("[ENTITY AUDIT] delete complete for entity: " + baseEntity);
    }

    @PostLoad
    private void afterLoad(BaseEntity baseEntity) {
        log.info("[ENTITY AUDIT] entity loaded from database: " + baseEntity);
    }
}
