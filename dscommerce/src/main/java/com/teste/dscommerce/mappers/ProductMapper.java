package com.teste.dscommerce.mappers;

import com.teste.dscommerce.dto.ProductDTO;
import com.teste.dscommerce.entities.Product;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductMapper {

  ProductDTO toDTO(Product entity);
}