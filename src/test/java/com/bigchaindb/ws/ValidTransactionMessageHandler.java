/*
 * Copyright BigchainDB GmbH and BigchainDB contributors
 * SPDX-License-Identifier: (Apache-2.0 AND CC-BY-4.0)
 * Code is Apache-2.0 and docs are CC-BY-4.0
 */
package com.bigchaindb.ws;

import com.bigchaindb.model.ValidTransaction;
import com.bigchaindb.util.JsonUtils;
import com.bigchaindb.ws.MessageHandler;

public class ValidTransactionMessageHandler implements MessageHandler {
	@Override
	public void handleMessage(String message) {
		ValidTransaction validTransaction = JsonUtils.fromJson(message, ValidTransaction.class);
	}
}
