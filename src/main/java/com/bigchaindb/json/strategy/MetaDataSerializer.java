/*
 * Copyright BigchainDB GmbH and BigchainDB contributors
 * SPDX-License-Identifier: (Apache-2.0 AND CC-BY-4.0)
 * Code is Apache-2.0 and docs are CC-BY-4.0
 */
package com.bigchaindb.json.strategy;

import com.bigchaindb.model.MetaData;
import com.bigchaindb.util.JsonUtils;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Map;

public class MetaDataSerializer implements JsonSerializer<MetaData>
{
	/**
	 *
	 * @param src object to serialize
	 * @param typeOfSrc type of src
	 * @param context the json context
	 * @return the json object
	 */
	public JsonElement serialize( MetaData src, Type typeOfSrc, JsonSerializationContext context )
	{
		Gson gson = JsonUtils.getGson();
		JsonElement metadata = gson.toJsonTree( src.getMetadata(), new TypeToken<Map<String, String>>() { }.getType() );
		return metadata;
	}
}
