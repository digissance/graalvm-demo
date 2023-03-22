package biz.digissance.graalvmdemo.jpa.party.role;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaPartyRoleRepository extends JpaRepository<JpaPartyRole, Long> {

    Optional<JpaPartyRole> findByIdentifier(String identifier);
}
