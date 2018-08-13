/*
 * Copyright BigchainDB GmbH and BigchainDB contributors
 * SPDX-License-Identifier: (Apache-2.0 AND CC-BY-4.0)
 * Code is Apache-2.0 and docs are CC-BY-4.0
 */
package com.bigchaindb.api;

import com.bigchaindb.constants.BigchainDbApi;
import com.bigchaindb.model.Assets;
import com.bigchaindb.model.BigChainDBGlobals;
import com.bigchaindb.util.JsonUtils;
import com.bigchaindb.util.NetworkUtils;

import okhttp3.Response;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * The Class AssetsApi.
 */
public class AssetsApi {
	
	private static final org.slf4j.Logger log = LoggerFactory.getLogger( AssetsApi.class );
	/**
	 * Gets the assets.
	 *
	 * @param searchKey the search key
	 * @return the assets
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static Assets getAssets(String searchKey) throws IOException {
		log.debug( "getAssets Call :" + searchKey );
		Response response = NetworkUtils.sendGetRequest(BigChainDBGlobals.getBaseUrl() + BigchainDbApi.ASSETS + "/?search="+ searchKey);
		String body = response.body().string();
		response.close();
		return JsonUtils.fromJson(body, Assets.class);
	}
	
	/**
	 * Gets the assets with limit.
	 *
	 * @param searchKey the search key
	 * @param limit the limit
	 * @return the assets with limit
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static Assets getAssetsWithLimit(String searchKey, String limit) throws IOException {
		log.debug( "getAssetsWithLimit Call :" + searchKey + " limit " + limit );
		Response response = NetworkUtils.sendGetRequest(BigChainDBGlobals.getBaseUrl() + BigchainDbApi.ASSETS + "/?search="+ searchKey+ "&limit=" + limit);
		String body = response.body().string();
		response.close();
		return JsonUtils.fromJson(body, Assets.class);
	}
}
