package practicum.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import practicum.model.CategoryEntity;

public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> {

    boolean existsByName(String name);
}
