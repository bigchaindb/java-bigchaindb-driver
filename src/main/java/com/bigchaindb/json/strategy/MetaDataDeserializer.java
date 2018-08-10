/*
 * Copyright BigchainDB GmbH and BigchainDB contributors
 * SPDX-License-Identifier: (Apache-2.0 AND CC-BY-4.0)
 * Code is Apache-2.0 and docs are CC-BY-4.0
 */
package com.bigchaindb.json.strategy;

import com.bigchaindb.model.MetaData;
import com.bigchaindb.model.MetaDatas;
import com.bigchaindb.util.JsonUtils;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

/**
 * The Class AssetsDeserializer.
 */
public class MetaDataDeserializer implements JsonDeserializer<MetaDatas> {

	/* (non-Javadoc)
	 * @see com.google.gson.JsonDeserializer#deserialize(com.google.gson.JsonElement, java.lang.reflect.Type, com.google.gson.JsonDeserializationContext)
	 */
	@Override
	public MetaDatas deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
			throws JsonParseException {

		MetaDatas metaDatas = new MetaDatas();
		for( JsonElement jElement: json.getAsJsonArray() ) {
			metaDatas.addMetaData(JsonUtils.fromJson(jElement.getAsJsonObject().toString(), MetaData.class));
		}
		return metaDatas;
	}
	
}
