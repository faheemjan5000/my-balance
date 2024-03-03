package com.example.waqas.balance.mapper;

import com.example.waqas.balance.dto.InvoiceDTO;
import com.example.waqas.balance.model.Invoice;
import com.example.waqas.balance.model.InvoiceOld;
import com.example.waqas.balance.utility.Utilities;

public class InvoiceMapper {
    private static final String PAYMENT_DONE = "YES";
    private static final String PAYMENT_NOT_DONE = "NO";
    public static Invoice convertInvoiceDTOintoInvoice(InvoiceDTO invoiceDTO) {
        Invoice invoice = new Invoice();
        invoice.setInvoicerName(invoiceDTO.getInvoicerName());
        invoice.setDate(invoiceDTO.getDate());
        invoice.setAmount(invoiceDTO.getAmount());
        invoice.setCurrency(invoiceDTO.getCurrency());
        if(invoiceDTO.isPaid()){
            invoice.setPaid(PAYMENT_DONE);
        }else {
            invoice.setPaid(PAYMENT_NOT_DONE);
        }
        return invoice;
    }

    public static InvoiceOld mapInvoiceToInvoiceOld(Invoice invoice) {
        InvoiceOld invoiceOld = new InvoiceOld();

        invoiceOld.setId(invoice.getId());
        invoiceOld.setAmount(invoice.getAmount());
        invoiceOld.setInvoicerName(invoice.getInvoicerName());
        invoiceOld.setDate(invoice.getDate());
        invoiceOld.setCurrency(invoice.getCurrency());
        invoiceOld.setPaid(invoice.getPaid());

        return invoiceOld;
    }
}
