package app.dao;

import app.entity.Details;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
@Transactional
public interface DetailsRepository extends JpaRepository<Details, Long> {
}
