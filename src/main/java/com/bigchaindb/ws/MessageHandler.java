/*
 * Copyright BigchainDB GmbH and BigchainDB contributors
 * SPDX-License-Identifier: (Apache-2.0 AND CC-BY-4.0)
 * Code is Apache-2.0 and docs are CC-BY-4.0
 */
package com.bigchaindb.ws;


/**
 * The Interface MessageHandler.
 */
public interface MessageHandler {
	
	/**
	 * Handle message.
	 *
	 * @param message the message
	 */
	public void handleMessage(String message);
}
