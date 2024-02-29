package com.example.waqas.balance.service;

import com.example.waqas.balance.exceptions.InvoiceNotFoundException;
import com.example.waqas.balance.model.CurrentBalance;
import com.example.waqas.balance.model.InvoiceEuro;
import com.example.waqas.balance.model.InvoiceUsd;
import com.example.waqas.balance.repository.InvoiceEuroRepository;
import com.example.waqas.balance.repository.InvoiceUsdRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class InvoiceService {

    @Autowired
    private InvoiceUsdRepository invoiceUsdRepository;

    @Autowired
    private InvoiceEuroRepository invoiceEuroRepository;


    public InvoiceEuro addInvoiceEuro(InvoiceEuro invoiceEuro){
        InvoiceEuro invoiceSaved = invoiceEuroRepository.save(invoiceEuro);
        this.writeAllInvoicesInFile();
        return invoiceSaved;
    }

    public InvoiceUsd addInvoiceUsd(InvoiceUsd invoiceUsd){
          InvoiceUsd invoiceSaved = invoiceUsdRepository.save(invoiceUsd);
          this.writeAllInvoicesInFile();
        return invoiceSaved;
    }


    public void removeInvoiceEuroById(Integer invoiceEuroID) throws InvoiceNotFoundException {
        Optional<InvoiceEuro> optionalInvoiceEuro = invoiceEuroRepository.findById(invoiceEuroID);
        if(optionalInvoiceEuro.isPresent()){
            invoiceEuroRepository.deleteById(invoiceEuroID);
            this.writeAllInvoicesInFile();
        }else{
            throw new InvoiceNotFoundException("This invoice is not present!");
        }
    }
    public void removeInvoiceUsdById(Integer invoiceUsdID) throws InvoiceNotFoundException {
        Optional<InvoiceUsd> optionalInvoiceUsd = invoiceUsdRepository.findById(invoiceUsdID);
        if(optionalInvoiceUsd.isPresent()){
            invoiceUsdRepository.deleteById(invoiceUsdID);
            this.writeAllInvoicesInFile();
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

       double sumOfAllCurrenciesInUSD = convertedEuroToDollars + usdAmount;
      return sumOfAllCurrenciesInUSD;
   }

    public CurrentBalance getAllCurrenciesBalance(){
        CurrentBalance currentBalance = new CurrentBalance();
        Double usdBalance = this.getUsdBalanceOnly();
        Double euroBalance = this.getEuroBalanceOnly();
        currentBalance.setUsd(usdBalance);
        currentBalance.setEuro(euroBalance);

        return currentBalance;
    }

    public void writeAllInvoicesInFile(){
        List<InvoiceUsd> usdInvoicesList = this.getAllUsdInvoices();
        List<InvoiceEuro> euroInvoicesList = this.getAllEuroInvoices();
        String fileName = "invoices.csv";
        try (FileWriter writer = new FileWriter(fileName)) {
            // Write header
            writer.append("ID,USD,NAME,DATE\n");

            // Write InvoiceUsd data
            for (InvoiceUsd invoiceUsd : usdInvoicesList) {
                writer.append(String.valueOf(invoiceUsd.getId()));
                writer.append(",");
                writer.append(String.valueOf(invoiceUsd.getUsd()));
                writer.append(",");
                writer.append(invoiceUsd.getInvoicerName());
                writer.append(",");
                writer.append(invoiceUsd.getDate());
                writer.append("\n");
                writer.append("\n");
                writer.append("\n");
            }
            writer.append("ID,EURO,NAME,DATE\n");
            // Write InvoiceEuro data
            for (InvoiceEuro invoiceEuro : euroInvoicesList) {
                writer.append(String.valueOf(invoiceEuro.getId()));
                writer.append(",");
                writer.append(String.valueOf(invoiceEuro.getEuro()));
                writer.append(",");
                writer.append(invoiceEuro.getInvoicerName());
                writer.append(",");
                writer.append(invoiceEuro.getDate());
                writer.append("\n");
            }

            System.out.println("Invoices has been written to " + fileName);
        } catch (IOException e) {
            System.err.println("Error occurred while writing CSV: " + e.getMessage());
        }
    }
}
