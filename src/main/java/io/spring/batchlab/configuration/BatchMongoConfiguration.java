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

import java.util.Collections;
import java.util.Map;

import javax.sql.DataSource;
import org.bson.types.ObjectId;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.builder.MongoItemReaderBuilder;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.task.configuration.EnableTask;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;

@EnableBatchProcessing
@EnableTask
@Configuration
public class BatchMongoConfiguration {

	@Autowired
	private MongoTemplate mongoTemplate;

	@Autowired
	private JobBuilderFactory jobBuilderFactory;

	@Autowired
	private StepBuilderFactory stepBuilderFactory;

	@Bean
	public Job readReport(Step step) {
		return jobBuilderFactory.get("readReport").incrementer(new RunIdIncrementer()).flow(step).end().build();
	}

	@Bean
	public Step step1(ItemReader itemReader, ItemProcessor itemProcessor, ItemWriter itemWriter) {
		return stepBuilderFactory.get("step1").<Map<Object, Object>, Map<Object, Object>>chunk(10).reader(itemReader)
				.processor(itemProcessor)
				.writer(itemWriter).build();
	}
	@Bean
	public ItemReader<Map<Object, Object>> itemReader() {
		return new MongoItemReaderBuilder().
				template(mongoTemplate).
				jsonQuery("{}").
				name("purchase_orders").
				sorts(Collections.singletonMap("_id", Sort.Direction.DESC)).
				targetType(Map.class).
				collection("purchase_orders").
				build();
	}

	@Bean
	public ItemProcessor<Map<Object,Object>, Map<Object,Object>> itemProcessor() {
		return new ItemProcessor<Map<Object,Object>, Map<Object,Object>>() {
			@Override
			public Map<Object,Object> process(Map<Object,Object> item) throws Exception {
				if(((ObjectId)item.get("_id")).toString().endsWith("7")) {
					item.put("card_type", "visa");
				}
				else if(((ObjectId)item.get("_id")).toString().endsWith("8")) {
					item.put("card_type", "amex");
				}
				else if(((ObjectId)item.get("_id")).toString().endsWith("9")) {
					item.put("card_type", "discover");
				}
				return item;
			}
		};
	}

	@Bean
	public ItemWriter<Map<Object,Object>> itemWriter(DataSource dataSource) {
		return new JdbcBatchItemWriterBuilder<Map<Object, Object>>().
				sql("INSERT INTO credit_transaction (clicks, zone, sku, amount, " +
						"quantity, mode, card_type) VALUES (:clicks, :zone, :sku, " +
						":amount, :quantity, :mode, :card_type)").
				dataSource(dataSource).
				columnMapped().
				build();
	}

}
