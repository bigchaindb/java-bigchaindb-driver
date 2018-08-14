/*
 * Copyright BigchainDB GmbH and BigchainDB contributors
 * SPDX-License-Identifier: (Apache-2.0 AND CC-BY-4.0)
 * Code is Apache-2.0 and docs are CC-BY-4.0
 */
package com.bigchaindb.api;

import org.junit.Ignore;
import org.junit.Test;

import com.bigchaindb.api.BlocksApi;
import com.bigchaindb.model.Block;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertTrue;

/**
 * The Class BlocksApiTest.
 */
public class BlocksApiTest extends AbstractApiTest {

    public static String V1_BLOCK_JSON = "{\n" +
            "  \"height\": 1,\n" +
            "  \"transactions\": [\n" +
            "    {\n" +
            "      \"asset\": {\n" +
            "        \"data\": {\n" +
            "          \"msg\": \"Hello BigchainDB!\"\n" +
            "        }\n" +
            "      },\n" +
            "      \"id\": \"4957744b3ac54434b8270f2c854cc1040228c82ea4e72d66d2887a4d3e30b317\",\n" +
            "      \"inputs\": [\n" +
            "        {\n" +
            "          \"fulfillment\": \"pGSAIDE5i63cn4X8T8N1sZ2mGkJD5lNRnBM4PZgI_zvzbr-cgUCy4BR6gKaYT-tdyAGPPpknIqI4JYQQ-p2nCg3_9BfOI-15vzldhyz-j_LZVpqAlRmbTzKS-Q5gs7ZIFaZCA_UD\",\n" +
            "          \"fulfills\": null,\n" +
            "          \"owners_before\": [\n" +
            "            \"4K9sWUMFwTgaDGPfdynrbxWqWS6sWmKbZoTjxLtVUibD\"\n" +
            "          ]\n" +
            "        }\n" +
            "      ],\n" +
            "      \"metadata\": {\n" +
            "        \"sequence\": 0\n" +
            "      },\n" +
            "      \"operation\": \"CREATE\",\n" +
            "      \"outputs\": [\n" +
            "        {\n" +
            "          \"amount\": \"1\",\n" +
            "          \"condition\": {\n" +
            "            \"details\": {\n" +
            "              \"public_key\": \"4K9sWUMFwTgaDGPfdynrbxWqWS6sWmKbZoTjxLtVUibD\",\n" +
            "              \"type\": \"ed25519-sha-256\"\n" +
            "            },\n" +
            "            \"uri\": \"ni:///sha-256;PNYwdxaRaNw60N6LDFzOWO97b8tJeragczakL8PrAPc?fpt=ed25519-sha-256&cost=131072\"\n" +
            "          },\n" +
            "          \"public_keys\": [\n" +
            "            \"4K9sWUMFwTgaDGPfdynrbxWqWS6sWmKbZoTjxLtVUibD\"\n" +
            "          ]\n" +
            "        }\n" +
            "      ],\n" +
            "      \"version\": \"2.0\"\n" +
            "    }\n" +
            "  ]\n" +
            "}";

    public static String V1_BLOCK_BY_TRANS_JSON = "[\n" +
            "  1\n" +
            "]";


    /**
     * Test get block.
     */
    @Test @Ignore
    public void testGetBlock() {
        try {
            Block block = BlocksApi.getBlock("1");
            assertTrue(block.getHeight() == 1);
            assertTrue(block.getTransactions().size() == 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Test get block.
     */
    @Test
    public void testGetBlockByTransactionId() {
        try {
            List<String> list = BlocksApi.getBlocksByTransactionId("4957744b3ac54434b8270f2c854cc1040228c82ea4e72d66d2887a4d3e30b317");
            assertTrue(list.size() == 1);
            assertTrue(list.get(0).equals("1"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
