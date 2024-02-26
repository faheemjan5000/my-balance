package com.example.waqas.balance.controller;

import com.example.waqas.balance.model.InvoiceEuro;
import com.example.waqas.balance.model.InvoiceUsd;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/balance")
public class BalanceController {

    @PostMapping("/addUsd")
    public void invoiceInUSD(@RequestBody InvoiceUsd invoiceUsd){

    }

    @PostMapping("/addEuro")
    public void invoiceEURO(@RequestBody InvoiceEuro invoiceEuro){

    }
}
