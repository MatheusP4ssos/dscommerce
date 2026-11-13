package com.teste.dscommerce.repositories;

import com.teste.dscommerce.entities.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

}