package ua.nure.delivery.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.nure.delivery.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
