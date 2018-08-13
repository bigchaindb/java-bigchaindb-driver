/*
 * Copyright BigchainDB GmbH and BigchainDB contributors
 * SPDX-License-Identifier: (Apache-2.0 AND CC-BY-4.0)
 * Code is Apache-2.0 and docs are CC-BY-4.0
 */
package com.bigchaindb.api;

import org.junit.Test;

import com.bigchaindb.api.ValidatorsApi;
import com.bigchaindb.model.Validators;

import java.io.IOException;

import static org.junit.Assert.assertTrue;


/**
 * The Class ValidatorsApiTest.
 */
public class ValidatorsApiTest extends AbstractApiTest {

    public static String V1_VALIDATORS_JSON = "[\n" +
            "    {\n" +
            "        \"pub_key\": {\n" +
            "               \"data\":\"4E2685D9016126864733225BE00F005515200727FBAB1312FC78C8B76831255A\",\n" +
            "               \"type\":\"ed25519\"\n" +
            "        },\n" +
            "        \"power\": 10\n" +
            "    },\n" +
            "    {\n" +
            "         \"pub_key\": {\n" +
            "               \"data\":\"608D839D7100466D6BA6BE79C320F8B81DE93CFAA58CF9768CF921C6371F2553\",\n" +
            "               \"type\":\"ed25519\"\n" +
            "         },\n" +
            "         \"power\": 5\n" +
            "    }\n" +
            "]";

    /**
     * Test get validators.
     */
    @Test
    public void testGetValidators() {
        try {
            Validators validators = ValidatorsApi.getValidators();
            assertTrue(validators.getValidators().size() == 2);
            assertTrue(validators.getValidators().get(0).getPower() == 10);
            assertTrue(validators.getValidators().get(0).getPubKey().getData().equals("4E2685D9016126864733225BE00F005515200727FBAB1312FC78C8B76831255A"));
            assertTrue(validators.getValidators().get(0).getPubKey().getType().equals("ed25519"));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
