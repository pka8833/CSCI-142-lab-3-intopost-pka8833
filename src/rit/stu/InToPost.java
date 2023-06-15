package rit.stu;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

/**
 * A program that converts an infix expression to postfix.<br>
 * <br>
 *  Usage: java InToPost filename<br>
 * <br>
 * For example, prog1.txt, containing infix expressions (one per line):<br>
 * <br>
 *  A + B<br>
 *  A + B * C<br>
 *  A + B * C + D<br>
 *  ( A + B ) * C<br>
 *  ( A + B ) / ( C - D )<br>
 *  ( ( A + B ) * C - ( D - E ) )<br>
 * <br>
 * When run: java InToPost prog1.txt:<br>
 * <br>
 *  InToPost: converting expressions from infix to postfix...<br>
 *  [A, +, B]<br>
 *  [A, +, B, *, C]<br>
 *  [A, +, B, *, C, +, D]<br>
 *  [(, A, +, B, ), *, C]<br>
 *  [(, A, +, B, ), /, (, C, -, D, )]<br>
 *  [(, (, A, +, B, ), *, C, -, (, D, -, E, ), )]<br>
 *  InToPost: emitting postfix expressions...<br>
 *  A B +<br>
 *  A B C * +<br>
 *  A B C * + D +<br>
 *  A B + C *<br>
 *  A B + C D - /<br>
 *  A B + C * D E - -<br>
 * <br>
 * @author RIT CS
 * @author PHILLIP ATTEA
 */
public class InToPost {
    /** The add operator */
    public static final String ADD = "+";
    /** The subtract operator */
    public static final String SUBTRACT = "-";
    /** The multiply operator */
    public static final String MULTIPLY = "*";
    /** The divide operator */
    public static final String DIVIDE = "/";
    /** The open parentheses operator */
    public static final String OPEN_PAREN = "(";
    /** The close parentheses operator */
    public static final String CLOSE_PAREN = ")";

    /** The list of converted postfix expressions */
    private final List<List<String>> infixExpressions;
    /** The name of the infix source file */
    private final String srcFile;

    /**
     * Constructs a new InToPost object.
     *
     * @param filename The name of the source file containing infix expressions.
     */
    public InToPost(String filename) {
        infixExpressions = new ArrayList<>();
        srcFile = filename;
    }

    /**
     * Tokenizes the given expression into individual tokens.
     *
     * @param expression The infix expression to tokenize.
     * @return A list of tokens.
     */
    private List<String> tokenizeExpression(String expression) {
        List<String> tokens = new ArrayList<>();
        String[] split = expression.split("\\s+");
        Collections.addAll(tokens, split);
        return tokens;
    }

    /**
     * Converts an infix expression to postfix notation.
     *
     * @param infixExpression The infix expression to convert.
     * @return A queue containing the postfix expression.
     */
    private Queue<String> convertToPostfix(List<String> infixExpression) {
        Queue<String> postfixExpression = new LinkedList<>();
        Stack<String> operatorStack = new Stack<>();

        for (String token : infixExpression) {
            if (isOperand(token)) {
                postfixExpression.add(token);
            } else if (isOperator(token)) {
                while (!operatorStack.isEmpty() && !OPEN_PAREN.equals(token) &&
                        hasHigherPrecedence(operatorStack.peek(), token)) {
                    postfixExpression.add(operatorStack.pop());
                }
                operatorStack.push(token);
            } else if (OPEN_PAREN.equals(token)) {
                operatorStack.push(token);
            } else if (CLOSE_PAREN.equals(token)) {
                while (!operatorStack.isEmpty() && !OPEN_PAREN.equals(operatorStack.peek())) {
                    postfixExpression.add(operatorStack.pop());
                }
                if (!operatorStack.isEmpty() && OPEN_PAREN.equals(operatorStack.peek())) {
                    operatorStack.pop(); // Discard the opening parenthesis
                } else {
                    throw new IllegalArgumentException("Mismatched parentheses");
                }
            }
        }

        while (!operatorStack.isEmpty()) {
            if (isParenthesis(operatorStack.peek())) {
                throw new IllegalArgumentException("Mismatched parentheses");
            }
            postfixExpression.add(operatorStack.pop());
        }

        return postfixExpression;
    }

    /**
     * Checks if the given token is an operand (variable).
     *
     * @param token The token to check.
     * @return true if the token is an operand, false otherwise.
     */
    private boolean isOperand(String token) {
        return token.matches("[a-zA-Z]+");
    }

    /**
     * Checks if the given token is an operator.
     *
     * @param token The token to check.
     * @return true if the token is an operator, false otherwise.
     */
    private boolean isOperator(String token) {
        return token.equals(ADD) || token.equals(SUBTRACT) ||
                token.equals(MULTIPLY) || token.equals(DIVIDE);
    }

    /**
     * Checks if the given token is a parenthesis.
     *
     * @param token The token to check.
     * @return true if the token is a parenthesis, false otherwise.
     */
    private boolean isParenthesis(String token) {
        return token.equals(OPEN_PAREN) || token.equals(CLOSE_PAREN);
    }

    /**
     * Checks if the operator at the top of the stack has higher precedence than the current operator.
     *
     * @param topOperator      The operator at the top of the stack.
     * @param currentOperator  The current operator.
     * @return true if the top operator has higher precedence, false otherwise.
     */
    private boolean hasHigherPrecedence(String topOperator, String currentOperator) {
        int topPrecedence = getPrecedence(topOperator);
        int currentPrecedence = getPrecedence(currentOperator);
        return topPrecedence >= currentPrecedence;
    }

    /**
     * Gets the precedence level of an operator.
     * Operators with higher precedence have a higher level.
     *
     * @param operator The operator to get the precedence of.
     * @return The precedence level of the operator.
     */
    private int getPrecedence(String operator) {
        if (operator.equals(ADD) || operator.equals(SUBTRACT)) {
            return 1;
        } else if (operator.equals(MULTIPLY) || operator.equals(DIVIDE)) {
            return 2;
        } else {
            return 0;
        }
    }

    /**
     * Converts all infix expressions in the source file to postfix expressions.
     *
     * @throws FileNotFoundException if the file is not found.
     */
    public void convert() throws FileNotFoundException {
        File file = new File(srcFile);
        Scanner scanner = new Scanner(file);

        while (scanner.hasNextLine()) {
            String infixExpression = scanner.nextLine().trim();
            List<String> tokens = tokenizeExpression(infixExpression);
            infixExpressions.add(tokens);

            // Print the converted expression
            System.out.println(tokens);
        }

        scanner.close();
    }

    /**
     * Displays the converted postfix expressions.
     */
    public void emit() {
        for (List<String> infixExpression : infixExpressions) {
            Queue<String> postfixExpression = convertToPostfix(infixExpression);

            // Print the postfix expression
            while (!postfixExpression.isEmpty()) {
                System.out.print(postfixExpression.poll() + " ");
            }
            System.out.println();
        }
    }

    /**
     * The main program takes the source file of infix expressions, converts them to postfix, and displays them.
     *
     * @param args Command line arguments.
     * @throws FileNotFoundException if the source file is not found.
     */
    public static void main(String[] args) throws FileNotFoundException {
        if (args.length == 0) {
            System.out.println("Usage: java InToPost filename");
            return;
        }

        String filename = args[0];
        InToPost converter = new InToPost(filename);

        System.out.println("InToPost: converting expressions from infix to postfix...");
        converter.convert();
        System.out.println("InToPost: emitting postfix expressions...");
        converter.emit();
    }
}