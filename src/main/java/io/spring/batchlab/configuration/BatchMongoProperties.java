/*
 * Copyright 2020 the original author or authors.
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

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "io.spring.batch.mongo")
public class BatchMongoProperties {

	private String jobName = "financialReport";

	private int chunkSize = 10;

	private String query = "{}";

	private String itemReaderName = "purchase_orders";

	private String sortField = "_id";

	private String collection = "purchase_orders";

	private String jdbcWriteString = "INSERT INTO credit_transaction (clicks, zone, sku, amount, " +
			"quantity, mode, transaction_fee) VALUES (:clicks, :zone, :sku, " +
			":amount, :quantity, :mode, :transaction_fee)";

	private String stepName = "financialReportStep";

	private double financeRate = .035;

	public String getJobName() {
		return jobName;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	public int getChunkSize() {
		return chunkSize;
	}

	public void setChunkSize(int chunkSize) {
		this.chunkSize = chunkSize;
	}

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public String getItemReaderName() {
		return itemReaderName;
	}

	public void setItemReaderName(String itemReaderName) {
		this.itemReaderName = itemReaderName;
	}

	public String getSortField() {
		return sortField;
	}

	public void setSortField(String sortField) {
		this.sortField = sortField;
	}

	public String getCollection() {
		return collection;
	}

	public void setCollection(String collection) {
		this.collection = collection;
	}

	public String getJdbcWriteString() {
		return jdbcWriteString;
	}

	public void setJdbcWriteString(String jdbcWriteString) {
		this.jdbcWriteString = jdbcWriteString;
	}

	public String getStepName() {
		return stepName;
	}

	public void setStepName(String stepName) {
		this.stepName = stepName;
	}

	public double getFinanceRate() {
		return financeRate;
	}

	public void setFinanceRate(double financeRate) {
		this.financeRate = financeRate;
	}
}
