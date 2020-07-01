package app.dao;

import app.entity.Orders;
import app.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public interface OrdersRepository extends JpaRepository<Orders, Long> {
    List<Orders> findAllByUser(User user);
}
