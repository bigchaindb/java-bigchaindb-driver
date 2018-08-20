/*
 * Copyright BigchainDB GmbH and BigchainDB contributors
 * SPDX-License-Identifier: (Apache-2.0 AND CC-BY-4.0)
 * Code is Apache-2.0 and docs are CC-BY-4.0
 */
package com.bigchaindb.model;

import com.google.gson.annotations.SerializedName;



/**
 * The Class FulFill.
 */
public class FulFill {
	
	/** The output index. */
	@SerializedName("output_index")
	private int outputIndex = 0;
	
	/** The transaction id. */
	@SerializedName("transaction_id")
	private String transactionId = "";

	/**
	 * Gets the output index.
	 *
	 * @return the output index
	 */
	public Integer getOutputIndex() {
		return outputIndex;
	}

	/**
	 * Sets the output index.
	 *
	 * @param outputIndex the new output index
	 */
	public void setOutputIndex(Integer outputIndex) {
		this.outputIndex = outputIndex;
	}

	/**
	 * Gets the transaction id.
	 *
	 * @return the transaction id
	 */
	public String getTransactionId() {
		return transactionId;
	}

	/**
	 * Sets the transaction id.
	 *
	 * @param transactionId the new transaction id
	 */
	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	
}
