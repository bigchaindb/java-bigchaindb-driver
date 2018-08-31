/*
 * Copyright BigchainDB GmbH and BigchainDB contributors
 * SPDX-License-Identifier: (Apache-2.0 AND CC-BY-4.0)
 * Code is Apache-2.0 and docs are CC-BY-4.0
 */
package com.bigchaindb.util;

import com.bigchaindb.json.factory.GsonEmptyCheckTypeAdapterFactory;
import com.bigchaindb.json.strategy.*;
import com.bigchaindb.model.*;
import com.google.gson.*;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

/**
 * Utility class for handling JSON serialization and deserialization.
 */
public class JsonUtils {

    /**
     * The gson.
     */
    private static String jsonDateFormat = "yyyy-MM-dd'T'HH:mm:ssZZZZZ";

    private static Map<String, TypeAdapter> typeAdaptersDeserialize = new ConcurrentHashMap<String, TypeAdapter>(16) {{
        put(Transaction.class.getCanonicalName(), new TypeAdapter(Transaction.class, new TransactionDeserializer()));
        put(Transactions.class.getCanonicalName(), new TypeAdapter(Transactions.class, new TransactionsDeserializer()));
        put(Assets.class.getCanonicalName(), new TypeAdapter(Assets.class, new AssetsDeserializer()));
        put(MetaDatas.class.getCanonicalName(), new TypeAdapter(MetaDatas.class, new MetaDataDeserializer()));
        put(Outputs.class.getCanonicalName(), new TypeAdapter(Outputs.class, new OutputsDeserializer()));
        put(Validators.class.getCanonicalName(), new TypeAdapter(Validators.class, new ValidatorDeserializer()));
    }};

    private static Map<String, TypeAdapter> typeAdaptersSerialize = new ConcurrentHashMap<String, TypeAdapter>(16) {{
        put(Asset.class.getCanonicalName(), new TypeAdapter(Asset.class, new AssetSerializer()));
        put(Input.class.getCanonicalName(), new TypeAdapter(Input.class, new InputSerializer()));
        put(MetaData.class.getCanonicalName(), new TypeAdapter(MetaData.class, new MetaDataSerializer()));
    }};

    private static synchronized GsonBuilder base() {
        GsonBuilder builder = new GsonBuilder();

        builder = builder
                .serializeNulls()
                .disableHtmlEscaping()
                .setDateFormat(jsonDateFormat)
                .registerTypeAdapterFactory(new GsonEmptyCheckTypeAdapterFactory())
                .addSerializationExclusionStrategy(new CustomExclusionStrategy());

        return builder;
    }

    /**
     * returns gson
     * @return
     */
    public static Gson getGson() {
        GsonBuilder builder = base();

        for (TypeAdapter adapter : typeAdaptersDeserialize.values()) {
            builder.registerTypeAdapter(adapter.getType(), adapter.getSerializer());
        }
        for (TypeAdapter adapter : typeAdaptersSerialize.values()) {
            builder.registerTypeAdapter(adapter.getType(), adapter.getSerializer());
        }

        return builder.create();
    }

    /**
     * returns gson
     * @param exclusionStrategies strategy to use
     * @return gson
     */
    public static Gson getGson(ExclusionStrategy... exclusionStrategies) {
        return getGson(null, exclusionStrategies);
    }

    /**
     * returns gson
     * @param ignoreClass class to ignore
     * @param exclusionStrategies strategy to use
     * @return gson
     */
    public static Gson getGson(Class ignoreClass, ExclusionStrategy... exclusionStrategies) {
        GsonBuilder builder = base();

        for (TypeAdapter adapter : typeAdaptersDeserialize.values()) {
            if (!adapter.getType().equals(ignoreClass))
                builder.registerTypeAdapter(adapter.getType(), adapter.getSerializer());
        }
        for (TypeAdapter adapter : typeAdaptersSerialize.values()) {
            if (!adapter.getType().equals(ignoreClass))
                builder.registerTypeAdapter(adapter.getType(), adapter.getSerializer());
        }

        return builder.setExclusionStrategies(exclusionStrategies).create();
    }

    /**
     * Add a type adapter
     *
     * @param type             the type (@Class) we're adapting
     * @param jsonDeserializer the type's deserializer
     */
    public static void addTypeAdapterDeserializer(Class type, JsonDeserializer<?> jsonDeserializer) {
        typeAdaptersDeserialize.put(type.getCanonicalName(), new TypeAdapter(type, jsonDeserializer));
    }

    /**
     * Add a type adapter
     *
     * @param type           the type (@Class) we're adapting
     * @param jsonSerializer the type's deserializer
     */
    public static void addTypeAdapterSerializer(Class type, JsonSerializer<?> jsonSerializer) {
        typeAdaptersSerialize.put(type.getCanonicalName(), new TypeAdapter(type, jsonSerializer));
    }

    /**
     * From json.
     *
     * @param <T>  the generic type
     * @param json the string from which the object is to be deserialized.
     * @param T    the type of the desired object.
     * @return an object of type T from the string. Returns null if json is
     * null.
     * @see Gson#fromJson(String, Class)
     */
    public static <T> T fromJson(String json, Class<T> T) {
        return getGson().fromJson(json, T);
    }

    /**
     * To json.
     *
     * @param src the object for which Json representation is to be created
     *            setting for Gson .
     * @return Json representation of src.
     * @see Gson#toJson(Object)
     */
    public static String toJson(Object src) {
        return getGson().toJson(src);
    }

    /**
     * To json.
     * @param src the object for which Json representation is to be created
     *            setting for Gson .
     * @return Json representation of src.
     * @see Gson#toJson(Object)
     */
    public static String toJson(Object src, ExclusionStrategy... exclusionStrategies) {
        return getGson(exclusionStrategies).toJson(src);
    }

    public static void setJsonDateFormat( final String dateFormat ) {
        jsonDateFormat = dateFormat;
    }
}
