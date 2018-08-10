/*
 * Copyright BigchainDB GmbH and BigchainDB contributors
 * SPDX-License-Identifier: (Apache-2.0 AND CC-BY-4.0)
 * Code is Apache-2.0 and docs are CC-BY-4.0
 */
package com.bigchaindb.api;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.security.KeyPair;
import java.util.Map;
import java.util.TreeMap;

import com.bigchaindb.api.AssetsApi;
import com.bigchaindb.builders.BigchainDbTransactionBuilder;
import com.bigchaindb.model.Assets;
import com.bigchaindb.model.Transaction;

import net.i2p.crypto.eddsa.EdDSAPrivateKey;
import net.i2p.crypto.eddsa.EdDSAPublicKey;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * The Class AssetsApiTest.
 */
public class AssetsApiTest extends AbstractApiTest {

    public static String V1_ASSET_JSON = "[\n" +
            "    {\n" +
            "        \"data\": {\"msg\": \"Hello BigchainDB 1!\"},\n" +
            "        \"id\": \"51ce82a14ca274d43e4992bbce41f6fdeb755f846e48e710a3bbb3b0cf8e4204\"\n" +
            "    },\n" +
            "    {\n" +
            "        \"data\": {\"msg\": \"Hello BigchainDB 2!\"},\n" +
            "        \"id\": \"b4e9005fa494d20e503d916fa87b74fe61c079afccd6e084260674159795ee31\"\n" +
            "    },\n" +
            "    {\n" +
            "        \"data\": {\"msg\": \"Hello BigchainDB 3!\"},\n" +
            "        \"id\": \"fa6bcb6a8fdea3dc2a860fcdc0e0c63c9cf5b25da8b02a4db4fb6a2d36d27791\"\n" +
            "    }\n" +
            "]";

    public static String V1_ASSET_LIMIT_JSON = "[\n" +
            "    {\n" +
            "        \"data\": {\"msg\": \"Hello BigchainDB 1!\"},\n" +
            "        \"id\": \"51ce82a14ca274d43e4992bbce41f6fdeb755f846e48e710a3bbb3b0cf8e4204\"\n" +
            "    },\n" +
            "    {\n" +
            "        \"data\": {\"msg\": \"Hello BigchainDB 2!\"},\n" +
            "        \"id\": \"b4e9005fa494d20e503d916fa87b74fe61c079afccd6e084260674159795ee31\"\n" +
            "    }\n" +
            "]";

    /**
     * Test asset search.
     */
    @Test
    public void testAssetSearch() {
        try {
            Assets assets = AssetsApi.getAssets("bigchaindb");
            assertTrue(assets.size() == 3);
            assertTrue(assets.getAssets().get(0).getId().equals("51ce82a14ca274d43e4992bbce41f6fdeb755f846e48e710a3bbb3b0cf8e4204"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Test asset search with limit.
     */
    @Test
    public void testAssetSearchWithLimit() {
        try {
            Assets assets = AssetsApi.getAssetsWithLimit("bigchaindb", "2");
            assertTrue(assets.size() == 2);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
