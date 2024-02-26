package com.example.waqas.balance.service;

import com.example.waqas.balance.exceptions.InvoiceNotFoundException;
import com.example.waqas.balance.model.CurrentBalance;
import com.example.waqas.balance.model.InvoiceEuro;
import com.example.waqas.balance.model.InvoiceUsd;
import com.example.waqas.balance.repository.InvoiceEuroRepository;
import com.example.waqas.balance.repository.InvoiceUsdRepository;
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


    public InvoiceEuro addInvoiceEuro(InvoiceEuro invoiceEuro){
        return invoiceEuroRepository.save(invoiceEuro);
    }

    public InvoiceUsd addInvoiceUsd(InvoiceUsd invoiceUsd){
        return invoiceUsdRepository.save(invoiceUsd);
    }


    public void removeInvoiceEuro(Double invoiceEuroID) throws InvoiceNotFoundException {
        Optional<InvoiceEuro> optionalInvoiceEuro = invoiceEuroRepository.findById(invoiceEuroID);
        if(optionalInvoiceEuro.isPresent()){
            invoiceEuroRepository.deleteById(invoiceEuroID);
        }else{
            throw new InvoiceNotFoundException("This invoice is not present!");
        }
    }
    public void removeInvoiceUsd(Double invoiceUsdID) throws InvoiceNotFoundException {
        Optional<InvoiceUsd> optionalInvoiceUsd = invoiceUsdRepository.findById(invoiceUsdID);
        if(optionalInvoiceUsd.isPresent()){
            invoiceUsdRepository.deleteById(invoiceUsdID);
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

    public Double getAllUsdBalance(){
        List<InvoiceUsd> allUsdInvoices = getAllUsdInvoices();
        double currentUsdBalance = 0.0;
        for(InvoiceUsd invoiceUsd : allUsdInvoices){
            currentUsdBalance = currentUsdBalance + invoiceUsd.getUsd();
        }
        return currentUsdBalance;
    }

   public Double getAllEuroBalance(){
        List<InvoiceEuro> allEuroBalance = getAllEuroInvoices();
        double currentEuroBalance = 0.0;
        for(InvoiceEuro invoiceEuro : allEuroBalance){
            currentEuroBalance = currentEuroBalance+invoiceEuro.getEuro();
        }

        return currentEuroBalance;
   }

   public CurrentBalance getAllCurrenciesBalance(){
        CurrentBalance currentBalance = new CurrentBalance();
        Double usdBalance = this.getAllUsdBalance();
        Double euroBalance = this.getAllEuroBalance();
        currentBalance.setUsd(usdBalance);
        currentBalance.setEuro(euroBalance);

        return currentBalance;
   }

   public Double getAllBalanceInUsd(double currentEuroToDollar) {
       // 1 euro = 1.12
       Double euroAmount = this.getAllEuroBalance();
       double convertedEuroToDollar = euroAmount * currentEuroToDollar;
      return convertedEuroToDollar;
   }
}
