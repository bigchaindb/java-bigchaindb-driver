/*
 * Copyright BigchainDB GmbH and BigchainDB contributors
 * SPDX-License-Identifier: (Apache-2.0 AND CC-BY-4.0)
 * Code is Apache-2.0 and docs are CC-BY-4.0
 */
package com.bigchaindb.model;

import java.util.ArrayList;
import java.util.List;



/**
 * The Class Assets.
 */
public class Assets {

	/** The assets. */
	private List<Asset> assets = new ArrayList<Asset>();

	/**
	 * Gets the assets.
	 *
	 * @return the assets
	 */
	public List<Asset> getAssets() {
		return assets;
	}

	/**
	 * Adds the asset.
	 *
	 * @param asset the asset
	 */
	public void addAsset(Asset asset) {
		this.assets.add(asset);
	}

	/**
	 * How many assets are there
	 *
	 * @return the number of assets
	 */
	public int size()
	{
		return assets != null ? assets.size() : 0;
	}
}
