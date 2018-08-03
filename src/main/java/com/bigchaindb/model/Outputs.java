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
