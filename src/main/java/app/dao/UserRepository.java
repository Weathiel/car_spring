package app.dao;

import app.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public interface UserRepository extends JpaRepository<User, Long> {
    @Query("select u from User u inner join u.role r where r.roleName in :rolename")
    List<User> findAllBySpecificRole(@Param("rolename") String... rolename);

    User findByUsername(String username);

    User findByEmail(String email);

}
