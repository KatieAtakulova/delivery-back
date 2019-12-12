package ua.nure.delivery.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.nure.delivery.entity.Excitement;

public interface ExcitementRepository extends JpaRepository<Excitement, Long> {
}
