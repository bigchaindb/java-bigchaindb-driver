/*
 * Copyright BigchainDB GmbH and BigchainDB contributors
 * SPDX-License-Identifier: (Apache-2.0 AND CC-BY-4.0)
 * Code is Apache-2.0 and docs are CC-BY-4.0
 */
package com.bigchaindb.api;

import com.bigchaindb.constants.BigchainDbApi;
import com.bigchaindb.model.BigChainDBGlobals;
import com.bigchaindb.model.Validators;
import com.bigchaindb.util.JsonUtils;
import com.bigchaindb.util.NetworkUtils;

import okhttp3.Response;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * The Class ValidatorsApi.
 */
public class ValidatorsApi {
	
	private static final org.slf4j.Logger log = LoggerFactory.getLogger( ValidatorsApi.class );

	/**
	 * Gets the the local validators set of a given node.
	 *
	 * @return the validators
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static Validators getValidators() throws IOException {
		log.debug( "getValidators Call" );
		Response response = NetworkUtils.sendGetRequest(BigChainDBGlobals.getBaseUrl() + BigchainDbApi.VALIDATORS);
		String body = response.body().string();
		response.close();
		return JsonUtils.fromJson(body, Validators.class);
	}

}
