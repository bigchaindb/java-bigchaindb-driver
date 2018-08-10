/*
 * Copyright BigchainDB GmbH and BigchainDB contributors
 * SPDX-License-Identifier: (Apache-2.0 AND CC-BY-4.0)
 * Code is Apache-2.0 and docs are CC-BY-4.0
 */
package com.bigchaindb.model;

import java.util.ArrayList;
import java.util.List;



/**
 * The Class Outputs.
 */
public class Outputs {
	
	/** The output. */
	private List<Output> output = new ArrayList<Output>();

	/**
	 * Gets the output.
	 *
	 * @return the output
	 */
	public List<Output> getOutput() {
		return output;
	}

	/**
	 * Adds the output.
	 *
	 * @param output the output
	 */
	public void addOutput(Output output) {
		this.output.add(output);
	}
	
	
}
