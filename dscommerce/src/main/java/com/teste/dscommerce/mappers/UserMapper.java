package com.teste.dscommerce.mappers;

import com.teste.dscommerce.dto.UserDTO;
import com.teste.dscommerce.dto.UserInsertDTO;
import com.teste.dscommerce.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

  UserDTO toDTO(User entity);

  @Mapping(target = "id", ignore = true)
  User toEntity(UserInsertDTO dto);
}