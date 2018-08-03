package com.bigchaindb.constants;



/**
 * The Enum BigchainDbApi.
 */
public enum BigchainDbApi {
	
	/** The api version. */
	//	version.
	API_VERSION("/v1"),
	
	/** The streams. */
	//	Websocket
	STREAMS("/streams/valid_transactions"),
	
	/** The assets. */
	//	Core models
	ASSETS("/assets"),

	/** The metadata. */
	METADATA("/metadata"),
	
	/** The outputs. */
	OUTPUTS("/outputs"),

	/** The blocks. */
	BLOCKS("/blocks"),
	
	/** The validators. */
	VALIDATORS("/validators"),
	
	/** The transactions. */
	TRANSACTIONS("/transactions");

	/** The value. */
	private final String value;


	/**
	 * Instantiates a new bigchain db api.
	 *
	 * @param value the value
	 */
	BigchainDbApi(final String value) {
		this.value = value;
	}

	/* (non-Javadoc)
	 * @see java.lang.Enum#toString()
	 */
	@Override
	public String toString() {
		return this.value;
	}
}
