/*
 * Copyright BigchainDB GmbH and BigchainDB contributors
 * SPDX-License-Identifier: (Apache-2.0 AND CC-BY-4.0)
 * Code is Apache-2.0 and docs are CC-BY-4.0
 */
package com.bigchaindb.json.strategy;

import com.bigchaindb.annotations.Exclude;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;



/**
 * The Class CustomExclusionStrategy.
 */
public class CustomExclusionStrategy implements ExclusionStrategy {

	/* (non-Javadoc)
	 * @see com.google.gson.ExclusionStrategy#shouldSkipField(com.google.gson.FieldAttributes)
	 */
	public boolean shouldSkipField(FieldAttributes f) {
		if(f.getAnnotation(Exclude.class)!=null){
			return true;
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see com.google.gson.ExclusionStrategy#shouldSkipClass(java.lang.Class)
	 */
	public boolean shouldSkipClass(Class clazz) {
		return false;
	}

}