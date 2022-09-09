package com.company;

import com.company.expressionparser.ExpressionParser;
import com.company.expressionparser.f;

public class Main {
    public static void main(String[] args) {
        final String exp = "3*(2+f(2))";
        var func = new f();
        var parser = new ExpressionParser();
        parser.addCustomFunction(func);
        // свой multiset (set с дубликатами)
        try {
            System.out.println(parser.parse(exp));
        } catch (Exception e) {
            System.err.println("exception: " + e.getMessage());
        }
    }
}
