package in.sampleweb.domain.data.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import in.sampleweb.domain.data.entity.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
    public Order findByRazorPayOrderId(String razorPayOrderId);
    List<Order> findByEmail(String email);
}

