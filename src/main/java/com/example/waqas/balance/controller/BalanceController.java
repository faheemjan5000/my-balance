package com.example.waqas.balance.controller;

import com.example.waqas.balance.exceptions.InvoiceNotFoundException;
import com.example.waqas.balance.model.CurrentBalance;
import com.example.waqas.balance.model.InvoiceEuro;
import com.example.waqas.balance.model.InvoiceUsd;
import com.example.waqas.balance.service.InvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/balance")
public class BalanceController {

    @Autowired
    private InvoiceService invoiceService;

    @PostMapping("/addUsd")
    public ResponseEntity<Object> invoiceInUSD(@RequestBody InvoiceUsd invoiceUsd){
       return new ResponseEntity<>(invoiceService.addInvoiceUsd(invoiceUsd),HttpStatus.CREATED);
    }

    @PostMapping("/addEuro")
    public ResponseEntity<Object> invoiceEURO(@RequestBody InvoiceEuro invoiceEuro){
        return new ResponseEntity<>(invoiceService.addInvoiceEuro(invoiceEuro),HttpStatus.CREATED);
    }

    @GetMapping("/onlyUSDBalance")
    public ResponseEntity<Double> getUsdBalanceOnly(){
        return ResponseEntity.ok(invoiceService.getAllUsdBalance());

    }

    @GetMapping("/onlyEUROBalance")
    public ResponseEntity<Double> getEuroBalanceOnly(){
        return ResponseEntity.ok(invoiceService.getAllEuroBalance());
    }

    @DeleteMapping("/deleteUSDInvoice")
    public ResponseEntity<String> removeInvoiceUSD(Double invoiceUsdId){
        try{
            invoiceService.removeInvoiceUsd(invoiceUsdId);
            return ResponseEntity.ok("invoice in usd deleted successfully!");
        } catch (InvoiceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @DeleteMapping("/deleteEUROInvoice")
    public ResponseEntity<String> removeInvoiceEURO(Double invoiceEuroId){
        try{
            invoiceService.removeInvoiceEuro(invoiceEuroId);
            return ResponseEntity.ok("invoice in euro deleted successfully!");
        } catch (InvoiceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/totalOfAllCurrenciesInUSD/{currentEuroRate}")
    public ResponseEntity<Double> getSumOfAllAmountInUSD(@PathVariable("currentEuroRate") double currentEuroToDollarValue){
        //this API returns the sum of all the currencies in USD
        return ResponseEntity.ok(invoiceService.getAllBalanceInUsd(currentEuroToDollarValue));
    }

    @GetMapping("/bothCurrenciesData")
    public ResponseEntity<CurrentBalance> getBalanceInBothCurrencies(){
        return ResponseEntity.ok(invoiceService.getAllCurrenciesBalance());
    }

    @GetMapping("/allEuroInvoices")
    public ResponseEntity<List<InvoiceEuro>> getAllEuroInvoices(){
        return ResponseEntity.ok(invoiceService.getAllEuroInvoices());
    }

    @GetMapping("/allUsdInvoices")
    public ResponseEntity<List<InvoiceUsd>> getAllUsdInvoices(){
        return ResponseEntity.ok(invoiceService.getAllUsdInvoices());
    }
}
