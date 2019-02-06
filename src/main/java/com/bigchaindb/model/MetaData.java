/*
 * Copyright BigchainDB GmbH and BigchainDB contributors
 * SPDX-License-Identifier: (Apache-2.0 AND CC-BY-4.0)
 * Code is Apache-2.0 and docs are CC-BY-4.0
 */
package com.bigchaindb.model;

import com.bigchaindb.annotations.Exclude;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/*
 * The Class MetaData.
 */
public class MetaData implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** The id. */
	@SerializedName("id")
	@Exclude
	private String id;
	
	/** The data. */
	@SerializedName("data")
	private Object data;

	/** the data class the type of the data class needed for serialization/deserialization */
	@Exclude
	private Class dataClass = com.google.gson.internal.LinkedTreeMap.class;

	/**
	 * Instantiates a new metadata.
	 */
	public MetaData() {}
	
	/**
	 * Instantiates a new metadata.
	 *
	 * @param data the data
	 * @param dataClass due to type erasure the data class needs to be provided for serialization/deserialization
	 */
	public MetaData(Object data, Class dataClass) {
		this.data = data;
		this.dataClass = dataClass;
	}

	/**
	 * Instantiates a new metadata by reference.
	 *
	 * @param id ID of the metadata.
	 */
	public MetaData(String id) {
		this.id = id;
	}
	
	/**
	 * Gets the data.
	 *
	 * @return the data
	 */
	public Object getMetaData() {
		return data;
	}

	/**
	 * return the type of the Asset data class
	 *
	 * @return  the data class type
	 */
	public Class getDataClass()
	{
		return dataClass;
	}

	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	public String getId() {
		return id;
	}
}
