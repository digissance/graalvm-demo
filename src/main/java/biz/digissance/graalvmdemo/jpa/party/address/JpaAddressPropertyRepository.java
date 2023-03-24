package biz.digissance.graalvmdemo.jpa.party.address;

import biz.digissance.graalvmdemo.jpa.party.address.JpaAddressProperty;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaAddressPropertyRepository extends JpaRepository<JpaAddressProperty, Long> {

    Optional<JpaAddressProperty> findByAddress_Address(String address);
}
