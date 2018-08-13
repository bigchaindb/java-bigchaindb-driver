/*
 * Copyright BigchainDB GmbH and BigchainDB contributors
 * SPDX-License-Identifier: (Apache-2.0 AND CC-BY-4.0)
 * Code is Apache-2.0 and docs are CC-BY-4.0
 */
package com.bigchaindb.model;

import com.bigchaindb.util.JsonUtils;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.Map;

public abstract class DataModel {
	
	/**
	 * To map string.
	 *
	 * @return the map
	 */
	public Map<String,String> toMapString() {
		Type mapType = new TypeToken<Map<String, String>>(){}.getType();  
		Map<String, String> son = JsonUtils.getGson().fromJson(JsonUtils.toJson(this), mapType);
		return son;
	}
}
