/*
 * Copyright BigchainDB GmbH and BigchainDB contributors
 * SPDX-License-Identifier: (Apache-2.0 AND CC-BY-4.0)
 * Code is Apache-2.0 and docs are CC-BY-4.0
 */
package com.bigchaindb.json.strategy;

import com.bigchaindb.model.Validator;
import com.bigchaindb.model.Validators;
import com.bigchaindb.util.JsonUtils;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.Iterator;



/**
 * The Class ValidatorDeserializer.
 */
public class ValidatorDeserializer implements JsonDeserializer<Validators> {

	/* (non-Javadoc)
	 * @see com.google.gson.JsonDeserializer#deserialize(com.google.gson.JsonElement, java.lang.reflect.Type, com.google.gson.JsonDeserializationContext)
	 */
	@Override
	public Validators deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
			throws JsonParseException {
		
		Validators validators = new Validators();
		Iterator<JsonElement> jsonIter = json.getAsJsonArray().iterator();
		while(jsonIter.hasNext()) {
			JsonElement jElement = jsonIter.next();
			validators.addValidator(JsonUtils.fromJson(jElement.getAsJsonObject().toString(), Validator.class));
		}
		return validators;
	}
	
}
