package com.mrp.creditcardbill.reader;

import java.sql.ResultSet;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.Nullable;
import com.mrp.creditcardbill.domain.CreditCard;
import com.mrp.creditcardbill.domain.Customer;
import com.mrp.creditcardbill.domain.Transaction;

@Configuration
public class DatabaseTransactionReaderConfig {

  @Bean
  public JdbcCursorItemReader<Transaction> databasCreditCardBillReader(
      @Qualifier("appDataSource") DataSource dataSource) {
    return new JdbcCursorItemReaderBuilder<Transaction>().name("databasCreditCardBillReader")
        .dataSource(dataSource)
        .sql(
            "select * from transaction join credit_card using (credit_card_number) order by credit_card_number")
        .rowMapper(rowMappter()).build();
  }

  private RowMapper<Transaction> rowMappter() {
    return new RowMapper<Transaction>() {

      @Override
      @Nullable
      public Transaction mapRow(ResultSet rs, int rowNum) throws SQLException {
        CreditCard creditCard = new CreditCard();
        creditCard.setCreditCardNumber(rs.getInt("credit_card_number"));

        Customer customer = new Customer();
        customer.setId(rs.getInt("customer"));

        creditCard.setCustomer(customer);

        Transaction transaction = new Transaction();
        transaction.setId(rs.getInt("id"));
        transaction.setCreditCard(creditCard);
        transaction.setDate(rs.getDate("date"));
        transaction.setValue(rs.getDouble("value"));
        transaction.setDescription(rs.getString("description"));

        return transaction;

      }

    };
  }
}
