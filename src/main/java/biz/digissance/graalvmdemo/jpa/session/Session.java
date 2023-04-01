package biz.digissance.graalvmdemo.jpa.session;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Transient;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
public class Session {

    public static final int DEFAULT_EXPIRATION_MINUTES = 30 * 24 * 60;

    @Id
    @Column(name = "pk")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "generator")
    @SequenceGenerator(name = "generator", sequenceName = "seq")
    private Long id;

    private String sessionId;
    private String userId;

    private String token;

    private Instant expiresAt;
    private Instant issuedAt;
    private Instant lastUsedAt;
    private Instant removedAt;

    private boolean deleted;

    public Session(String sessionId, String userId, String token) {
        this(sessionId, userId, token, DEFAULT_EXPIRATION_MINUTES);
    }

    /**
     * Creates a new {@link Session} with the given ID for the given User ID.
     *
     * @param id      Session ID
     * @param userId  User ID
     * @param token   Token's token
     * @param minutes minutes to expire from time of issue
     */
    public Session(String sessionId, String userId, String token, int minutes) {
        this.sessionId = sessionId;
        this.userId = userId;
        this.token = token;
        if (minutes == 0) {
            minutes = DEFAULT_EXPIRATION_MINUTES;
        }
        expiresAt = Instant.now().plus(minutes, ChronoUnit.MINUTES);
        issuedAt = Instant.now();
    }

    @Transient
    public boolean isValid() {
        return isValid(Instant.now());
    }

    @Transient
    public boolean isValid(Instant now) {
        return expiresAt.isAfter(now) && !deleted;
    }
}