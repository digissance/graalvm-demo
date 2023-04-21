package biz.digissance.graalvmdemo.domain.party;

import biz.digissance.graalvmdemo.domain.party.authentication.EmailPasswordAuthentication;
import biz.digissance.graalvmdemo.domain.party.authentication.OidcAuthentication;
import biz.digissance.graalvmdemo.http.OidcRegisterRequest;
import biz.digissance.graalvmdemo.http.RegisterRequest;
import biz.digissance.graalvmdemo.jpa.party.JpaParty;
import biz.digissance.graalvmdemo.jpa.party.JpaPartyRepository;
import biz.digissance.graalvmdemo.jpa.party.PartyMapper;
import biz.digissance.graalvmdemo.jpa.party.address.JpaAddressProperty;
import biz.digissance.graalvmdemo.jpa.party.address.JpaEmailAddress;
import biz.digissance.graalvmdemo.jpa.party.authentication.JpaPartyAuthentication;
import biz.digissance.graalvmdemo.jpa.party.person.JpaPerson;
import biz.digissance.graalvmdemo.jpa.party.person.JpaPersonName;
import biz.digissance.graalvmdemo.jpa.party.person.JpaPersonRepository;
import biz.digissance.graalvmdemo.jpa.party.role.JpaPartyRole;
import biz.digissance.graalvmdemo.jpa.party.role.JpaPartyRoleTypeRepository;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import net.liccioni.archetypes.party.Party;
import net.liccioni.archetypes.party.Person;
import net.liccioni.archetypes.uniqueid.UniqueIdentifier;

public class DomainPersonRepository implements PersonRepository {
    private final JpaPersonRepository repository;
    private final JpaPartyRepository partyRepository;
    private final JpaPartyRoleTypeRepository partyRoleTypeRepository;
    private final PartyMapper mapper;

    public DomainPersonRepository(final JpaPersonRepository jpaPersonRepository,
                                  final JpaPartyRepository partyRepository,
                                  final JpaPartyRoleTypeRepository partyRoleTypeRepository,
                                  final PartyMapper mapper) {
        this.repository = jpaPersonRepository;
        this.partyRepository = partyRepository;
        this.partyRoleTypeRepository = partyRoleTypeRepository;
        this.mapper = mapper;
    }

    @Override
    public Party save(final Party person) {

        final var jpaPerson = Optional.ofNullable(person.getIdentifier())
                .map(UniqueIdentifier::getId)
                .flatMap(partyRepository::findByIdentifier)
                .map(p -> mapper.toPartyJpaForUpdate(person, p, p)).orElseGet(() -> mapper.toPartyJpa(person));
        final var savedJpa = partyRepository.save(jpaPerson);
        return mapper.toPartyDomain(savedJpa);
    }

    @Override
    public List<Person> findAll() {
        return repository.findAll().stream()
                .map((JpaPerson personDto) -> {
                    return (Person) mapper.toPartyDomain(personDto);
                }).collect(Collectors.toList());
    }

    @Override
    public Optional<Person> findByIdentifier(final String identifier) {
        return repository.findByIdentifier(identifier).map((JpaPerson personDto) -> {
            return (Person) mapper.toPartyDomain(personDto);
        });
    }

    @Override
    public Optional<Party> findByAuthenticationUserName(final String username) {
        return partyRepository.findPartyByUsername(username).map(mapper::toPartyDomain);
    }

    @Override
    public Party save(final RegisterRequest registerRequest, final String password) {
        final var person = partyRepository.findPartyByUsername(registerRequest.getEmail())
                .orElseGet(() -> mapper.toPartyJpa(mapper.toPersonDomain(registerRequest, password)));
        final var auths = person.getAuthentications().stream()
                .map(mapper::toAuthDomain)
                .collect(Collectors.toSet());
        final var newAuth = EmailPasswordAuthentication.builder()
                .emailAddress(registerRequest.getEmail())
                .password(password)
                .build();
        if (auths.contains(newAuth)) {
            throw new IllegalArgumentException("Cannot register again");
        }
        final var authentication = mapper.toAuthJpa(newAuth);
        authentication.setParty(person);
        person.getAuthentications().add(authentication);
        return mapper.toPartyDomain(partyRepository.save(person));
    }

    @Override
    public Party save(final OidcRegisterRequest registerRequest) {
        final var person = partyRepository.findPartyByUsername(registerRequest.getEmail())
                .orElseGet(() -> this.toPartyJpa(registerRequest));
        final var auths = person.getAuthentications().stream().map(mapper::toAuthDomain).collect(Collectors.toSet());
        final var newAuth = OidcAuthentication.builder()
                .provider(registerRequest.getProvider())
                .username(registerRequest.getEmail())
                .build();
        if (auths.contains(newAuth)) {
            return mapper.toPartyDomain(person);
        }
        final var authentication = mapper.toAuthJpa(newAuth);
        authentication.setParty(person);
        person.getAuthentications().add(authentication);
        return mapper.toPartyDomain(partyRepository.save(person));
    }

    private JpaParty toPartyJpa(final OidcRegisterRequest registerRequest) {
        final var emailAddress = new JpaEmailAddress();
        emailAddress.setEmailAddress(registerRequest.getEmail());
        final var jpaPerson = new JpaPerson();
        final var jpaPersonName = new JpaPersonName();
        jpaPersonName.setGivenName(registerRequest.getFirstName());
        jpaPersonName.setFamilyName(registerRequest.getLastName());
        jpaPerson.setPersonName(jpaPersonName);
        final var jpaAddressProperty = new JpaAddressProperty();
        jpaAddressProperty.setParty(jpaPerson);
        jpaAddressProperty.setUse(Set.of("authentication"));
        jpaAddressProperty.setAddress(emailAddress);
        jpaPerson.getAddressProperties().add(jpaAddressProperty);
        final var jpaRole = new JpaPartyRole();
        jpaRole.setParty(jpaPerson);
        jpaRole.setType(partyRoleTypeRepository.findByName("USER").orElseThrow());
        jpaPerson.getRoles().add(jpaRole);
        return jpaPerson;
    }
}
