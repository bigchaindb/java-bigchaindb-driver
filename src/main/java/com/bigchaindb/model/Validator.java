/*
 * Copyright BigchainDB GmbH and BigchainDB contributors
 * SPDX-License-Identifier: (Apache-2.0 AND CC-BY-4.0)
 * Code is Apache-2.0 and docs are CC-BY-4.0
 */
package com.bigchaindb.model;

import com.google.gson.annotations.SerializedName;


/**
 * The Class Validator.
 */
public class Validator {

    /**
     * The public key.
     */
    @SerializedName("pub_key")
    private ValidatorPubKey pubKey;

    /**
     * The signature.
     */
    @SerializedName("power")
    private Integer power;

    /**
     * Gets the pubKey.
     *
     * @return the pubKey
     */
    public ValidatorPubKey getPubKey() {
        return pubKey;
    }

    /**
     * Gets the power.
     *
     * @return the power
     */
    public Integer getPower() {
        return power;
    }

}
