/*
 * Copyright BigchainDB GmbH and BigchainDB contributors
 * SPDX-License-Identifier: (Apache-2.0 AND CC-BY-4.0)
 * Code is Apache-2.0 and docs are CC-BY-4.0
 */
package com.bigchaindb.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;



/**
 * The Class Input.
 */
public class Input implements Serializable {

	/** The string fulillment, if applicable. */
	@SerializedName("fulfillment")
	private String fullFillment = null;

	/** The fulfillment details, if applicable. */
	private Details fullFillmentDetails = null;
	
	/** The ful fills. */
	@SerializedName("fulfills")
	private FulFill fulFills = null;
	
	/** The owners before. */
	@SerializedName("owners_before")
	private List<String> ownersBefore = new ArrayList<String>();

	/**
	 * Gets the full fillment.
	 *
	 * @return the full fillment
	 */
	public String getFullFillment() {
		return fullFillment;
	}

	/**
	 * Gets the fulfillment details.
	 *
	 * @return fulfillment details.
	 */
	public Details getFullFillmentDetails() {
		return fullFillmentDetails;
	}

	/**
	 * Sets the full fillment as a string.
	 *
	 * @param fullFillment the new full fillment
	 */
	public void setFullFillment(String fullFillment) {
		this.fullFillment = fullFillment;
	}

	/**
	 * Sets the full fillment as a set of details.
	 *
	 * @param fullFillment the new full fillment
	 */
	public void setFullFillment(Details fullFillment) {
		this.fullFillmentDetails = fullFillment;
	}

	/**
	 * Gets the ful fills.
	 *
	 * @return the ful fills
	 */
	public FulFill getFulFills() {
		return fulFills;
	}

	/**
	 * Sets the ful fills.
	 *
	 * @param fulFills the new ful fills
	 */
	public void setFulFills(FulFill fulFills) {
		this.fulFills = fulFills;
	}

	/**
	 * Gets the owners before.
	 *
	 * @return the owners before
	 */
	public List<String> getOwnersBefore() {
		return ownersBefore;
	}

	/**
	 * Adds the owner.
	 *
	 * @param owner the owner
	 */
	public void addOwner(String owner) {
		this.ownersBefore.add(owner);
	}

	
	
}
