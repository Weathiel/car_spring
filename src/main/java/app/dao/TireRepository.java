package app.dao;

import app.entity.Tire;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
@Transactional
public interface TireRepository extends JpaRepository<Tire, Long> {
}
