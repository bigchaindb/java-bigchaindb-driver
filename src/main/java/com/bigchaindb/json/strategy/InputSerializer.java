package com.bigchaindb.json.strategy;

import com.bigchaindb.model.Input;
import com.bigchaindb.util.JsonUtils;
import com.google.gson.*;

import java.lang.reflect.Type;


/**
 * Serialize an input record.
 */
public class InputSerializer implements JsonSerializer<Input>
{
  /**
   *  Serialize an input object.
   *
   * @param src object to serialize
   * @param typeOfSrc type of src
   * @param context the json context
   * @return the json object
   */
  public JsonElement serialize(Input src, Type typeOfSrc, JsonSerializationContext context )
  {
    Gson gson = JsonUtils.getGson();
    JsonObject asset = new JsonObject();


    if (src.getFullFillment() != null) {
      asset.add( "fulfillment", gson.toJsonTree( src.getFullFillment() ) );
    } else {
      asset.add( "fulfillment", gson.toJsonTree( src.getFullFillmentDetails() ) );
    }
    asset.add( "fulfills", gson.toJsonTree( src.getFulFills() ) );
    asset.add( "owners_before", gson.toJsonTree( src.getOwnersBefore() ) );

    return asset;
  }
}