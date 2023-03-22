package biz.digissance.graalvmdemo.jpa.party.role;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaPartyRoleTypeRepository extends JpaRepository<JpaPartyRoleType, Long> {

    Optional<JpaPartyRoleType> findByName(String name);
}
