package com.teste.dscommerce.mappers;

import com.teste.dscommerce.dto.OrderItemDTO;
import com.teste.dscommerce.entities.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderItemMapper {

  @Mapping(source = "order.id", target = "orderId")
  @Mapping(source = "product.id", target = "productId")
  @Mapping(source = "product.price", target = "price")
  @Mapping(source = "product.name", target = "productName")
  OrderItemDTO toDTO(OrderItem entity);
}