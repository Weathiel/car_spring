package app.dao;

import app.entity.Engine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
@Transactional
public interface EngineRepository extends JpaRepository<Engine, Long> {
}
