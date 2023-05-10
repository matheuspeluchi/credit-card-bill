package com.mrp.creditcardbill.reader;

import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.ItemStreamReader;

import com.mrp.creditcardbill.domain.CreditCardBill;
import com.mrp.creditcardbill.domain.Transaction;

public class CreditCardBillReaderConfig implements ItemStreamReader<CreditCardBill> {

  private ItemStreamReader<Transaction> delegate;
  private Transaction currentTransaction;

  public CreditCardBillReaderConfig(ItemStreamReader<Transaction> delegate) {
    this.delegate = delegate;
  }

  @Override
  public CreditCardBill read() throws Exception {
    if (currentTransaction == null)
      currentTransaction = delegate.read();

    CreditCardBill creditCardBill = null;
    Transaction transaction = currentTransaction;
    if (transaction != null) {
      creditCardBill = new CreditCardBill();
      creditCardBill.setCreditCard(transaction.getCreditCard());
      creditCardBill.setCustomer(transaction.getCreditCard().getCustomer());
      creditCardBill.getTransactions().add(transaction);

      while (isCorrelatedTransaction(transaction))
        creditCardBill.getTransactions().add(currentTransaction);
    }

    return creditCardBill;
  }

  private boolean isCorrelatedTransaction(Transaction transaction) throws Exception {
    return peek() != null && transaction.getCreditCard().getCreditCardNumber() == currentTransaction
        .getCreditCard().getCreditCardNumber();
  }

  private Transaction peek() throws Exception {
    currentTransaction = delegate.read();
    return currentTransaction;
  }

  @Override
  public void close() throws ItemStreamException {
    delegate.close();
  }

  @Override
  public void open(ExecutionContext executionContext) throws ItemStreamException {
    delegate.open(executionContext);

  }

  @Override
  public void update(ExecutionContext executionContext) throws ItemStreamException {
    delegate.update(executionContext);
  }

}
