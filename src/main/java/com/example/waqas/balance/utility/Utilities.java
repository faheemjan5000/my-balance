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
        log.info("Utilities.writeOrUpdateInvoicesFile() method is called...");
        String fileName = "invoices.csv";
        log.info("starting writing invoices.csv file...");
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

            Utilities.writeAllCurrenciesIntoFile(sumOfAllCurrencies);

            log.info("Invoices have been written to " + fileName);
        } catch (IOException e) {
            log.error("Error occurred while writing CSV file : "+fileName+" message :" + e.getMessage());
        }
    }

    public static void  writeAllCurrenciesIntoFile(AllCurrencies sumOfAllCurrencies) {
        log.info("Utilities.writeAllCurrenciesIntoFile() method is called...");
        String fileName = "AllCurrencies.csv";
        log.info("starting to write CSV file : {}",fileName);
        try (FileWriter writer = new FileWriter(fileName)) {
            writer.append("USD,EURO\n");
            writer.append(String.valueOf(sumOfAllCurrencies.getUsd()));
            writer.append(",");
            writer.append(String.valueOf(sumOfAllCurrencies.getEuro()));
            writer.append("\n");

            log.info("Invoices have been written to " + fileName);
        } catch (IOException e) {
            log.error("Error occurred while writing CSV file : "+fileName+" message :" + e.getMessage());
        }
    }
        public static void writeOldInvoicesFile(List<InvoiceOld> oldInvoicesList){
        String fileName = "oldInvoices.csv";
        try (FileWriter writer = new FileWriter(fileName)) {
            // Write header
            writer.append("ID,NAME,AMOUNT,AMOUNT_IN_EURO,CURRENCY,PAID,DATE\n");

            // Write InvoiceUsd data
            for (InvoiceOld invoiceOld : oldInvoicesList) {
                writer.append(String.valueOf(invoiceOld.getId())); //ID
                writer.append(",");
                writer.append(invoiceOld.getInvoicerName());       //NAME
                writer.append(",");
                writer.append(String.valueOf(invoiceOld.getAmount()));//AMOUNT
                writer.append(",");
                writer.append(String.valueOf(invoiceOld.getAmountInEuro()));//AMOUNT_IN_EURO
                writer.append(",");
                writer.append(invoiceOld.getCurrency());              //CURRENCY
                writer.append(",");
                writer.append(invoiceOld.getPaid());                  //PAID
                writer.append(",");
                writer.append(invoiceOld.getDate());                  //DATE
                writer.append("\n");

            }

           log.info("Invoices has been written to " + fileName);
        } catch (IOException e) {
            log.error("Error occurred while writing CSV file : "+fileName+" message :" + e.getMessage());
        }
    }

}
