package com.bigchaindb.model;

import java.util.ArrayList;
import java.util.List;


/**
 * The Class Validators.
 */
public class Validators {

    /**
     * The validators.
     */
    private List<Validator> validators = new ArrayList<Validator>();

    /**
     * Gets the validators.
     *
     * @return the validators
     */
    public List<Validator> getValidators() {
        return validators;
    }

    /**
     * Adds the validator.
     *
     * @param validator the validator
     */
    public void addValidator(Validator validator) {
        this.validators.add(validator);
    }
}
