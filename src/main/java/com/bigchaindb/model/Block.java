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
