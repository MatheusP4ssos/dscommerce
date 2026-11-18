package com.teste.dscommerce.entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "tb_order")

public class Order {

  @Id
  @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
  private Long id;

  private Instant moment;
  @Enumerated(EnumType.STRING)
  private OrderStatus status;

  @Version
  private Long version;

  @ManyToOne
  @JoinColumn(name = "user_id")
  private User client;

  @OneToOne(mappedBy = "order", cascade = CascadeType.ALL)
  private Payment payment;

  @OneToMany(mappedBy = "id.order", cascade = CascadeType.ALL)
  private Set<OrderItem> items = new HashSet<>();

  public Order() {
  }

  public Order(Long id, Instant moment, OrderStatus status, User client, Payment payment) {
    this.id = id;
    this.moment = moment;
    this.status = status;
    this.client = client;
    this.payment = payment;
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

  public OrderStatus getStatus() {
    return status;
  }

  public void setStatus(OrderStatus status) {
    this.status = status;
  }

  public Long getVersion() {
    return version;
  }

  public void setVersion(Long version) {
    this.version = version;
  }

  public User getClient() {
    return client;
  }

  public void setClient(User client) {
    this.client = client;
  }

  public Payment getPayment() {
    return payment;
  }

  public void setPayment(Payment payment) {
    this.payment = payment;
  }

  public Set<OrderItem> getItems() {
    return items;
  }

  public void setItems(Set<OrderItem> items) {
    this.items = items;
  }

  public BigDecimal getTotal() {
    return items.stream().map(OrderItem::getSubTotal).reduce(BigDecimal.ZERO, BigDecimal::add);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Order order)) {
      return false;
    }
    return id != null && id.equals(order.id);
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}
