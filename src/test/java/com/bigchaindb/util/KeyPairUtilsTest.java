/*
 * Copyright BigchainDB GmbH and BigchainDB contributors
 * SPDX-License-Identifier: (Apache-2.0 AND CC-BY-4.0)
 * Code is Apache-2.0 and docs are CC-BY-4.0
 */
package com.bigchaindb.util;


import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.bigchaindb.util.KeyPairUtils;

import java.security.KeyPair;

public class KeyPairUtilsTest {

    private KeyPair generatedKeyPair;

    /**
     * Inits the.
     */
    @Before
    public void init() {
        generatedKeyPair = KeyPairUtils.generateNewKeyPair();
    }

    @Test
    public void testBytesEncoding() {
        byte[] encodedKey = KeyPairUtils.encodePrivateKey(generatedKeyPair);
        KeyPair decodedKeyPair = KeyPairUtils.decodeKeyPair(encodedKey);
        Assert.assertArrayEquals(generatedKeyPair.getPrivate().getEncoded(),
                decodedKeyPair.getPrivate().getEncoded());
    }

    @Test
    public void testBase64Encoding() {
        String encodedKey = KeyPairUtils.encodePrivateKeyBase64(generatedKeyPair);
        KeyPair decodedKeyPair = KeyPairUtils.decodeKeyPair(encodedKey);
        Assert.assertArrayEquals(generatedKeyPair.getPrivate().getEncoded(),
                decodedKeyPair.getPrivate().getEncoded());
    }
}
