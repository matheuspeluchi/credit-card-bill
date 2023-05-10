package com.mrp.creditcardbill.job;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CerditCardBillJobConfig {

  @Bean
  public Job cerditCardBillJob(JobRepository jobRepository, Step step) {
    return new JobBuilder("cerditCardBillJob", jobRepository)
        .start(step)
        .incrementer(new RunIdIncrementer())
        .build();
  }
}
