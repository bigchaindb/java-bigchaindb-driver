/*
 * Copyright BigchainDB GmbH and BigchainDB contributors
 * SPDX-License-Identifier: (Apache-2.0 AND CC-BY-4.0)
 * Code is Apache-2.0 and docs are CC-BY-4.0
 */
package com.bigchaindb.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;


/**
 * The Class Block.
 */
public class Block {

    /**
     * The id.
     */
    @SerializedName("height")
    private Integer height;

    /**
     * The transactions.
     */
    @SerializedName("transactions")
    private List<Transaction> transactions;

    /**
     * Gets the height.
     *
     * @return the height
     */
    public Integer getHeight() {
        return height;
    }

    /**
     * Gets the transactions.
     *
     * @return the transactions
     */
    public List<Transaction> getTransactions() {
        return transactions;
    }

}
