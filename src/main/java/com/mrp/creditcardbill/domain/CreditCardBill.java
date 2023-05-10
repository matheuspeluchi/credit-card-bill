package com.mrp.creditcardbill.domain;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreditCardBill {
  private Customer customer;
  private CreditCard creditCard;
  private List<Transaction> transactions = new ArrayList<>();

  public Double getTotal() {
    return transactions
        .stream()
        .mapToDouble(Transaction::getValue)
        .reduce(0.0, Double::sum);
  }
}