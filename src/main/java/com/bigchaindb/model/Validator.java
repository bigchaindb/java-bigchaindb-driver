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
