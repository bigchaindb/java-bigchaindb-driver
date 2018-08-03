package com.bigchaindb.model;

import java.util.ArrayList;
import java.util.List;


/**
 * The Class MetaDatas.
 */
public class MetaDatas {

	/** The assets. */
	private List<MetaData> metaDatas = new ArrayList<MetaData>();

	/**
	 * Gets the metadata.
	 *
	 * @return the metadata
	 */
	public List<MetaData> getMetaDatas() {
		return metaDatas;
	}

	/**
	 * Adds the metadata.
	 *
	 * @param metadata the metadata
	 */
	public void addMetaData(MetaData metadata) {
		this.metaDatas.add(metadata);
	}

	/**
	 * How many metadata are there
	 *
	 * @return the number of metadata
	 */
	public int size()
	{
		return metaDatas != null ? metaDatas.size() : 0;
	}
}
