package biz.digissance.graalvmdemo.domain.party;

import biz.digissance.graalvmdemo.http.RegisterRequest;
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
    public Party register(final RegisterRequest registerRequest) {
        final var person = partyMapper.toPersonDomain(registerRequest, passwordEncoder.encode(registerRequest.getPassword()));
        return personRepository.create(person);
    }
}
