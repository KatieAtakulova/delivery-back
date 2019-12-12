package ua.nure.delivery.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.nure.delivery.entity.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
