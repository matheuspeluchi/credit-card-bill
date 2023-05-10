package com.mrp.creditcardbill.writer;

import java.io.IOException;
import java.io.Writer;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import org.springframework.batch.item.file.FlatFileFooterCallback;
import org.springframework.batch.item.file.FlatFileHeaderCallback;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.MultiResourceItemWriter;
import org.springframework.batch.item.file.ResourceSuffixCreator;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.batch.item.file.builder.MultiResourceItemWriterBuilder;
import org.springframework.batch.item.file.transform.LineAggregator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import com.mrp.creditcardbill.domain.CreditCardBill;
import com.mrp.creditcardbill.domain.Transaction;

@Configuration
public class CreditCardBillFileWriterConfig {

  @Bean
  public MultiResourceItemWriter<CreditCardBill> creditCardBillFileWriter() {
    return new MultiResourceItemWriterBuilder<CreditCardBill>().name("creditCardBillFileWriter")
        .resource(new FileSystemResource("files/bill_"))
        .itemCountLimitPerResource(1)
        .resourceSuffixCreator(sufixCreator())
        .delegate(creditCardBillFile())
        .build();

  }

  private FlatFileItemWriter<CreditCardBill> creditCardBillFile() {
    return new FlatFileItemWriterBuilder<CreditCardBill>().name("creditCardBillFile")
        .resource(new FileSystemResource("files/bill_.txt"))
        .lineAggregator(lineAggregator())
        .headerCallback(headerCallBack())
        .footerCallback(footerCallBack())
        .build();
  }

  @Bean
  public FlatFileFooterCallback footerCallBack() {
    return new TotalTransactionsFooterCallBack();
  }

  private FlatFileHeaderCallback headerCallBack() {
    return new FlatFileHeaderCallback() {

      @Override
      public void writeHeader(Writer writer) throws IOException {
        writer.append(String.format("%121s\n", "Card XPTO"));
        writer.append(String.format("%121s\n\n", "Fifth Avenue, 150"));
      }

    };
  }

  private LineAggregator<CreditCardBill> lineAggregator() {
    return new LineAggregator<CreditCardBill>() {

      @Override
      public String aggregate(CreditCardBill creditCardBill) {
        StringBuilder writer = new StringBuilder();
        writer.append(String.format("Name: %s\n", creditCardBill.getCustomer().getName()));
        writer.append(String.format("Adress: %s\n\n\n", creditCardBill.getCustomer().getAddress()));
        writer.append(String.format("Card full invoice %d\n",
            creditCardBill.getCreditCard().getCreditCardNumber()));
        writer.append(
            "------------------------------------------------------------------------------------------------------------------------\n");
        writer.append("Date Description value \n");
        writer.append(
            "------------------------------------------------------------------------------------------------------------------------\n");

        for (Transaction transaction : creditCardBill.getTransactions()) {
          writer.append(String.format("\n[%10s] %-80s - %s",
              new SimpleDateFormat("dd/MM/yyyy").format(transaction.getDate()),
              transaction.getDescription(),
              NumberFormat.getCurrencyInstance().format(transaction.getValue())));
        }
        return writer.toString();
      }

    };
  }

  private ResourceSuffixCreator sufixCreator() {
    return new ResourceSuffixCreator() {

      @Override
      public String getSuffix(int index) {
        return index + ".txt";
      }
    };
  }
}
