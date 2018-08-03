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
