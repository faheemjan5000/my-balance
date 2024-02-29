package com.example.waqas.balance.utility;

import com.example.waqas.balance.model.InvoiceEuro;
import com.example.waqas.balance.model.InvoicePaid;
import com.example.waqas.balance.model.InvoiceUsd;
import com.example.waqas.balance.service.InvoicePaidService;
import com.example.waqas.balance.service.InvoiceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class Utilities {

    @Autowired
    private InvoiceService invoiceService;

    @Autowired
    private InvoicePaidService invoicePaidService;

    public static void writeOrUpdateAllPaidInvoicesFile(List<InvoicePaid> paidInvoicesList){
       // List<InvoicePaid> paidInvoicesList = invoicePaidService.getAllPaidInvoices();
        String fileName = "paidInvoices.csv";
        try (FileWriter writer = new FileWriter(fileName)) {
            // Write header
            writer.append("ID,NAME,AMOUNT,CURRENCY,DATE\n");

            // Write InvoiceUsd data
            for (InvoicePaid invoicePaid : paidInvoicesList) {
                writer.append(String.valueOf(invoicePaid.getId())); //ID
                writer.append(",");
                writer.append(invoicePaid.getInvoicerName());       //NAME
                writer.append(",");
                writer.append(String.valueOf(invoicePaid.getAmount()));//AMOUNT
                writer.append(",");
                writer.append(invoicePaid.getCurrency());              //CURRENCY
                writer.append(",");
                writer.append(invoicePaid.getDate());                  //DATE
                writer.append("\n");

            }

            System.out.println("Invoices has been written to " + fileName);
        } catch (IOException e) {
            System.err.println("Error occurred while writing CSV: " + e.getMessage());
        }
    }

    public static void writeAllInvoicesInFile(List<InvoiceUsd> usdInvoicesList, List<InvoiceEuro> euroInvoicesList){
        // List<InvoiceUsd> usdInvoicesList = this.getAllUsdInvoices();
        //List<InvoiceEuro> euroInvoicesList = this.getAllEuroInvoices();
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

            }
            writer.append("\n");
            writer.append("####,####,####,####\n");
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
