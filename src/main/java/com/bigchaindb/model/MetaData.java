package com.bigchaindb.model;

import com.google.gson.annotations.SerializedName;

import java.util.Map;
import java.util.TreeMap;



/**
 * The Class MetaData.
 */
public class MetaData {
	
	/** The id. */
	@SerializedName("id")
	private String id;
	
	/** The metadata. */
	@SerializedName("metadata")
	private Map<String,String> metadata = new TreeMap<String, String>();
	
	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * Sets the id.
	 *
	 * @param id the new id
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * Gets the metadata.
	 *
	 * @return the metadata
	 */
	public Map<String, String> getMetadata() {
		return metadata;
	}

	public void setMetaData(String key, String value) {
		this.metadata.put(key, value);
	}
}
