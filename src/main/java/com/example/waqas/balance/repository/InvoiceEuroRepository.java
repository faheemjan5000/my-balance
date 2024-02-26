package com.example.waqas.balance.repository;

import com.example.waqas.balance.model.InvoiceEuro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InvoiceEuroRepository extends JpaRepository<InvoiceEuro,Double>{
}