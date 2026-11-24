package com.teste.dscommerce.mappers;

import com.teste.dscommerce.dto.CategoryDTO;
import com.teste.dscommerce.entities.Category;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

  CategoryDTO toDTO(Category entity);

  Category toEntity(CategoryDTO dto);
}
