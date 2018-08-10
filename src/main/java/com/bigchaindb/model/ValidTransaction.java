/*
 * Copyright BigchainDB GmbH and BigchainDB contributors
 * SPDX-License-Identifier: (Apache-2.0 AND CC-BY-4.0)
 * Code is Apache-2.0 and docs are CC-BY-4.0
 */
package com.bigchaindb.model;

import com.google.gson.annotations.SerializedName;


//{
//    "transaction_id": "<sha3-256 hash>",
//    "asset_id": "<sha3-256 hash>",
//    "block_id": "<sha3-256 hash>"
//}

/**
 * The Class ValidTransaction.
 */
public class ValidTransaction {
	
	/** The transaction id. */
	@SerializedName("transaction_id")
	private String transactionId;
	
	/** The asset id. */
	@SerializedName("asset_id")
	private String assetId;
	
	/** The block id. */
	@SerializedName("block_id")
	private String blockId;
	
	/**
	 * Gets the transaction id.
	 *
	 * @return the transaction id
	 */
	public String getTransactionId() {
		return transactionId;
	}

	/**
	 * Gets the asset id.
	 *
	 * @return the asset id
	 */
	public String getAssetId() {
		return assetId;
	}

	/**
	 * Gets the block id.
	 *
	 * @return the block id
	 */
	public String getBlockId() {
		return blockId;
	}
}
