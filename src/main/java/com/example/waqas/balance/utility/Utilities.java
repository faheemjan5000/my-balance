package com.example.waqas.balance.utility;

import com.example.waqas.balance.model.AllCurrencies;
import com.example.waqas.balance.model.Invoice;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class Utilities {


    public static void writeOrUpdateInvoicesFile(List<Invoice> invoicesList, AllCurrencies sumOfAllCurrencies){
        // List<InvoicePaid> paidInvoicesList = invoicePaidService.getAllPaidInvoices();
        String fileName = "finalInvoices.csv";
        try (FileWriter writer = new FileWriter(fileName)) {
            // Write header
            writer.append("ID,NAME,AMOUNT,CURRENCY,DATE,PAID\n");

            // Write InvoiceUsd data
            for (Invoice invoice : invoicesList) {
                writer.append(String.valueOf(invoice.getId())); //ID
                writer.append(",");
                writer.append(invoice.getInvoicerName());       //NAME
                writer.append(",");
                writer.append(String.valueOf(invoice.getAmount()));//AMOUNT
                writer.append(",");
                writer.append(invoice.getCurrency());              //CURRENCY
                writer.append(",");
                writer.append(invoice.getDate());                  //DATE
                writer.append(",");
                writer.append(invoice.getPaid());                  //PAID
                writer.append("\n");
            }


            System.out.println("Invoices has been written to " + fileName);
        } catch (IOException e) {
            System.err.println("Error occurred while writing CSV: " + e.getMessage());
        }
    }

}
