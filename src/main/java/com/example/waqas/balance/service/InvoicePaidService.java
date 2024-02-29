package com.example.waqas.balance.service;

import com.example.waqas.balance.exceptions.InvoiceNotFoundException;
import com.example.waqas.balance.exceptions.InvoicePaidAddingException;
import com.example.waqas.balance.model.InvoicePaid;
import com.example.waqas.balance.repository.InvoicePaidRepository;
import com.example.waqas.balance.utility.Utilities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class InvoicePaidService {

    @Autowired
    private InvoicePaidRepository invoicePaidRepository;

    public InvoicePaid saveInvoicePaid(InvoicePaid invoicePaid) throws InvoicePaidAddingException {
        if(invoicePaid.getAmount()>0.00 &&
                (invoicePaid.getCurrency().equalsIgnoreCase("usd") || invoicePaid.getCurrency().equalsIgnoreCase("euro"))){
            InvoicePaid invoicePaidSaved = invoicePaidRepository.save(invoicePaid);
            Utilities.writeOrUpdateAllPaidInvoicesFile(this.getAllPaidInvoices());
            return invoicePaidSaved;
        }else{
            throw new InvoicePaidAddingException("adding invoicePaid error");
        }
    }
    public InvoicePaid getInvoicePaidById(Integer id) throws InvoiceNotFoundException {
        Optional<InvoicePaid> optionalInvoicePaid = invoicePaidRepository.findById(id);
        if(optionalInvoicePaid.isPresent()){
            return optionalInvoicePaid.get();
        }
        else {
            throw new InvoiceNotFoundException("invice does not exist");
        }
    }

    public void removeInvoicePaidById(Integer id) throws InvoiceNotFoundException {
        if(invoicePaidRepository.findById(id).isPresent()){
            invoicePaidRepository.deleteById(id);
            Utilities.writeOrUpdateAllPaidInvoicesFile(this.getAllPaidInvoices());
        }
        else {
            throw new InvoiceNotFoundException("Invoice does not exist!");
        }
    }

    public List<InvoicePaid> getAllPaidInvoices(){
        return invoicePaidRepository.findAll();
    }
}
