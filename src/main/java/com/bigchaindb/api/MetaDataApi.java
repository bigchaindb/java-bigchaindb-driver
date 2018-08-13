/*
 * Copyright BigchainDB GmbH and BigchainDB contributors
 * SPDX-License-Identifier: (Apache-2.0 AND CC-BY-4.0)
 * Code is Apache-2.0 and docs are CC-BY-4.0
 */
package com.bigchaindb.api;

import com.bigchaindb.constants.BigchainDbApi;
import com.bigchaindb.model.BigChainDBGlobals;
import com.bigchaindb.model.MetaDatas;
import com.bigchaindb.util.JsonUtils;
import com.bigchaindb.util.NetworkUtils;

import okhttp3.Response;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * The Class MetaDataApi.
 */
public class MetaDataApi {
	
	private static final org.slf4j.Logger log = LoggerFactory.getLogger( MetaDataApi.class );
	/**
	 * Gets the metadata.
	 *
	 * @param searchKey the search key
	 * @return the metadata
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static MetaDatas getMetaData(String searchKey) throws IOException {
		log.debug( "getMetaData Call :" + searchKey );
		Response response = NetworkUtils.sendGetRequest(BigChainDBGlobals.getBaseUrl() + BigchainDbApi.METADATA + "/?search="+ searchKey);
		String body = response.body().string();
		response.close();
		return JsonUtils.fromJson(body, MetaDatas.class);
	}
	
	/**
	 * Gets the metadata with limit.
	 *
	 * @param searchKey the search key
	 * @param limit the limit
	 * @return the metadata with limit
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static MetaDatas getMetaDataWithLimit(String searchKey, String limit) throws IOException {
		log.debug( "getMetaDataWithLimit Call :" + searchKey + " limit " + limit );
		Response response = NetworkUtils.sendGetRequest(BigChainDBGlobals.getBaseUrl() + BigchainDbApi.METADATA + "/?search="+ searchKey+ "&limit=" + limit);
		String body = response.body().string();
		response.close();
		return JsonUtils.fromJson(body, MetaDatas.class);
	}
}
