package com.mrp.creditcardbill.processor;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.validator.ValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.mrp.creditcardbill.domain.CreditCardBill;
import com.mrp.creditcardbill.domain.Customer;

@Component
public class LoadCustomerDataProcessor implements ItemProcessor<CreditCardBill, CreditCardBill> {

  private RestTemplate restTemplate = new RestTemplate();

  @Override
  public CreditCardBill process(@NonNull CreditCardBill creditCardBill) throws Exception {
    String uri = String.format("http://my-json-server.typicode.com/matheuspeluchi/json-server/profile/%d",
        creditCardBill.getCustomer().getId());
    ResponseEntity<Customer> response = restTemplate.getForEntity(uri, Customer.class);

    if (response.getStatusCode() != HttpStatus.OK)
      throw new ValidationException("Customer not found!");

    creditCardBill.setCustomer(response.getBody());
    return creditCardBill;
  }

}
