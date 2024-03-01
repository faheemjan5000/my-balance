package com.example.waqas.balance.service;

import com.example.waqas.balance.dto.InvoiceDTO;
import com.example.waqas.balance.exceptions.InvoiceNotFoundException;
import com.example.waqas.balance.exceptions.WrongStatusException;
import com.example.waqas.balance.mapper.InvoiceMapper;
import com.example.waqas.balance.model.Invoice;
import com.example.waqas.balance.repository.InvoiceRepository;
import com.example.waqas.balance.utility.PaymentStatus;
import com.example.waqas.balance.utility.Utilities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class InvoiceService {

    @Autowired
    private InvoiceRepository invoiceRepository;

    public Invoice addInvoice(InvoiceDTO invoiceDTO){
        Invoice invoice = InvoiceMapper.convertInvoiceDTOintoInvoice(invoiceDTO);
        Invoice invoiceSaved = invoiceRepository.save(invoice);
        Utilities.writeOrUpdateInvoicesFile(this.getAllInvoices());
        return invoiceSaved;
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
    public void removeInvoiceById(Integer id) throws InvoiceNotFoundException {
        if(invoiceRepository.findById(id).isPresent()){
            invoiceRepository.deleteById(id);
            Utilities.writeOrUpdateInvoicesFile(this.getAllPaidInvoices());
        }
        else {
            throw new InvoiceNotFoundException("Invoice does not exist!");
        }
    }
    public void removeAllInvoices(){

        invoiceRepository.deleteAll();
        Utilities.writeOrUpdateInvoicesFile(this.getAllPaidInvoices());
    }

    public Invoice changeInvoicePaymentStatus(Integer invoiceId, String status) throws WrongStatusException, InvoiceNotFoundException {
        Invoice invoiceRetrieved = null;
       if(status.equalsIgnoreCase(String.valueOf(PaymentStatus.YES)) || status.equalsIgnoreCase(String.valueOf(PaymentStatus.NO)) ) {
           try {
               invoiceRetrieved = this.getInvoiceById(invoiceId);
               invoiceRetrieved.setPaid(status);
               invoiceRepository.save(invoiceRetrieved);
               Utilities.writeOrUpdateInvoicesFile(this.getAllInvoices());
               return invoiceRetrieved;
           } catch (InvoiceNotFoundException e) {
               throw new InvoiceNotFoundException(e.getMessage());
           }
       }
        else{
            throw new WrongStatusException("wrong status!");
       }
    }
}
