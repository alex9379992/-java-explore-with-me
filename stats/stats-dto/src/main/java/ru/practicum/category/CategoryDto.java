package ru.practicum.category;

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@Valid
public class CategoryDto {

    private Long id;
    @NotNull
    @NotEmpty
    @NotBlank
    private String name;
}
