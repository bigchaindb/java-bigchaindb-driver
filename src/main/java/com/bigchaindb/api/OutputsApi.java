/*
 * Copyright BigchainDB GmbH and BigchainDB contributors
 * SPDX-License-Identifier: (Apache-2.0 AND CC-BY-4.0)
 * Code is Apache-2.0 and docs are CC-BY-4.0
 */
package com.bigchaindb.api;

import com.bigchaindb.constants.BigchainDbApi;
import com.bigchaindb.model.BigChainDBGlobals;
import com.bigchaindb.model.Outputs;
import com.bigchaindb.util.JsonUtils;
import com.bigchaindb.util.NetworkUtils;

import okhttp3.Response;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * The Class OutputsApi.
 */
public class OutputsApi {

	private static final org.slf4j.Logger log = LoggerFactory.getLogger( OutputsApi.class );
	/**
	 * Gets the outputs.
	 *
	 * @param publicKey the public key
	 * @return the outputs
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static Outputs getOutputs(String publicKey) throws IOException { 
		log.debug( "getOutputs Call :" + publicKey );
		Response response = NetworkUtils.sendGetRequest(BigChainDBGlobals.getBaseUrl() + BigchainDbApi.OUTPUTS + "?public_key="+ publicKey);
		String body = response.body().string();
		response.close();
		return JsonUtils.fromJson(body, Outputs.class);
	}
	
	/**
	 * Gets the spent outputs.
	 *
	 * @param publicKey the public key
	 * @return the spent outputs
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static Outputs getSpentOutputs(String publicKey) throws IOException { 
		log.debug( "getSpentOutputs Call :" + publicKey );
		Response response = NetworkUtils.sendGetRequest(BigChainDBGlobals.getBaseUrl() + BigchainDbApi.OUTPUTS + "?public_key="+ publicKey+ "&spent=true");
		String body = response.body().string();
		response.close();
		return JsonUtils.fromJson(body, Outputs.class);
	}

	/**
	 * Gets the unspent outputs.
	 *
	 * @param publicKey the public key
	 * @return the unspent outputs
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static Outputs getUnspentOutputs(String publicKey) throws IOException {
		log.debug( "getUnspentOutputs Call :" + publicKey );
		Response response = NetworkUtils.sendGetRequest(BigChainDBGlobals.getBaseUrl() + BigchainDbApi.OUTPUTS + "?public_key="+ publicKey+ "&spent=false");
		String body = response.body().string();
		response.close();
		return JsonUtils.fromJson(body, Outputs.class);
	}
}
