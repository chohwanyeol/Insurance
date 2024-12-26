package com.insurance.insurance.batch;

import com.insurance.insurance.service.InsuranceService;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.batch.repeat.RepeatStatus;

@Configuration
@EnableBatchProcessing
@RequiredArgsConstructor
public class BatchConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final InsuranceService insuranceService;

    @Bean
    public Job dailyJob() {
        return new org.springframework.batch.core.job.builder.JobBuilder("dailyJob", jobRepository)
                .start(deletePendingStep())
                .next(setDontRenewStep())
                .next(setRenewableStep())
                .next(setExpiredStep())
                .build();
    }

    @Bean
    public Step deletePendingStep() {
        return new StepBuilder("deletePendingStep", jobRepository)
                .tasklet((contribution, chunkContext) -> {
                    insuranceService.deleteAllByStatus("pending");
                    return RepeatStatus.FINISHED;
                }, transactionManager)
                .build();
    }

    @Bean
    public Step setDontRenewStep() {
        return new StepBuilder("setDontRenewStep", jobRepository)
                .tasklet((contribution, chunkContext) -> {
                    // 필요한 데이터를 가져온 뒤 갱신 로직 수행
                    insuranceService.setDontRenew(insuranceService.getDontRenew());
                    return RepeatStatus.FINISHED;
                }, transactionManager)
                .build();
    }


    @Bean
    public Step setRenewableStep() {
        return new StepBuilder("setRenewableStep", jobRepository)
                .tasklet((contribution, chunkContext) -> {
                    // 필요한 데이터를 가져온 뒤 갱신 로직 수행
                    insuranceService.setInsuranceRenewable(insuranceService.getInsuranceRenewable());
                    return RepeatStatus.FINISHED;
                }, transactionManager)
                .build();
    }

    @Bean
    public Step setExpiredStep() {
        return new StepBuilder("setExpiredStep", jobRepository)
                .tasklet((contribution, chunkContext) -> {
                    insuranceService.setInsuranceExpired();
                    return RepeatStatus.FINISHED;
                }, transactionManager)
                .build();
    }
}
