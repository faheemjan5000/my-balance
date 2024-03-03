package com.example.waqas.balance.utility;

import com.example.waqas.balance.model.AllCurrencies;
import com.example.waqas.balance.model.Invoice;
import com.example.waqas.balance.model.InvoiceOld;
import lombok.extern.slf4j.Slf4j;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

@Slf4j
public class Utilities {


    public static void writeOrUpdateInvoicesFile(List<Invoice> invoicesList, AllCurrencies sumOfAllCurrencies){
        // List<InvoicePaid> paidInvoicesList = invoicePaidService.getAllPaidInvoices();
        String fileName = "invoices.csv";
        try (FileWriter writer = new FileWriter(fileName)) {
            // Write header
            writer.append("ID,NAME,AMOUNT,CURRENCY,PAID,DATE\n");

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
                writer.append(invoice.getPaid());                  //PAID
                writer.append(",");
                writer.append(invoice.getDate());                  //DATE
                writer.append("\n");

            }

            writer.append("\n");
            writer.append("\n");
            writer.append("\n");

            writer.append("BALANCE");
            writer.append("\n");
            writer.append("USD,EURO\n");
            writer.append(String.valueOf(sumOfAllCurrencies.getUsd()));
            writer.append(",");
            writer.append(String.valueOf(sumOfAllCurrencies.getEuro()));
            writer.append("\n");
    writer.close();

            System.out.println("Invoices has been written to " + fileName);
        } catch (IOException e) {
            System.err.println("Error occurred while writing CSV: " + e.getMessage());
        }
    }


    public static void writeOldInvoicesFile(List<InvoiceOld> oldInvoicesList){
        String fileName = "oldInvoices.csv";
        try (FileWriter writer = new FileWriter(fileName)) {
            // Write header
            writer.append("ID,NAME,AMOUNT,CURRENCY,PAID,DATE\n");

            // Write InvoiceUsd data
            for (InvoiceOld invoiceOld : oldInvoicesList) {
                writer.append(String.valueOf(invoiceOld.getId())); //ID
                writer.append(",");
                writer.append(invoiceOld.getInvoicerName());       //NAME
                writer.append(",");
                writer.append(String.valueOf(invoiceOld.getAmount()));//AMOUNT
                writer.append(",");
                writer.append(invoiceOld.getCurrency());              //CURRENCY
                writer.append(",");
                writer.append(invoiceOld.getPaid());                  //PAID
                writer.append(",");
                writer.append(invoiceOld.getDate());                  //DATE
                writer.append("\n");

            }

            writer.close();

            System.out.println("Invoices has been written to " + fileName);
        } catch (IOException e) {
            System.err.println("Error occurred while writing CSV: " + e.getMessage());
        }
    }

}
