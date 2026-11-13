package com.teste.dscommerce.mappers;


import com.teste.dscommerce.dto.OrderDTO;
import com.teste.dscommerce.entities.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {OrderItemMapper.class, PaymentMapper.class})
public interface OrderMapper {

  @Mapping(source = "status", target = "orderStatus")
  @Mapping(source = "client.id", target = "clientId")
  @Mapping(source = "client.name", target = "clientName")
  OrderDTO toDTO(Order entity);
}

