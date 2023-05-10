package com.mrp.creditcardbill.domain;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {
  private int id;
  private CreditCard creditCard;
  private Double value;
  private Date date;
  private String description;
}
