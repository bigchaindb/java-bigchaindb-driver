package com.bigchaindb.model;

import java.util.Map;

public class Connection {

	/**
	 * node details
	 */
	private Map<String, Object> connection;
	/**
	 * how many times this node tried to connect and failed?
	 */
	private int retryCount = 0;
	/**
	 * when is the next time this node should try to connect?
	 */
	private long timeToRetryForConnection = 0;
	
	public Connection(Map<String, Object> connection) {
		this.connection = connection;
	}

	public Map<String, Object> getConnection() {
		return connection;
	}

	public int getRetryCount() {
		return retryCount;
	}

	public void setRetryCount(int retryCount) {
		this.retryCount = retryCount;
	}

	public long getTimeToRetryForConnection() {
		return timeToRetryForConnection;
	}

	public void setTimeToRetryForConnection(long timeToRetryForConnection) {
		this.timeToRetryForConnection = timeToRetryForConnection;
	}
}
