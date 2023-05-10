package com.mrp.creditcardbill.step;

import org.springframework.batch.core.Step;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemStreamReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import com.mrp.creditcardbill.domain.CreditCardBill;
import com.mrp.creditcardbill.domain.Transaction;
import com.mrp.creditcardbill.reader.CreditCardBillReaderConfig;
import com.mrp.creditcardbill.writer.TotalTransactionsFooterCallBack;

@Configuration
public class CrediCardBillStepConfig {

  @Bean
  public Step crediCardBillStep(JobRepository repository,
      PlatformTransactionManager transactionManager,
      ItemStreamReader<Transaction> databaseTransactionReader,
      ItemProcessor<CreditCardBill, CreditCardBill> loadCustomerDataProcessor,
      ItemWriter<CreditCardBill> databasCreditCardBillWriter,
      TotalTransactionsFooterCallBack listener) {
    return new StepBuilder("crediCardBillStep", repository)
        .<CreditCardBill, CreditCardBill>chunk(1, transactionManager)
        .reader(new CreditCardBillReaderConfig(databaseTransactionReader))
        .processor(loadCustomerDataProcessor)
        .writer(databasCreditCardBillWriter)
        .listener(listener)
        .build();
  }
}
