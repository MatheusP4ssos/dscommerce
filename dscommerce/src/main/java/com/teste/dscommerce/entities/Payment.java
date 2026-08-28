package com.teste.dscommerce.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.Objects;

@Entity
@Table(name = "tb_payment")
public class Payment {

  @Id
  @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
  private Long id;

  @Column(columnDefinition = "TIMESTAMP WITHOUT TIME ZONE")
  private Instant moment;

  @OneToOne
  @MapsId
  private Order order;

  public Payment() {
  }

  public Payment(Long id, Instant moment) {
    this.id = id;
    this.moment = moment;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Instant getMoment() {
    return moment;
  }

  public void setMoment(Instant moment) {
    this.moment = moment;
  }

  public Order getOrder() {
    return order;
  }

  public void setOrder(Order order) {
    this.order = order;
  }

  @Override
  public boolean equals(Object o) {
    if (!(o instanceof Payment payment)) {
      return false;
    }
    return Objects.equals(id, payment.id);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(id);
  }
}
