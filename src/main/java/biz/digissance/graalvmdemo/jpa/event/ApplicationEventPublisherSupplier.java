package biz.digissance.graalvmdemo.jpa.event;

import org.springframework.context.ApplicationEventPublisher;

public enum ApplicationEventPublisherSupplier {
    INSTANCE;

    private ApplicationEventPublisher publisher;

    public ApplicationEventPublisher getPublisher(){
        return this.publisher;
    }

    public void setPublisher(final ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }
}
