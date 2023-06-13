package practicum.util;

import practicum.model.CategoryEntity;
import ru.practicum.category.CategoryDto;

public class CategoryMapper {

    public static CategoryDto toDto(CategoryEntity categoryEntity) {
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setId(categoryEntity.getId());
        categoryDto.setName(categoryEntity.getName());
        return categoryDto;
    }

    public static CategoryEntity toEntity(CategoryDto category) {
        CategoryEntity categoryEntity = new CategoryEntity();
        categoryEntity.setName(category.getName());
        return categoryEntity;
    }
}
