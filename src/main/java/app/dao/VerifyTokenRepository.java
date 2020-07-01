package app.dao;

import app.entity.VerifyToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
@Transactional
public interface VerifyTokenRepository extends JpaRepository<VerifyToken, Long> {
    VerifyToken findByToken(String token);
}
