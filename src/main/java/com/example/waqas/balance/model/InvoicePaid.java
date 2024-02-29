package com.example.waqas.balance.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class InvoicePaid {  //These are the payment done to wiki

    @Id
    private Integer id;
    private String currency;
    private Double amount;
    private String invoicerName;
    private String date;   //this is the date of when invoice was made.
}
