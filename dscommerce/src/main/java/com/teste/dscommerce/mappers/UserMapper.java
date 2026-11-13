package com.teste.dscommerce.mappers;

import com.teste.dscommerce.dto.UserDTO;
import com.teste.dscommerce.dto.UserInsertDTO;
import com.teste.dscommerce.entities.Role;
import com.teste.dscommerce.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

  UserDTO toDTO(User entity);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "orders", ignore = true)
  @Mapping(target = "roles", ignore = true)
  User toEntity(UserInsertDTO dto);

  default String roleToAuthority(Role role) {
    return role == null ? null : role.getAuthority();
  }
}