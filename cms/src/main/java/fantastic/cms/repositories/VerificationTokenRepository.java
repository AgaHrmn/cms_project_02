package fantastic.cms.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import fantastic.cms.models.VerificationToken;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {
    VerificationToken findByToken(String token);
}
