package com.example.waqas.balance.repository;

import com.example.waqas.balance.model.InvoicePaid;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InvoicePaidRepository extends JpaRepository<InvoicePaid,Integer> {
}
