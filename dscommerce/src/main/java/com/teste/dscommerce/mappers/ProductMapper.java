package com.teste.dscommerce.mappers;

import com.teste.dscommerce.dto.ProductDTO;
import com.teste.dscommerce.entities.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductMapper {

  ProductDTO toDTO(Product entity);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "categories", ignore = true)
  Product toEntity(ProductDTO dto);
}