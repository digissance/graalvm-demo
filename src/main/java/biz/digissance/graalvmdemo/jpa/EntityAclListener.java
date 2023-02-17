package biz.digissance.graalvmdemo.jpa;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class EntityAclListener {

    //    @TransactionalEventListener
//    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @EventListener
    public void processCustomerCreatedEvent(EntityCreated event) {
        log.info("Event received: " + event);
    }
}
