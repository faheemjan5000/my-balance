package com.example.waqas.balance.controller;

import com.example.waqas.balance.exceptions.InvoiceNotFoundException;
import com.example.waqas.balance.exceptions.InvoicePaidAddingException;
import com.example.waqas.balance.model.InvoicePaid;
import com.example.waqas.balance.repository.InvoicePaidRepository;
import com.example.waqas.balance.service.InvoicePaidService;
import com.example.waqas.balance.utility.Utilities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.ResourceBundle;

@RestController
@RequestMapping("/paidInvoice")
public class InvoicePaidController {

    @Autowired
    private InvoicePaidService invoicePaidService;

    @PostMapping("/addPaidInvoice")
    public ResponseEntity<Object> addPaidInvoice(@RequestBody InvoicePaid invoicePaid){
        try {
            return new ResponseEntity<>(invoicePaidService.saveInvoicePaid(invoicePaid),HttpStatus.CREATED);
        } catch (InvoicePaidAddingException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }

    }

    @GetMapping("/get/{id}")
    public ResponseEntity<Object> getInvoiceById(@PathVariable("id") Integer id){
        try {
            return ResponseEntity.ok(invoicePaidService.getInvoicePaidById(id));
        } catch (InvoiceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/allPaidInvoices")
    public ResponseEntity<Object> getAllPaidInvoices(){
        List<InvoicePaid> allPaidInvoices = invoicePaidService.getAllPaidInvoices();
        if(allPaidInvoices.isEmpty()){
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("No Paid invoice found in database!");
        }else{
            return ResponseEntity.ok(invoicePaidService.getAllPaidInvoices());
        }

    }

    @DeleteMapping("/removeInvoicePaid")
    public ResponseEntity<String> removeInvoicePaidById(Integer invoicePaidId){
        try {
            invoicePaidService.removeInvoicePaidById(invoicePaidId);
            return ResponseEntity.ok().body("InvoicePaid with id "+invoicePaidId+" deleted successfully");
        } catch (InvoiceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("error occured deleting Paid invoice");
        }
    }

    @DeleteMapping("/resetPAIDInvoices")
    public ResponseEntity<String> resePaidInvoices(){
         invoicePaidService.removeAllPaidInvoices();
        //update the csv file for invoices in USD and in EURO
        Utilities.writeOrUpdateAllPaidInvoicesFile(invoicePaidService.getAllPaidInvoices());
        return ResponseEntity.ok("deleted all PAID invoices successfully");
    }


}
