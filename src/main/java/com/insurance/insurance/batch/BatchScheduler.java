package com.insurance.insurance.batch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class BatchScheduler {

    private final JobLauncher jobLauncher;
    private final Job dailyJob;

    public BatchScheduler(JobLauncher jobLauncher, Job dailyJob) {
        this.jobLauncher = jobLauncher;
        this.dailyJob = dailyJob;
    }

    @Scheduled(cron = "0 0 0 * * ?") // 매일 자정 실행
    public void runBatchJob() throws Exception {
        jobLauncher.run(dailyJob, new JobParametersBuilder()
                .addLong("time", System.currentTimeMillis()) // 파라미터를 추가하여 Job 재실행 가능
                .toJobParameters());
        System.out.println("배치 작업 완료: 자정 배치 실행");
    }
}