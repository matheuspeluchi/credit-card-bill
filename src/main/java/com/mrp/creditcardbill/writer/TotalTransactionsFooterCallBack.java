package com.mrp.creditcardbill.writer;

import java.io.IOException;
import java.io.Writer;
import java.text.NumberFormat;
import java.util.List;

import org.springframework.batch.core.annotation.AfterChunk;
import org.springframework.batch.core.annotation.BeforeWrite;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.file.FlatFileFooterCallback;

import com.mrp.creditcardbill.domain.CreditCardBill;

public class TotalTransactionsFooterCallBack implements FlatFileFooterCallback {

  private Double total = 0.0;

  @Override
  public void writeFooter(Writer writer) throws IOException {
    writer.write(String.format("%n%121s", "Total: " + NumberFormat.getCurrencyInstance().format(total)));
  }

  @BeforeWrite
  public void beforeWrite(Chunk<CreditCardBill> bills) {
    for (CreditCardBill creditCardBill : bills)
      total += creditCardBill.getTotal();
  }

  @AfterChunk
  public void afterChunk(ChunkContext chunkContext) {
    total = 0.0;
  }

}
