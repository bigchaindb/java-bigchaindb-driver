/*
 * Copyright BigchainDB GmbH and BigchainDB contributors
 * SPDX-License-Identifier: (Apache-2.0 AND CC-BY-4.0)
 * Code is Apache-2.0 and docs are CC-BY-4.0
 */
package com.bigchaindb.util;

import com.google.api.client.util.Base64;
import net.i2p.crypto.eddsa.EdDSAPrivateKey;
import net.i2p.crypto.eddsa.EdDSAPublicKey;
import net.i2p.crypto.eddsa.spec.EdDSANamedCurveTable;
import net.i2p.crypto.eddsa.spec.EdDSAParameterSpec;
import net.i2p.crypto.eddsa.spec.EdDSAPrivateKeySpec;
import net.i2p.crypto.eddsa.spec.EdDSAPublicKeySpec;

import java.security.KeyPair;
import java.util.Arrays;

public class KeyPairUtils {

    public static KeyPair generateNewKeyPair() {
        net.i2p.crypto.eddsa.KeyPairGenerator edDsaKpg
                = new net.i2p.crypto.eddsa.KeyPairGenerator();
        return edDsaKpg.generateKeyPair();
    }

    /**
     * Encodes the public key to base58.
     *
     * @param publicKey the public key
     * @return the string
     */
    public static String encodePublicKeyInBase58(EdDSAPublicKey publicKey) {
        return Base58.encode(Arrays.copyOfRange(publicKey.getEncoded(), 12, 44));
    }

    public static byte[] encodePrivateKey(KeyPair keyPair) {
        return keyPair.getPrivate().getEncoded();
    }

    public static String encodePrivateKeyBase64(KeyPair keyPair) {
        return Base64.encodeBase64String(encodePrivateKey(keyPair));
    }

    public static KeyPair decodeKeyPair(byte[] encodedPrivateKey) {
        EdDSAParameterSpec keySpecs = EdDSANamedCurveTable.getByName("Ed25519");
        byte[] seed = Arrays.copyOfRange(encodedPrivateKey, 16, 48);
        EdDSAPrivateKeySpec privKeySpec = new EdDSAPrivateKeySpec(seed, keySpecs);
        EdDSAPublicKeySpec pubKeySpec = new EdDSAPublicKeySpec(privKeySpec.getA(), keySpecs);
        return new KeyPair(new EdDSAPublicKey(pubKeySpec), new EdDSAPrivateKey(privKeySpec));
    }

    public static KeyPair decodeKeyPair(String encodedPrivateKeyBase64) {
        return decodeKeyPair(Base64.decodeBase64(encodedPrivateKeyBase64));
    }
}
