package biz.digissance.graalvmdemo.domain;

import biz.digissance.graalvmdemo.http.PersonDTO;
import biz.digissance.graalvmdemo.jpa.party.PartyMapper;
import net.liccioni.archetypes.party.Party;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class PartyServiceImpl implements PartyService {
    private final PersonRepository personRepository;
    private final PartyMapper partyMapper;

    public PartyServiceImpl(final PersonRepository personRepository, final PartyMapper partyMapper) {
        this.personRepository = personRepository;
        this.partyMapper = partyMapper;
    }

    @Override
    public Party register(final PersonDTO personDto) {
        final var person = partyMapper.toPersonDomain(personDto);
        return personRepository.create(person);
    }
}
