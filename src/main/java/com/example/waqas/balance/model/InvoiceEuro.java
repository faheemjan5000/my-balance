package com.example.waqas.balance.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class InvoiceEuro {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private double euro;

}
