package com.example.waqas.balance.repository;

import com.example.waqas.balance.model.InvoiceUsd;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InvoiceUsdRepository extends JpaRepository<InvoiceUsd,Integer> {
}
