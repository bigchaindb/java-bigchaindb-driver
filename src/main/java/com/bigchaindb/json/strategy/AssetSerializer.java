/*
 * Copyright BigchainDB GmbH and BigchainDB contributors
 * SPDX-License-Identifier: (Apache-2.0 AND CC-BY-4.0)
 * Code is Apache-2.0 and docs are CC-BY-4.0
 */
package com.bigchaindb.json.strategy;

import com.bigchaindb.model.Asset;
import com.bigchaindb.util.JsonUtils;
import com.google.gson.*;

import java.lang.reflect.Type;

public class AssetSerializer implements JsonSerializer<Asset>
{
	/**
	 *  Serialize an asset object to json object
	 *  Note: given the type of the asset.data it maybe necessary to
	 *  to add a type adapter {@link JsonSerializer} and/or {@link JsonDeserializer} with {@link JsonUtils} and
	 *  {@link com.bigchaindb.util.JsonUtils#addTypeAdapterSerializer}
	 *
	 *  TODO test user.data with custom serializer
	 *
	 * @param src object to serialize
	 * @param typeOfSrc type of src
	 * @param context the json context
	 * @return the json object
	 */
	public JsonElement serialize( Asset src, Type typeOfSrc, JsonSerializationContext context )
	{
		Gson gson = JsonUtils.getGson();
		JsonObject asset = new JsonObject();

		if (src.getData() != null) {
			asset.add( "data", gson.toJsonTree( src.getData(), src.getDataClass() ) );
		} else {
			asset.add("id", gson.toJsonTree( src.getId() ));
		}
		
		return asset;
	}
}
