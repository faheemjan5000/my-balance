package com.example.waqas.balance.service;

import com.example.waqas.balance.exceptions.InvoiceNotFoundException;
import com.example.waqas.balance.model.CurrentBalance;
import com.example.waqas.balance.model.InvoiceEuro;
import com.example.waqas.balance.model.InvoicePaid;
import com.example.waqas.balance.model.InvoiceUsd;
import com.example.waqas.balance.repository.InvoiceEuroRepository;
import com.example.waqas.balance.repository.InvoicePaidRepository;
import com.example.waqas.balance.repository.InvoiceUsdRepository;
import com.example.waqas.balance.utility.Utilities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class InvoiceService {

    @Autowired
    private InvoiceUsdRepository invoiceUsdRepository;

    @Autowired
    private InvoiceEuroRepository invoiceEuroRepository;

    @Autowired
    private InvoicePaidRepository invoicePaidRepository;

    @Autowired
    private InvoicePaidService invoicePaidService;




    public InvoiceEuro addInvoiceEuro(InvoiceEuro invoiceEuro){
        InvoiceEuro invoiceSaved = invoiceEuroRepository.save(invoiceEuro);
        Utilities.writeAllInvoicesInFile(this.getAllUsdInvoices(),this.getAllEuroInvoices());
        return invoiceSaved;
    }

    public InvoiceUsd addInvoiceUsd(InvoiceUsd invoiceUsd){
          InvoiceUsd invoiceSaved = invoiceUsdRepository.save(invoiceUsd);
          Utilities.writeAllInvoicesInFile(this.getAllUsdInvoices(),this.getAllEuroInvoices());
        return invoiceSaved;
    }


    public void removeInvoiceEuroById(Integer invoiceEuroID) throws InvoiceNotFoundException {
        //we remove an invoice when it is paid to wiki so we add it to another file invoicesPaid file
        Optional<InvoiceEuro> optionalInvoiceEuro = invoiceEuroRepository.findById(invoiceEuroID);
        if(optionalInvoiceEuro.isPresent()){
            InvoicePaid invoicePaid = this.convertInvoiceEuroToInvoicePaid(optionalInvoiceEuro.get());
            invoiceEuroRepository.deleteById(invoiceEuroID);
            Utilities.writeAllInvoicesInFile(this.getAllUsdInvoices(),this.getAllEuroInvoices());
            //write the removed(paid) invoice to the paid invoice repository
            invoicePaidRepository.save(invoicePaid);
            //this.writeAllPaidInvoicesInFile();
            Utilities.writeOrUpdateAllPaidInvoicesFile(invoicePaidService.getAllPaidInvoices());
        }else{
            throw new InvoiceNotFoundException("This invoice is not present!");
        }
    }


    public void removeInvoiceUsdById(Integer invoiceUsdID) throws InvoiceNotFoundException {
        Optional<InvoiceUsd> optionalInvoiceUsd = invoiceUsdRepository.findById(invoiceUsdID);
        if(optionalInvoiceUsd.isPresent()){
            InvoicePaid invoicePaid = this.convertInvoiceUsdToInvoicePaid(optionalInvoiceUsd.get());
            invoiceUsdRepository.deleteById(invoiceUsdID);
            Utilities.writeAllInvoicesInFile(this.getAllUsdInvoices(),this.getAllEuroInvoices());
            //write the removed(paid) invoice to the invoice paid repository
            invoicePaidRepository.save(invoicePaid);
            //this.writeAllPaidInvoicesInFile();
            Utilities.writeOrUpdateAllPaidInvoicesFile(invoicePaidService.getAllPaidInvoices());
        }else{
            throw new InvoiceNotFoundException("This invoice is not present!");
        }
    }

    public List<InvoiceEuro> getAllEuroInvoices(){
        return invoiceEuroRepository.findAll();
    }

    public List<InvoiceUsd> getAllUsdInvoices(){
        return invoiceUsdRepository.findAll();
    }

    public Double getUsdBalanceOnly(){
        List<InvoiceUsd> allUsdInvoices = getAllUsdInvoices();
        double currentUsdBalance = 0.0;
        for(InvoiceUsd invoiceUsd : allUsdInvoices){
            currentUsdBalance = currentUsdBalance + invoiceUsd.getUsd();
        }
        return currentUsdBalance;
    }

   public Double getEuroBalanceOnly(){
        List<InvoiceEuro> allEuroBalance = getAllEuroInvoices();
        double currentEuroBalance = 0.0;
        for(InvoiceEuro invoiceEuro : allEuroBalance){
            currentEuroBalance = currentEuroBalance+invoiceEuro.getEuro();
        }

        return currentEuroBalance;
   }

   public Double getSumOfAllCurrenciesInUsd(Double currentEuroToDollarPrice) {
       // 1 euro = 1.12
       Double euroAmount = this.getEuroBalanceOnly();
       Double usdAmount =  this.getUsdBalanceOnly();
       double convertedEuroToDollars = euroAmount * currentEuroToDollarPrice;

       return (convertedEuroToDollars + usdAmount);
   }

    public CurrentBalance getAllCurrenciesBalance(){
        CurrentBalance currentBalance = new CurrentBalance();
        Double usdBalance = this.getUsdBalanceOnly();
        Double euroBalance = this.getEuroBalanceOnly();
        currentBalance.setUsd(usdBalance);
        currentBalance.setEuro(euroBalance);

        return currentBalance;
    }




    private InvoicePaid convertInvoiceEuroToInvoicePaid(InvoiceEuro invoiceEuro) {
        InvoicePaid invoicePaid = new InvoicePaid();
        invoicePaid.setId(invoiceEuro.getId());
        invoicePaid.setDate(invoiceEuro.getDate());
        invoicePaid.setInvoicerName(invoiceEuro.getInvoicerName());
        invoicePaid.setAmount(invoiceEuro.getEuro());
        invoicePaid.setCurrency("EURO");

        return invoicePaid;
    }
    private InvoicePaid convertInvoiceUsdToInvoicePaid(InvoiceUsd invoiceUsd) {
        InvoicePaid invoicePaid = new InvoicePaid();
        invoicePaid.setId(invoiceUsd.getId());
        invoicePaid.setDate(invoiceUsd.getDate());
        invoicePaid.setInvoicerName(invoicePaid.getInvoicerName());
        invoicePaid.setAmount(invoiceUsd.getUsd());
        invoicePaid.setCurrency("USD");

        return invoicePaid;
    }

    public void deleteAllUsdInvoices(){
        invoiceUsdRepository.deleteAll();
    }


    public void deleteAllEuroInvoices(){
        invoiceEuroRepository.deleteAll();
    }

}
