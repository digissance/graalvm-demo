package biz.digissance.graalvmdemo.domain;

import biz.digissance.graalvmdemo.http.PersonDTO;
import biz.digissance.graalvmdemo.jpa.party.PartyMapper;
import net.liccioni.archetypes.party.Party;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class PartyServiceImpl implements PartyService {
    private final PersonRepository personRepository;
    private final PartyMapper partyMapper;
    private final PasswordEncoder passwordEncoder;

    public PartyServiceImpl(final PersonRepository personRepository,
                            final PartyMapper partyMapper,
                            final PasswordEncoder passwordEncoder) {
        this.personRepository = personRepository;
        this.partyMapper = partyMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Party register(final PersonDTO personDto) {
        final var person = partyMapper.toPersonDomain(personDto, passwordEncoder.encode(personDto.getPassword()));
        return personRepository.create(person);
    }
}
