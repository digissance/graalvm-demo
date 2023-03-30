package biz.digissance.graalvmdemo.jpa.acl;

import biz.digissance.graalvmdemo.jpa.base.BaseEntity;
import biz.digissance.graalvmdemo.jpa.event.EntityCreated;
import jakarta.persistence.PrePersist;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Service
public class EntityAclListener {

//        @TransactionalEventListener
//    @Transactional(propagation = Propagation.REQUIRES_NEW)
//    @EventListener
    public void processCustomerCreatedEvent(EntityCreated event) {
        log.info("Event received: " + event);
    }

    @TransactionalEventListener
    public void setAclBeforePersist(EntityCreated entity) {
        log.info("Event received: " + entity);
//        List<String> acls = new ArrayList<>();
//        String authId = auditorAware.getCurrentAuditor()
//                .orElseThrow(() -> new IllegalStateException("no auditor available"));
//        acls.add(authId + ":READ_WRITE");
//        acls.add("ADMIN:READ_WRITE");
//        entity.setAcl(acls.toArray(new String[0]));
    }
}
