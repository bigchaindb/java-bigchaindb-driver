/*
 * Copyright BigchainDB GmbH and BigchainDB contributors
 * SPDX-License-Identifier: (Apache-2.0 AND CC-BY-4.0)
 * Code is Apache-2.0 and docs are CC-BY-4.0
 */
package com.bigchaindb.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.bouncycastle.jcajce.provider.digest.SHA3;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The Class DriverUtils.
 */
public class DriverUtils {

    /**
     * The Constant DIGITS.
     */
    private static final char[] DIGITS =
            {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    /**
     * Gets the hex.
     *
     * @param data the data
     * @return the hex
     */
    public static String getHex(byte[] data) {
        final int l = data.length;
        final char[] outData = new char[l << 1];
        for (int i = 0, j = 0; i < l; i++) {
            outData[j++] = DIGITS[(0xF0 & data[i]) >>> 4];
            outData[j++] = DIGITS[0x0F & data[i]];
        }

        return new String(outData);
    }

    /**
     * To conform with BigchainDB serialization
     *
     * @param input the json string to sort the properties for
     * @return the json object
     */
    public static JsonObject makeSelfSortingGson(String input) {
        if (input == null) return null;

        JsonParser jsonParser = new JsonParser();
        return makeSelfSortingGson(jsonParser.parse(input).getAsJsonObject());
    }

    /**
     * Make self sorting.
     *
     * @param input the input
     * @return the JSON object
     */
    /*
    We are using a hack to make stardard org.json be automatically sorted
    by key desc alphabetically
     */
    public static JsonObject makeSelfSortingGson(JsonObject input) {
        if (input == null) return null;

        JsonObject json = new JsonObject();

        List<String> keys = new ArrayList<>(input.keySet());
        Collections.sort(keys);
        for (String key : keys) {
            JsonElement j = input.get(key);
            if (j instanceof JsonObject) {
                json.add(key, makeSelfSortingGson((JsonObject) j));
            } else if (j instanceof JsonArray) {
                JsonArray h = (JsonArray) j;
                JsonArray oList = new JsonArray();
                for (int i = 0; i < h.size(); i++) {
                    JsonElement joi = h.get(i);
                    if (joi instanceof JsonObject) {
                        oList.add(makeSelfSortingGson((JsonObject) joi));
                        json.add(key, oList);
                    } else {
                        oList.add(joi);
                        json.add(key, oList);
                    }
                }
            } else {
                json.add(key, j);
            }
        }

        return json;
    }

    public static byte[] getSha3HashRaw(byte[] input) {
        SHA3.DigestSHA3 md = new SHA3.DigestSHA3(256); //same as DigestSHA3 md = new SHA3.Digest256();
        md.update(input);
        return md.digest();
    }

    public static String getSha3HashHex(byte[] input) {
        SHA3.DigestSHA3 md = new SHA3.DigestSHA3(256); //same as DigestSHA3 md = new SHA3.Digest256();
        md.update(input);
        String id = getHex(md.digest());
        return id;
    }
}
