package com.teste.dscommerce.mappers;

import com.teste.dscommerce.dto.ProductDTO;
import com.teste.dscommerce.entities.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductMapper {

  @Mapping(source = "id", target = "id")
  ProductDTO toDTO(Product entity);

  @Mapping(target = "id", ignore = true) // Ignores id on creation so Hibernate manages generation
  Product toEntity(ProductDTO dto);
}