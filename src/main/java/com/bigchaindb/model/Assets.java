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
