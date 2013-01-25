/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2;

import fit.ColumnFixture;

public class Cai2FrontControllerFixture extends ColumnFixture {

	// the "setters" are public fields
	public double left;

	public double right;

	public char operator;

	public Cai2FrontControllerFixture() {
	}

	// the "getters" are no-argument methods that return something
	public double result() {
		Calculator calculator = Calculator.getInstance();
		switch (operator) {
		case '+':
			return calculator.add(left, right);
		case '-':
			return calculator.subtract(left, right);
		case '*':
			return calculator.multiply(left, right);
		case '/':
			return calculator.divide(left, right);
		default:
			throw new IllegalArgumentException("Unknown operator: " + operator);
		}
	}
}
