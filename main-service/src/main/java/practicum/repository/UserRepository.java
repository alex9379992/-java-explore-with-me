package practicum.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import practicum.model.UserEntity;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    boolean existsByName(String name);

    @Query("SELECT u FROM UserEntity u " +
            "WHERE ((:userIds) is null or u.id IN (:userIds)) " +
            "ORDER BY u.id " +
            "DESC")
    Page<UserEntity> findAllByUserIdIn(Pageable pageable, @Param("userIds") List<Long> userIds);
}
