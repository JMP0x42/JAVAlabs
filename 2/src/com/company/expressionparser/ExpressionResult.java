package com.company.expressionparser;

class ExpressionResult
{
    public double value;
    public String rest;

    public ExpressionResult(double value, String rest) {
        this.value = value;
        this.rest = rest;
    }
}
