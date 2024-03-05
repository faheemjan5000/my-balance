package com.example.waqas.balance.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class InvoiceOld {

    @Id
    private Integer id;
    private String invoicerName;
    private Double amount;
    private Double amountInEuro;
    private String currency;
    private String date;
    private String paid; //YES,NO

}
