package com.company.expressionparser;

public class f implements IFunctionExpression {

    @Override
    public String getMetaname() {
        return "f";
    }

    @Override
    public double calculate(double value) {
        return 2 * value;
    }
}
