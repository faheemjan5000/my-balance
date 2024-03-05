package com.example.waqas.balance.repository;

import com.example.waqas.balance.model.InvoiceOld;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InvoiceOldRepository extends JpaRepository<InvoiceOld,Integer> {
}
