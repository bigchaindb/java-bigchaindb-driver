/*
 * Copyright BigchainDB GmbH and BigchainDB contributors
 * SPDX-License-Identifier: (Apache-2.0 AND CC-BY-4.0)
 * Code is Apache-2.0 and docs are CC-BY-4.0
 */
package com.bigchaindb.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;



/**
 * The Class Transactions.
 */
public class Transactions implements Serializable {
	
	/** The transactions. */
	private List<Transaction> transactions = new ArrayList<Transaction>();

	/**
	 * Gets the transactions.
	 *
	 * @return the transactions
	 */
	public List<Transaction> getTransactions() {
		return transactions;
	}

	/**
	 * Adds the transaction.
	 *
	 * @param transaction the transaction
	 */
	public void addTransaction(Transaction transaction) {
		this.transactions.add(transaction);
	}
	
	
}
