package com.example.waqas.balance.controller;

import com.example.waqas.balance.dto.InvoiceDTO;
import com.example.waqas.balance.exceptions.InvoiceNotFoundException;
import com.example.waqas.balance.exceptions.InvoiceOldNotFoundException;
import com.example.waqas.balance.exceptions.WrongStatusException;
import com.example.waqas.balance.model.AllCurrencies;
import com.example.waqas.balance.model.Invoice;
import com.example.waqas.balance.model.InvoiceOld;
import com.example.waqas.balance.service.InvoiceOldService;
import com.example.waqas.balance.service.InvoiceService;
import com.example.waqas.balance.utility.Utilities;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/invoice")
public class InvoiceController {

    @Autowired
    private InvoiceService invoiceService;

    @Autowired
    private InvoiceOldService invoiceOldService;

    @PostMapping("/addInvoice")
    public ResponseEntity<Object> addInvoice(@Valid @RequestBody InvoiceDTO invoiceDTO){
        log.info("InvoiceController.addInvoice() method is called...");
        try {
            return new ResponseEntity<>(invoiceService.addInvoice(invoiceDTO), HttpStatus.CREATED);
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }

    }

    @GetMapping("/get/{id}")
    public ResponseEntity<Object> getInvoiceById(@PathVariable("id") Integer id){
        log.info("InvoiceController.getInvoiceById() method is called...");
        try {
            return ResponseEntity.ok(invoiceService.getInvoiceById(id));
        } catch (InvoiceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/allInvoices")
    public ResponseEntity<Object> getAllInvoices(){
        log.info("InvoiceController.getAllInvoices() method is called...");
        List<Invoice> allInvoices = invoiceService.getAllInvoices();
        if(allInvoices.isEmpty()){
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("No invoice found in database!");
        }else{
            return ResponseEntity.ok(allInvoices);
        }

    }

    @GetMapping("/oldInvoices")
    public ResponseEntity<Object> getAllOldInvoices(){
        log.info("InvoiceController.getAllOldInvoices() method is called...");
        List<InvoiceOld> oldInvoices = invoiceOldService.getAllOldInvoices();
        if(oldInvoices.isEmpty()){
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("No invoice found in database!");
        }else{
            return ResponseEntity.ok(oldInvoices);
        }

    }

    @DeleteMapping("/removeInvoice/{id}/{amountInEuro}")
    public ResponseEntity<String> removeInvoiceById(@PathVariable("id") Integer invoiceId,@PathVariable("amountInEuro") double amountConvertedInEuro){
        log.info("InvoiceController.removeInvoiceById() method is called...");
        try {
            invoiceService.removeInvoiceById(invoiceId,amountConvertedInEuro);
            return ResponseEntity.ok().body("Invoice with id "+invoiceId+" deleted successfully");
        } catch (InvoiceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("error occured deleting invoice");
        }
    }

    @DeleteMapping("/removeOldInvoice/{id}")
    public ResponseEntity<String> removeOldInvoiceById(@PathVariable("id") Integer invoiceId){
        log.info("InvoiceController.removeOldInvoiceById() method is called...");
        try {
            invoiceOldService.removeInvoiceOldById(invoiceId);
            return ResponseEntity.ok().body("InvoiceOld with id "+invoiceId+" deleted successfully");
        } catch (InvoiceOldNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("error occurred deleting invoiceOld");
        }
    }

    @DeleteMapping("/resetInvoices")
    public ResponseEntity<String> resetInvoices(){
        log.info("InvoiceController.resetInvoices() method is called...");
        invoiceService.removeAllInvoices();
        return ResponseEntity.ok("deleted all invoices successfully");
    }

    @PutMapping("/updateStatus/{id}/{status}")
    public ResponseEntity<Object> changeInvoicePaymentStatus(@PathVariable("id") Integer invoiceId,@PathVariable("status") String paymentStatus){
        log.info("InvoiceController.changeInvoiceStatus() method is called...");
                 try {
                     Invoice updatedInvoice = invoiceService.changeInvoicePaymentStatus(invoiceId,paymentStatus);
                     return ResponseEntity.ok(updatedInvoice);
                 } catch (WrongStatusException e) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
                 } catch (InvoiceNotFoundException e) {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
                 }

    }

    @GetMapping("/allCurrencies")
    public ResponseEntity<AllCurrencies> getAllCurrencies(){
        log.info("InvoiceController.getAllCurrencies() method is called...");
        return ResponseEntity.ok(invoiceService.getSumOfAllCurrencies());
    }

    @GetMapping("/writeInvoices")
    public ResponseEntity<String> writeInvoicesIntoCSV(){
        log.info("InvoiceController.writeInvoicesIntoCSV() method is called...");
        List<Invoice> invoiceList = invoiceService.getAllInvoices();
            Utilities.writeOrUpdateInvoicesFile(invoiceList, invoiceService.getSumOfAllCurrencies());
            return ResponseEntity.status(HttpStatus.CREATED).body("Invoices written to the file");

    }

    @GetMapping("/writeOldInvoices")
    public ResponseEntity<Object> writeOldInvoicesIntoCSV(){
        log.info("InvoiceController.writeOldInvoicesIntoCSV() method is called...");
        List<InvoiceOld> oldInvoicesList = invoiceOldService.getAllOldInvoices();
        if(oldInvoicesList.isEmpty()){
            log.warn("No old invoice exist!");
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("No content found in database!");
        }else {
            Utilities.writeOldInvoicesFile(oldInvoicesList);
            return ResponseEntity.status(HttpStatus.CREATED).body("Invoices written to the file");
        }
    }

}
