/*
 * Copyright BigchainDB GmbH and BigchainDB contributors
 * SPDX-License-Identifier: (Apache-2.0 AND CC-BY-4.0)
 * Code is Apache-2.0 and docs are CC-BY-4.0
 */
package com.bigchaindb.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;



/**
 * The Class Condition.
 */
public class Condition implements Serializable {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The details. */
	@SerializedName("details")
	private Details details;
	
	/** The uri. */
	@SerializedName("uri")
	private String uri;
	
	/**
	 * Instantiates a new condition.
	 */
	public Condition() {
	}

	/**
	 * Instantiates a new condition.
	 *
	 * @param details the details
	 * @param uri the uri
	 */
	public Condition(Details details, String uri) {
		this.details = details;
		this.uri = uri;
	}

	/**
	 * Gets the details.
	 *
	 * @return the details
	 */
	public Details getDetails() {
		return details;
	}

	/**
	 * Gets the uri.
	 *
	 * @return the uri
	 */
	public String getUri() {
		return uri;
	}

}
