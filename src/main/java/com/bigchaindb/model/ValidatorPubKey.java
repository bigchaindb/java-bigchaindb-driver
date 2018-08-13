/*
 * Copyright BigchainDB GmbH and BigchainDB contributors
 * SPDX-License-Identifier: (Apache-2.0 AND CC-BY-4.0)
 * Code is Apache-2.0 and docs are CC-BY-4.0
 */
package com.bigchaindb.model;

import com.google.gson.annotations.SerializedName;


/**
 * The Class ValidatorPubKey.
 */
public class ValidatorPubKey {

    /**
     * The data.
     */
    @SerializedName("data")
    private String data;

    /**
     * The type.
     */
    @SerializedName("type")
    private String type;

    /**
     * Gets the data.
     *
     * @return the data
     */
    public String getData() {
        return data;
    }

    /**
     * Gets the type.
     *
     * @return the type
     */
    public String getType() {
        return type;
    }

}
