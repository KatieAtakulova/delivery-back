package ua.nure.delivery.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.nure.delivery.entity.OrderProduct;

public interface OrderProductRepository extends JpaRepository<OrderProduct, Long> {
}
