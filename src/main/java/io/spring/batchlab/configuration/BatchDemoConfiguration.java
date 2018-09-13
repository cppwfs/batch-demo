/*
 * Copyright 2017 the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *          http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package io.spring.batchlab.configuration;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.task.configuration.EnableTask;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@EnableBatchProcessing
@EnableTask
@Configuration
public class BatchDemoConfiguration {

	private static final Log logger = LogFactory.getLog(BatchDemoConfiguration.class);


	/**
	 * The name of our job: {@value}.
	 */
	public static final String JOB_NAME = "batchDemoJob";

	/**
	 * The name of our step: {@value}. A job can have many steps, but this job has
	 * only one.
	 */
	public static final String STEP_NAME = "step1";

	// Created automatically by Spring Boot
	public JobBuilderFactory jobBuilderFactory;

	// Created automatically by Spring Boot
	public StepBuilderFactory stepBuilderFactory;

	/**
	 * Get Spring to pass in the factories created by Spring Boot.
	 *
	 * @param jobBuilderFactory
	 *            A builder for creating a new Job.
	 * @param stepBuilderFactory
	 *            A builder for creating a new Job Step.
	 */
	@Autowired
	public BatchDemoConfiguration(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory) {
		super();
		this.jobBuilderFactory = jobBuilderFactory;
		this.stepBuilderFactory = stepBuilderFactory;
	}

	/**
	 * Setup the Job using the specified step. A job may
	 * have many steps, this one has just one.
	 *
	 * @param step1
	 *            The only step in this job.
	 * @return
	 */
	@Bean
	public Job importUserJob(Step step1) {
		return jobBuilderFactory.get(JOB_NAME) // Get a builder for this job
				.flow(step1) // The step that the job actually runs
				.end() // End of job definition
				.build(); // Build the job
	}

	/**
	 * Our Step just prints "Job was run" to the console.
	 * @return The step.
	 */
	@Bean
	public Step step1() {
		return stepBuilderFactory.get(STEP_NAME) // Get a builder for this step
				.tasklet(new DemoTasklet()).build();
	}


	public static class DemoTasklet implements Tasklet {
		@Override
		public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
			logger.info("Job was run");
			//return RepeatStatus.FINISHED;
			throw new IllegalStateException("No batch task for you!");
		}
	}
}
