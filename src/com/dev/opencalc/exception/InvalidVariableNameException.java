package com.dev.opencalc.exception;

/**
 * Calculation exception for an improper variable name.
 * @author John Riley
 *
 */
public class InvalidVariableNameException extends CalculationException {

	private static final long serialVersionUID = 1L;

	public InvalidVariableNameException() {
		super("Invalid Variable Name entered.");
	}

	public InvalidVariableNameException(String name) {
		super("Variable name: " + name + " is invalid.");
	}
}
