package com.example.waqas.balance.controller;

import com.example.waqas.balance.dto.InvoiceDTO;
import com.example.waqas.balance.exceptions.InvoiceNotFoundException;
import com.example.waqas.balance.exceptions.WrongStatusException;
import com.example.waqas.balance.model.Invoice;
import com.example.waqas.balance.service.InvoiceService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/invoice")
public class InvoiceController {

    @Autowired
    private InvoiceService invoiceService;

    @PostMapping("/addInvoice")
    public ResponseEntity<Object> addInvoice(@Valid @RequestBody InvoiceDTO invoiceDTO){
        try {
            return new ResponseEntity<>(invoiceService.addInvoice(invoiceDTO), HttpStatus.CREATED);
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }

    }

    @GetMapping("/get/{id}")
    public ResponseEntity<Object> getInvoiceById(@PathVariable("id") Integer id){
        try {
            return ResponseEntity.ok(invoiceService.getInvoiceById(id));
        } catch (InvoiceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/paidInvoices")
    public ResponseEntity<Object> getAllPaidInvoices(){
        List<Invoice> allInvoices = invoiceService.getAllPaidInvoices();
        if(allInvoices.isEmpty()){
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("No invoice found in database!");
        }else{
            return ResponseEntity.ok(allInvoices);
        }

    }

    @GetMapping("/allInvoices")
    public ResponseEntity<Object> getAllInvoices(){
        List<Invoice> allInvoices = invoiceService.getAllInvoices();
        if(allInvoices.isEmpty()){
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("No invoice found in database!");
        }else{
            return ResponseEntity.ok(allInvoices);
        }

    }

    @DeleteMapping("/removeInvoice/{id}")
    public ResponseEntity<String> removeInvoiceById(@PathVariable("id") Integer invoiceId){
        try {
            invoiceService.removeInvoiceById(invoiceId);
            return ResponseEntity.ok().body("Invoice with id "+invoiceId+" deleted successfully");
        } catch (InvoiceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("error occured deleting invoice");
        }
    }

    @DeleteMapping("/resetInvoices")
    public ResponseEntity<String> resetInvoices(){
        invoiceService.removeAllInvoices();
        return ResponseEntity.ok("deleted all invoices successfully");
    }

    @PutMapping("/updateStatus/{id}/{status}")
    public ResponseEntity<Object> changeInvoiceStatus(@PathVariable("id") Integer invoiceId,@PathVariable("status") String paymentStatus){
                 try {
                     Invoice updatedInvoice = invoiceService.changeInvoicePaymentStatus(invoiceId,paymentStatus);
                     return ResponseEntity.ok(updatedInvoice);
                 } catch (WrongStatusException e) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
                 } catch (InvoiceNotFoundException e) {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
                 }

    }

}
