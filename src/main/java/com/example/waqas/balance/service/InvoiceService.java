package com.example.waqas.balance.service;

import com.example.waqas.balance.dto.InvoiceDTO;
import com.example.waqas.balance.exceptions.InvoiceNotFoundException;
import com.example.waqas.balance.exceptions.InvoiceOldAddingException;
import com.example.waqas.balance.exceptions.WrongStatusException;
import com.example.waqas.balance.mapper.InvoiceMapper;
import com.example.waqas.balance.model.AllCurrencies;
import com.example.waqas.balance.model.Invoice;
import com.example.waqas.balance.model.InvoiceOld;
import com.example.waqas.balance.repository.InvoiceRepository;
import com.example.waqas.balance.utility.PaymentStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class InvoiceService {

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private InvoiceOldService invoiceOldService;

    public Invoice addInvoice(InvoiceDTO invoiceDTO){
        Invoice invoice = InvoiceMapper.convertInvoiceDTOintoInvoice(invoiceDTO);
        return invoiceRepository.save(invoice);
    }

    public Invoice getInvoiceById(Integer id) throws InvoiceNotFoundException {
        Optional<Invoice> optionalInvoice = invoiceRepository.findById(id);
        if(optionalInvoice.isPresent()){
            return optionalInvoice.get();
        }
        else {
            throw new InvoiceNotFoundException("invoice does not exist");
        }
    }

    public List<Invoice> getAllPaidInvoices(){
        List<Invoice> paidInvoices = invoiceRepository.findAll().stream()
                .filter(invoice -> invoice.getPaid().equalsIgnoreCase("YES"))
                .toList();
        return paidInvoices;
    }

    public List<Invoice> getAllInvoices(){
        return invoiceRepository.findAll();
    }

    public void removeInvoiceById(Integer id) throws InvoiceNotFoundException, InvoiceOldAddingException {
        log.info("InvoiceService.removeInvoiceById() method is called...");
        log.info("ID passed : {}",id);
        Optional<Invoice> invoiceSearch = invoiceRepository.findById(id);
        if(invoiceSearch.isPresent()){
            log.info("invoice found : {}",invoiceSearch.get());
            invoiceRepository.deleteById(id);
            log.info("deleted invoice successfully,Now adding it to the history of invoices");
            com.example.waqas.balance.model.InvoiceOld invoiceOld = InvoiceMapper.mapInvoiceToInvoiceOld(invoiceSearch.get());
            log.info("invoiceOld to be persisted : {}",invoiceOld);
            //before removing and adding into old invoices , we must change its payment status to YES i.e we only remove and add paid invoices into old invoices aka invoices history
            invoiceOld.setPaid(PaymentStatus.YES.toString());
            invoiceOldService.addInvoiceOld(invoiceOld);
        }
        else {
            throw new InvoiceNotFoundException("Invoice does not exist!");
        }
    }
    public void removeAllInvoices(){

        invoiceRepository.deleteAll();
    }

    public Invoice changeInvoicePaymentStatus(Integer invoiceId, String status) throws WrongStatusException, InvoiceNotFoundException {
        Invoice invoiceRetrieved = null;
       if(status.equalsIgnoreCase(String.valueOf(PaymentStatus.YES)) || status.equalsIgnoreCase(String.valueOf(PaymentStatus.NO)) ) {
           try {
               invoiceRetrieved = this.getInvoiceById(invoiceId);
               invoiceRetrieved.setPaid(status);
               invoiceRepository.save(invoiceRetrieved);
               return invoiceRetrieved;
           } catch (InvoiceNotFoundException e) {
               throw new InvoiceNotFoundException(e.getMessage());
           }
       }
        else{
            throw new WrongStatusException("wrong status!");
       }
    }

    public AllCurrencies getSumOfAllCurrencies(){
        List<Invoice> allInvoices = this.getAllInvoices();
        double sumUsd = 0.00;
        double sumEuro = 0.00;
        List<Invoice> usdInvoices = allInvoices.stream()
                .filter(invoice -> invoice.getCurrency().equalsIgnoreCase("USD"))
                .toList();
        List<Invoice> euroInvoices = allInvoices.stream()
                .filter(invoice -> invoice.getCurrency().equalsIgnoreCase("EURO"))
                .toList();
         for(Invoice usdInvoice : usdInvoices){
             sumUsd = sumUsd + usdInvoice.getAmount();
         }

        for(Invoice euroInvoice : euroInvoices){
            sumEuro = sumEuro + euroInvoice.getAmount();
        }

        AllCurrencies allCurrencies = new AllCurrencies();
        allCurrencies.setEuro(sumEuro);
        allCurrencies.setUsd(sumUsd);

        return allCurrencies;
    }
}
