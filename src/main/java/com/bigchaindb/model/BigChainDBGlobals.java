/*
 * Copyright BigchainDB GmbH and BigchainDB contributors
 * SPDX-License-Identifier: (Apache-2.0 AND CC-BY-4.0)
 * Code is Apache-2.0 and docs are CC-BY-4.0
 */
package com.bigchaindb.model;

import java.util.List;
import java.util.Map;

import okhttp3.OkHttpClient;


/**
 * The Class Globals.
 */
public class BigChainDBGlobals {
	
	/** The api endpoints. */
	private static ApiEndpoints apiEndpoints;

	/**
	 * Gets the api endpoints.
	 *
	 * @return the api endpoints
	 */
	public static ApiEndpoints getApiEndpoints() {
		return apiEndpoints;
	}

	/**
	 * Sets the api endpoints.
	 *
	 * @param apiEndpoints the new api endpoints
	 */
	public static void setApiEndpoints(ApiEndpoints apiEndpoints) {
		BigChainDBGlobals.apiEndpoints = apiEndpoints;
	}

	/** The authorization tokens. */
	private static Map<String, String> authorizationTokens;
	
	/** The base url. */
	private static String baseUrl;
	
	/** The http client. */
	private static OkHttpClient httpClient;
	
	/** The ws socket url. */
	private static String wsSocketUrl;

	/**
	 * Gets the ws socket url.
	 *
	 * @return the ws socket url
	 */
	public static String getWsSocketUrl() {
		return wsSocketUrl;
	}

	/**
	 * Sets the ws socket url.
	 *
	 * @param wsSocketUrl the new ws socket url
	 */
	public static void setWsSocketUrl(String wsSocketUrl) {
		BigChainDBGlobals.wsSocketUrl = wsSocketUrl;
	}

	/**
	 * Gets the authorization tokens.
	 *
	 * @return the authorization tokens
	 */
	public static Map<String, String> getAuthorizationTokens() {
		return authorizationTokens;
	}

	/**
	 * Sets the authorization tokens.
	 *
	 * @param authorizationTokens the authorization tokens
	 */
	public static void setAuthorizationTokens(Map<String, String> authorizationTokens) {
		BigChainDBGlobals.authorizationTokens = authorizationTokens;
	}

	/**
	 * Gets the base url.
	 *
	 * @return the base url
	 */
	public static String getBaseUrl() {
		return baseUrl;
	}

	/**
	 * Sets the base url.
	 *
	 * @param baseUrl the new base url
	 */
	public static void setBaseUrl(String baseUrl) {
		BigChainDBGlobals.baseUrl = baseUrl;
	}

	/**
	 * Gets the http client.
	 *
	 * @return the http client
	 */
	public static OkHttpClient getHttpClient() {
		return httpClient;
	}

	/**
	 * Sets the http client.
	 *
	 * @param httpClient the new http client
	 */
	public static void setHttpClient(OkHttpClient httpClient) {
		BigChainDBGlobals.httpClient = httpClient;
	}

	/**
	 * indicates if connection to a node still successful
	 */
	private static boolean isConnected;
	/**
	 * timeout (defaults to 20000 ms)
	 */
	protected static int timeout = 20000;
	/**
	 * time to wait till timeout is reached
	 */
	private static long timeTillTimeout = System.currentTimeMillis() + timeout;
	/**
	 * list of nodes to connect to
	 */
	private static List<Connection> connections;
	/**
	 * delay added if connection failed
	 */
	public static final int DELAY = 500;
	/**
	 * current connected node
	 */
	private static Connection currentNode;
	
	/**
	 * check if there are multiple nodes used
	 * @return boolean
	 */
	private static boolean hasMultipleNodes =  false;
	

	public static boolean isHasMultipleNodes() {
		return hasMultipleNodes;
	}

	public static void setHasMultipleNodes(boolean hasMultipleNodes) {
		BigChainDBGlobals.hasMultipleNodes = hasMultipleNodes;
	}

	public static Connection getCurrentNode() {
		return currentNode;
	}

	public static void setCurrentNode(Connection currentNode) {
		BigChainDBGlobals.currentNode = currentNode;
	}

	public static long getTimeTillTimeout() {
		return timeTillTimeout;
	}

	public static long calculateTimeTillTimeout() {
		return timeTillTimeout - System.currentTimeMillis();
	}
	
	public static void resetTimeTillTimeout() {
		BigChainDBGlobals.timeTillTimeout = System.currentTimeMillis() 
				+ BigChainDBGlobals.getTimeout();
	}

	public static int getTimeout() {
		return timeout;
	}

	public static void setTimeout(int timeout) {
		BigChainDBGlobals.timeout = timeout;
	}

	public static boolean isConnected() {
		return isConnected;
	}

	public static void setConnected(boolean isConnected) {
		BigChainDBGlobals.isConnected = isConnected;
	}
	
	
	public static List<Connection> getConnections() {
		return connections;
	}

	public static void setConnections(List<Connection> connections) {
		BigChainDBGlobals.connections = connections;
	}
	
}
