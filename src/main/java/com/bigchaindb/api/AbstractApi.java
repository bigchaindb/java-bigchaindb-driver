/*
 * Copyright BigchainDB GmbH and BigchainDB contributors
 * SPDX-License-Identifier: (Apache-2.0 AND CC-BY-4.0)
 * Code is Apache-2.0 and docs are CC-BY-4.0
 */
package com.bigchaindb.api;
import okhttp3.MediaType;



/**
 * The Class AbstractApi.
 */
public abstract class AbstractApi {


	/** The Constant JSON. */
	protected static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
}
