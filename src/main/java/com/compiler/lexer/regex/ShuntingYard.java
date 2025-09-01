package com.compiler.lexer.regex;

import java.util.Stack;
/**
 * Utility class for regular expression parsing using the Shunting Yard
 * algorithm.
 * <p>
 * Provides methods to preprocess regular expressions by inserting explicit
 * concatenation operators, and to convert infix regular expressions to postfix
 * notation for easier parsing and NFA construction.
 */
/**
 * Utility class for regular expression parsing using the Shunting Yard
 * algorithm.
 */
public class ShuntingYard {

    /**
     * Default constructor for ShuntingYard.
     */
    public ShuntingYard() {
        // TODO: Implement constructor if needed
    }

    /**
     * Inserts the explicit concatenation operator ('·') into the regular
     * expression according to standard rules. This makes implicit
     * concatenations explicit, simplifying later parsing.
     *
     * @param regex Input regular expression (may have implicit concatenation).
     * @return Regular expression with explicit concatenation operators.
     */
    public static String insertConcatenationOperator(String regex) {
        // TODO: Implement insertConcatenationOperator
        /*
            Pseudocode:
            For each character in regex:
                - Append current character to output
                - If not at end of string:
                        - Check if current and next character form an implicit concatenation
                        - If so, append '·' to output
            Return output as string
         */
        StringBuilder output = new StringBuilder();
        char[] arreglo = regex.toCharArray();
        for (int i = 0; i < arreglo.length; i++) {
            char c = regex.charAt(i);
            output.append(c);
            if (i+1 < arreglo.length) {
                char next = arreglo[i+1];
                char current = arreglo [i];
                if (concImplicita(current, next)) {
                    output.append('·');
                }
            }
        }
        return output.toString();
        // throw new UnsupportedOperationException("Not implemented");
    }
    /**
     * Método auxiliar para determinar si existe una concatenación implícita entre dos caracteres
     * @param current Caracter c1
     * @param next Caracter c2
     * @return true o false, dependiendo de si es o no concatenación implícita
     */

    public static boolean concImplicita (char current, char next) {
        return (isOperand(current) && isOperand(next))    // regla 1
        || (isOperand(current) && next == '(')        // regla 2
        || (current == ')' && isOperand(next))        // regla 3
        || (esUnario(current) && isOperand(next))      // regla 4
        || (current == ')' && next == '(');          // regla 5
    }

    /**
     * Método auxiliar que determina si un caracter es o no unario
     * @param c Caracter
     * @return true o false, dependiendo de si es o no unario
     */
    private static boolean esUnario(char c) {
        return c == '*' || c == '+' || c == '?';
    }

    /**
     * Determines if the given character is an operand (not an operator or
     * parenthesis).
     *
     * @param c Character to evaluate.
     * @return true if it is an operand, false otherwise.
     */
    private static boolean isOperand(char c) {
        // TODO: Implement isOperand
        /*
        Pseudocode:
        Return true if c is not one of: '|', '*', '?', '+', '(', ')', '·'
         */
        switch (c) {
            case '|':
            case '*':
            case '?':
            case '+':
            case '(':
            case ')':
            case '·': 
                return false;
            default:
                return true;
        }
        // throw new UnsupportedOperationException("Not implemented");
    }

    /**
     * Converts an infix regular expression to postfix notation using the
     * Shunting Yard algorithm. This is useful for constructing NFAs from
     * regular expressions.
     *
     * @param infixRegex Regular expression in infix notation.
     * @return Regular expression in postfix notation.
     */
    public static String toPostfix(String infixRegex) {
        // TODO: Implement toPostfix
        /*
        Pseudocode:
        1. Define operator precedence map
        2. Preprocess regex to insert explicit concatenation operators
        3. For each character in regex:
            - If operand: append to output
            - If '(': push to stack
            - If ')': pop operators to output until '(' is found
            - If operator: pop operators with higher/equal precedence, then push current operator
        4. After loop, pop remaining operators to output
        5. Return output as string
         */
        
        String regex = insertConcatenationOperator(infixRegex);

        StringBuilder output = new StringBuilder();
        Stack<Character> stack = new Stack<>();

        for (int i = 0; i < regex.length(); i++) {
            char c = regex.charAt(i);

            if (isOperand(c)) {
                output.append(c);
            } 
            else if (c == '(') {
                stack.push(c);
            } 
            else if (c == ')') {
                while (!stack.isEmpty() && stack.peek() != '(') {
                    output.append(stack.pop());
                }
                if (!stack.isEmpty() && stack.peek() == '(') {
                    stack.pop(); // eliminar el (
                }
            } 
            else {
                // operador: pop mientras el de la pila tenga mayor o igual precedencia
                while (!stack.isEmpty() && stack.peek() != '(' && precedencia(stack.peek()) >= precedencia(c)) {
                    output.append(stack.pop());
                }
                stack.push(c);
            }
        }

        while (!stack.isEmpty()) {
            char t = stack.pop();
            if (t != '(' && t != ')') output.append(t); // ignorar los paréntesis que queden
        }

        return output.toString();

        // throw new UnsupportedOperationException("Not implemented");
    }

    /**
     * Método auxiliar que indica la precedencia de cada operador 
     * (los de mayor valor son los que tiene más precedencia)
     * @param operador El operador del que se quiere obtener la precedencia
     */

    private static int precedencia(char operador) {
        switch (operador) {
            case '*':
            case '+':
            case '?':
                return 3; 
            case '·':
                return 2; 
            case '|':
                return 1;
            default:
                return 0;
        }
    }
}
