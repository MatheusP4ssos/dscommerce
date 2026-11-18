package com.teste.dscommerce.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.Objects;

@Entity
@Table(name = "tb_role")
public class Role {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Column(nullable = false, unique = true)
  private String authority;

  public Role() {
  }

  public Role(Long id, String authority) {
    this.id = id;
    this.authority = authority;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getAuthority() {
    return authority;
  }

  public void setAuthority(String authority) {
    this.authority = authority;
  }

  @Override
  public boolean equals(Object o) {
    if (!(o instanceof Role role)) {
      return false;
    }
    return Objects.equals(id, role.id);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(id);
  }
}
