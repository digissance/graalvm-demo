package biz.digissance.graalvmdemo.domain.party;

import biz.digissance.graalvmdemo.http.OidcRegisterRequest;
import biz.digissance.graalvmdemo.http.RegisterRequest;
import biz.digissance.graalvmdemo.jpa.party.PartyMapper;
import net.liccioni.archetypes.party.Party;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class PartyServiceImpl implements PartyService {
    private final PartyRepository partyRepository;
    private final PartyMapper partyMapper;
    private final PasswordEncoder passwordEncoder;

    public PartyServiceImpl(final PartyRepository partyRepository,
                            final PartyMapper partyMapper,
                            final PasswordEncoder passwordEncoder) {
        this.partyRepository = partyRepository;
        this.partyMapper = partyMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Party register(final RegisterRequest registerRequest) {
        final var password = passwordEncoder.encode(registerRequest.getPassword());
//        final var person = personRepository.findByAuthenticationUserName(registerRequest.getEmail())
//                .orElseGet(() -> partyMapper.toPersonDomain(registerRequest, password));
//        person.getAuthentications().add(EmailPasswordAuthentication.builder()
//                .emailAddress(registerRequest.getEmail())
//                .password(password)
//                .build());
        return partyRepository.save(registerRequest, password);
    }

    @Override
    public Party register(final OidcRegisterRequest registerRequest) {

        return partyRepository.save(registerRequest);
    }
}
