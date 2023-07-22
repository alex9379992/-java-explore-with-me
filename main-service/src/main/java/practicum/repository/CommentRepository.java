package practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import practicum.model.CommentEntity;

import java.util.List;

public interface CommentRepository extends JpaRepository<CommentEntity, Long> {

    List<CommentEntity> findAllByAuthorId(Long authorId);
}
