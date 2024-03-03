package com.example.waqas.balance.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InvoiceDTO {

    @NotEmpty(message = "Name should not be empty")
    private String invoicerName;
    @Min(value = 1 , message = "Amount should not be less than 1")
    private Double amount;
    @Pattern(regexp = "^(USD|EURO)$", message = "currency should be 'USD' or 'EURO'")
    private String currency;
    private String date;
    @Column(name = "PAID")
    private boolean paid;
}
