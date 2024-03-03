package com.example.waqas.balance.service;

import com.example.waqas.balance.exceptions.InvoiceOldAddingException;
import com.example.waqas.balance.exceptions.InvoiceOldNotFoundException;
import com.example.waqas.balance.model.Invoice;
import com.example.waqas.balance.model.InvoiceOld;
import com.example.waqas.balance.repository.InvoiceOldRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class InvoiceOldService {

    @Autowired
    private InvoiceOldRepository invoiceOldRepository;

    public InvoiceOld addInvoiceOld(InvoiceOld invoiceOld) throws InvoiceOldAddingException {
        log.info("InvoiceOldService.addInvoiceOld() method is called...");
        log.info("InvoiceOld to be added : {}",invoiceOld);
        InvoiceOld invoiceOldSaved = invoiceOldRepository.save(invoiceOld);
        if(invoiceOldSaved.getInvoicerName()!=null && invoiceOldSaved.getId()!=null){
            log.info("InvoiceOld added successfully!");
            return invoiceOld;  //will use the return value later in controller
        }
        else{
            log.error("invoiceOld adding error...");
            throw new InvoiceOldAddingException("invoiceOld is not adding. error occurred!");
        }
    }

    public InvoiceOld getInvoiceOldById(Integer id) throws InvoiceOldNotFoundException {
        log.info("InvoiceOldService.getInvoiceOldById() method is called..");
        log.info("InvoiceOld ID passed : {}",id);
        Optional<InvoiceOld> optionalInvoiceOld = invoiceOldRepository.findById(id);
        if(optionalInvoiceOld.isPresent()){
            log.info("invoiceOld found : {}",optionalInvoiceOld.get());
            return optionalInvoiceOld.get();
        }else{
            throw new InvoiceOldNotFoundException("invoiceOld not found : "+id);
        }
    }
    public List<InvoiceOld> getAllOldInvoices(){
        log.info("InvoiceOldService.getAllOldInvoices() method is called...");
        return invoiceOldRepository.findAll();
    }

    public void removeInvoiceOldById(Integer id) throws InvoiceOldNotFoundException {
        log.info("InvoiceOldService.removeInvoiceOldById() method is called...");
        log.info("InvoiceOld ID passed : {}",id);
        log.info("checking if invoiceOld exist for ID : {}",id);
        InvoiceOld invoiceOld =  this.getInvoiceOldById(id);
        log.error("Now removing invoiceOld with ID : {}",id);
        invoiceOldRepository.deleteById(id);
        log.error("invoiceOld removed successfully!");
    }
}
