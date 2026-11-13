package com.teste.dscommerce.mappers;

import com.teste.dscommerce.dto.PaymentDTO;
import com.teste.dscommerce.entities.Payment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PaymentMapper {

  @Mapping(source = "order.id", target = "orderId")
  PaymentDTO toDTO(Payment entity);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "order", ignore = true)
  Payment toEntity(PaymentDTO dto);
}