/*
 * Copyright BigchainDB GmbH and BigchainDB contributors
 * SPDX-License-Identifier: (Apache-2.0 AND CC-BY-4.0)
 * Code is Apache-2.0 and docs are CC-BY-4.0
 */
package com.bigchaindb.api;

import org.junit.Test;

import com.bigchaindb.api.MetaDataApi;
import com.bigchaindb.model.Assets;
import com.bigchaindb.model.MetaDatas;

import java.io.IOException;

import static org.junit.Assert.assertTrue;

/**
 * The Class AssetsApiTest.
 */
public class MetaDataApiTest extends AbstractApiTest {

    public static String V1_METADATA_JSON = "[\n" +
            "    {\n" +
            "        \"metadata\": {\"metakey1\": \"Hello BigchainDB 1!\"},\n" +
            "        \"id\": \"51ce82a14ca274d43e4992bbce41f6fdeb755f846e48e710a3bbb3b0cf8e4204\"\n" +
            "    },\n" +
            "    {\n" +
            "        \"metadata\": {\"metakey2\": \"Hello BigchainDB 2!\"},\n" +
            "        \"id\": \"b4e9005fa494d20e503d916fa87b74fe61c079afccd6e084260674159795ee31\"\n" +
            "    },\n" +
            "    {\n" +
            "        \"metadata\": {\"metakey3\": \"Hello BigchainDB 3!\"},\n" +
            "        \"id\": \"fa6bcb6a8fdea3dc2a860fcdc0e0c63c9cf5b25da8b02a4db4fb6a2d36d27791\"\n" +
            "    }\n" +
            "]\n";

    public static String V1_METADATA_LIMIT_JSON = "[\n" +
            "    {\n" +
            "        \"metadata\": {\"msg\": \"Hello BigchainDB 1!\"},\n" +
            "        \"id\": \"51ce82a14ca274d43e4992bbce41f6fdeb755f846e48e710a3bbb3b0cf8e4204\"\n" +
            "    },\n" +
            "    {\n" +
            "        \"metadata\": {\"msg\": \"Hello BigchainDB 2!\"},\n" +
            "        \"id\": \"b4e9005fa494d20e503d916fa87b74fe61c079afccd6e084260674159795ee31\"\n" +
            "    }\n" +
            "]";

    /**
     * Test metadata search.
     */
    @Test
    public void testMetaDataSearch() {
        try {
            MetaDatas metadata = MetaDataApi.getMetaData("bigchaindb");
            assertTrue(metadata.size() == 3);
            assertTrue(metadata.getMetaDatas().get(0).getId().equals("51ce82a14ca274d43e4992bbce41f6fdeb755f846e48e710a3bbb3b0cf8e4204"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Test metadata search with limit.
     */
    @Test
    public void testMetaDataSearchWithLimit() {
        try {
            MetaDatas metadata = MetaDataApi.getMetaDataWithLimit("bigchaindb", "2");
            assertTrue(metadata.size() == 2);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
