package biz.digissance.graalvmdemo.jpa;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonRepository extends JpaRepository<PersonEntity, PartyPK> {
    Optional<PersonEntity> findByIdentifier(String identifier);
}
