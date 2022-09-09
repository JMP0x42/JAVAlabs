package com.company.expressionparser;

/**
 * Interface for custom function for ExpressionParser
 */
public interface IFunctionExpression {
	String getMetaname();
	double calculate(double value);
}
