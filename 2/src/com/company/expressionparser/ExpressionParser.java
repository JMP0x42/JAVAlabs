package com.company.expressionparser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

/**
 * Class that allows you to parse mathematics expressions like "2*(1+2/4*sqrt(4))"
 */
public class ExpressionParser {
    private final HashMap<String, Double> vars;
    private final ArrayList<IFunctionExpression> customFunctions;

    public ExpressionParser() {
        vars = new HashMap<>();
        customFunctions = new ArrayList<>();
        customFunctions.add(new SqrtFunction());
    }

    /**
     * Add a custom function to parser
     * @param func custom function
     */
    public void addCustomFunction(IFunctionExpression func) {
        customFunctions.add(func);
    }

    /**
     * Getting user variable from variable store (hashMap)
     * @param variableName name of variable
     * @return variable value
     */
    private Double getVariable(String variableName) {
        if (!vars.containsKey(variableName)) {
            Scanner in = new Scanner(System.in);
            System.out.println("Enter a value of variable: " + variableName);
            var varValue = in.nextDouble();
            vars.put(variableName, varValue);
        }

        return vars.get(variableName);
    }

    /**
     * parse expression
     * @param s expression
     * @return value of parsed expression
     * @throws IllegalArgumentException if expression invalid
     */
    public double parse(String s) throws IllegalArgumentException {
        ExpressionResult result = processPlusMinus(s);
        if (!result.rest.isEmpty()) {
            throw new IllegalArgumentException("Invalid expression " + s + ", supposed problem symbol at position " + (s.length() - result.rest.length() + 1));
        }
        return result.value;
    }

    /**
     * proceed plus and minus operation
     * @param s expression
     * @return result of parsing (calculated value + rest of expression)
     * @throws IllegalArgumentException if expression broken
     */
    private ExpressionResult processPlusMinus(String s) throws IllegalArgumentException {
        ExpressionResult current = processMulDiv(s);
        double leftValue = current.value;

        while (current.rest.length() > 0) {
            if (!(current.rest.charAt(0) == '+' || current.rest.charAt(0) == '-')) break;

            char operation = current.rest.charAt(0);
            String next = current.rest.substring(1);

            current = processMulDiv(next);
            if (operation == '+') {
                leftValue += current.value;
            } else {
                leftValue -= current.value;
            }
        }
        return new ExpressionResult(leftValue, current.rest);
    }

    /**
     * proceed operations in the brackets
     * @param s expression
     * @return result of parsing (calculated value + rest of expression)
     * @throws IllegalArgumentException if expression broken
     */
    private ExpressionResult processBracket(String s) throws IllegalArgumentException {
        char zeroChar = s.charAt(0);
        if (zeroChar == '(') {
            ExpressionResult r = processPlusMinus(s.substring(1));
            if (!r.rest.isEmpty() && r.rest.charAt(0) == ')') {
                r.rest = r.rest.substring(1);
            } else {
                throw new IllegalArgumentException("Invalid bracket expression");
            }
            return r;
        }
        return processFunctionOrVariable(s);
    }

    /**
     * calculating value of function or user variable
     * @param s expression
     * @return result of parsing (calculated value + rest of expression)
     * @throws IllegalArgumentException if expression broken
     */
    private ExpressionResult processFunctionOrVariable(String s) throws IllegalArgumentException {
        StringBuilder f = new StringBuilder();
        int i = 0;

        while (i < s.length() && (Character.isLetter(s.charAt(i)) || ( Character.isDigit(s.charAt(i)) && i > 0 ) )) {
            f.append(s.charAt(i));
            i++;
        }
        if (f.length() > 0) {
            if (s.length() > i && s.charAt( i ) == '(') { // start of func arg
                ExpressionResult r = processBracket(s.substring(f.length()));
                return processFunction(f.toString(), r);
            } else { // user variable
                return new ExpressionResult(getVariable(f.toString()), s.substring(f.length()));
            }
        }
        return toNumber(s);
    }

    /**
     * proceed multiplication or dividing operations
     * @param s expression
     * @return result of parsing (calculated value + rest of expression)
     * @throws IllegalArgumentException if expression broken
     */
    private ExpressionResult processMulDiv(String s) throws IllegalArgumentException {
        ExpressionResult current = processBracket(s);

        double leftValue = current.value;
        while (true) {
            if (current.rest.length() == 0) {
                return current;
            }
            char operation = current.rest.charAt(0);
            if ((operation != '*' && operation != '/')) return current;

            String next = current.rest.substring(1);
            ExpressionResult right = processBracket(next);

            if (operation == '*') {
                leftValue *= right.value;
            } else {
                leftValue /= right.value;
            }

            current = new ExpressionResult(leftValue, right.rest);
        }
    }

    /**
     * proceed multiplication or dividing operations
     * @param s expression
     * @return result of parsing (calculated value + rest of expression)
     * @throws IllegalArgumentException if expression broken
     */
   private ExpressionResult toNumber(String s) throws IllegalArgumentException {
        int i = 0;
        int dots = 0;
        boolean negative = false;

        if( s.charAt(0) == '-' ) {
            negative = true;
            s = s.substring(1);
        }

        while (i < s.length() && (Character.isDigit(s.charAt(i)) || s.charAt(i) == '.')) {
            if (s.charAt(i) == '.' && ++dots > 1) {
                throw new IllegalArgumentException("Invalid number " + s.substring(0, i + 1));
            }
            i++;
        }

        if (i == 0) {
            throw new IllegalArgumentException("Invalid number " + s);
        }

        double dPart = Double.parseDouble(s.substring(0, i));
        if (negative)
            dPart = -dPart;
        String restPart = s.substring(i);

        return new ExpressionResult(dPart, restPart);
    }

    /**
     * Gets value from user function
     * @param funcMetaname name of function
     * @param r value which to be sent to function
     * @return value of function
     * @throws IllegalArgumentException if function with specific name does not exist
     */
    private ExpressionResult processFunction(String funcMetaname, ExpressionResult r) throws IllegalArgumentException {
        var func = this.customFunctions.stream().filter(f -> funcMetaname.equals(f.getMetaname())).findFirst().orElse(null);

        if (func == null) {
            throw new IllegalArgumentException("Invalid function name " + funcMetaname);
        }

        return new ExpressionResult(func.calculate(r.value), r.rest);
    }
} 