package biz.digissance.graalvmdemo.jpa;

import java.util.UUID;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.enhanced.SequenceStyleGenerator;

public class UUIDSequenceIdGenerator extends SequenceStyleGenerator {

    @Override
    public Object generate(final SharedSessionContractImplementor session, final Object object)
            throws HibernateException {
        return UUID.randomUUID().toString();
    }
}
