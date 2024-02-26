package com.example.waqas.balance.service;

import com.example.waqas.balance.model.InvoiceUsd;
import com.example.waqas.balance.repository.InvoiceUsdRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InvoiceUsdService {

    @Autowired
    private InvoiceUsdRepository invoiceUsdRepository;

    public InvoiceUsd addInvoiceUsd(InvoiceUsd invoiceUsd){
        return invoiceUsdRepository.save(invoiceUsd);
    }
}
