/*
 * Copyright BigchainDB GmbH and BigchainDB contributors
 * SPDX-License-Identifier: (Apache-2.0 AND CC-BY-4.0)
 * Code is Apache-2.0 and docs are CC-BY-4.0
 */
package com.bigchaindb.ws;

import com.bigchaindb.builders.BigchainDbConfigBuilder;

public class WsMonitorTest {

	public static void main(String[] args) {
		BigchainDbConfigBuilder.baseUrl("https://test.ipdb.io")
		.addToken("app_id", "2bbaf3ff")
		.addToken("app_key", "c929b708177dcc8b9d58180082029b8d")
		.webSocketMonitor(new ValidTransactionMessageHandler())
		.setup();
	}
}
