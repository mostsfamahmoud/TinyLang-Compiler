/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/File.java to edit this template
 */
package compilerproject;

import java.util.ArrayList;
import java.util.List;
import javafx.scene.control.TextArea;


public class Parser {

    private Scanner scanner;  // Scanner object for tokenization
    private Token currToken;  // Current token being processed
    private SyntaxTree tree;  // Syntax tree construction object

    /**
     * Constructor for Parser class.
     *
     * @param scanner The Scanner object used for tokenization.
     *                Initializes the parser with the given Scanner and creates a SyntaxTree.
     */
    public Parser(Scanner scanner) {
        this.scanner = scanner;
        this.currToken = scanner.nextToken();  // Gets the first token from the scanner
        this.tree = new SyntaxTree();  // Initializes the syntax tree
    }

    public Parser(TextArea TextArea1) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public Parser(String toString) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    /**
     * Matches the current token with the expected token.
     *
     * @param expectedType The token expected to match the current token.
     * @throws SyntaxException if the tokens do not match.
     */
    private void match(String expectedType) throws SyntaxException
    {
        if (!expectedType.equals(currToken.getTokenType())) {
            throw new SyntaxException("Syntax error. Expected " + expectedType + " \nInstead got token \"" + currToken.getTokenType() + "\"");
        }
        currToken = scanner.nextToken();  // Advances to the next token after a successful match
    }

    /**
     * Parses the input and constructs the syntax tree.
     *
     * @throws SyntaxException if a syntax error is encountered or a SEMICOLON is missing.
     */
    public void parse() throws SyntaxException {
        statementSequence();  // Initiates parsing of the statement sequence
        if (!currToken.getTokenType().equals("EOF")) {
            throw new SyntaxException("Missing SEMICOLON");
        }
        tree.end();  // Ends the construction of the syntax tree
    }

    /**
     * Advances the current token to the next one.
     */
    private void advance() {
        currToken = scanner.nextToken();  // Advances to the next token
    }

    public SyntaxTree getTree() {
        return tree;
    }

    /**
     * Parses and constructs a sequence of statements.
     *
     * @return the ID of the initial statement.
     * @throws SyntaxException if a syntax error is encountered.
     */
    private int statementSequence() throws SyntaxException {
        int stmt = statement();  // Parses a single statement
        int stmtSeq = stmt;  // Stores the ID of the initial statement
        while (currToken.getTokenType().equals("SEMICOLON")) {
            match("SEMICOLON");  // Matches a SEMICOLON token
            int newStmt = statement();  // Parses the next statement
            tree.addChild(stmt, newStmt);  // Adds the new statement as a child
            tree.sameRank(stmt, newStmt);  // Marks statements at the same level in the syntax tree
            stmt = newStmt;  // Updates the current statement
        }
        return stmtSeq;  // Returns the ID of the initial statement
    }


    /**
     * Parses and handles different types of statements: IF, WRITE, READ, ASSIGN, REPEAT.
     *
     * @return the ID of the parsed statement in the syntax tree.
     * @throws SyntaxException if a syntax error is encountered.
     */
    private int statement() throws SyntaxException {
        switch (currToken.getTokenType()) {
            case "IF":
                return ifStatement();
            case "WRITE":
                return writeStatement();
            case "READ":
                return readStatement();
            case "IDENTIFIER":
                return assignStatement();
            case "REPEAT":
                return repeatStatement();
            default:
                throw new SyntaxException("Syntax error near token \"" + currToken.getTokenType() + "\"");
        }
    }


    /**
     * Parses and constructs an IF statement.
     *
     * @return the ID of the IF node in the syntax tree.
     * @throws SyntaxException if a syntax error is encountered.
     */
    private int ifStatement() throws SyntaxException {
        match("IF");
        int ifNode = tree.makeIFNode();
        tree.addChild(ifNode, expression());
        match("THEN");
        tree.addChild(ifNode, statementSequence());
        if (currToken.getTokenType().equals("ELSE"))
        {
            match("ELSE");
            tree.addChild(ifNode, statementSequence());
        }
        match("END");
        return ifNode;
    }

    /**
     * Parses and constructs a REPEAT statement.
     *
     * @return the ID of the REPEAT node in the syntax tree.
     * @throws SyntaxException if a syntax error is encountered.
     */
 private int repeatStatement() throws SyntaxException {
    match("REPEAT");  // Matches a REPEAT token
    int repeatNode = tree.makeRepeatNode();  // Creates a REPEAT node in the syntax tree

    // Collect statements in the body before adding them to the syntax tree
    int[] bodyStatements = collectStatementsInBody();

    // Add the first statement in the body to the syntax tree with a white line
    // Add the first statement in the body to the syntax tree with a white line
if (bodyStatements.length > 0) {
    tree.addChild(repeatNode, bodyStatements[0]);
}

// Add the rest of the statements with white lines
for (int i = 1; i < bodyStatements.length; i++) {
    tree.addChild(repeatNode, bodyStatements[i], "white");
    tree.sameRank(bodyStatements[i - 1], bodyStatements[i]);  // Marks statements at the same level
}

    match("UNTIL");  // Matches an UNTIL token
    int test = expression();  // Parses the condition for the REPEAT loop
    tree.addChild(repeatNode, test);  // Adds the condition test as a child of the REPEAT node
    return repeatNode;  // Returns the ID of the REPEAT node
}



private int[] collectStatementsInBody() throws SyntaxException {
    // Collect statements in the body before adding them to the syntax tree
    int stmt = statement();
    List<Integer> bodyStatements = new ArrayList<>();
    bodyStatements.add(stmt);

    boolean collectingBody = true;
    while (collectingBody && currToken.getTokenType().equals("SEMICOLON")) {
        match("SEMICOLON");  // Matches a SEMICOLON token

     if (currToken.getTokenType().equals("UNTIL")) {
            // Stop collecting body when UNTIL is encountered
           collectingBody = false;
       } else {
           int newStmt = statement();  // Parses the next statement
           bodyStatements.add(newStmt);  // Add the new statement to the list
       }
  }

    // Connect body statements in the syntax tree
    for (int i = 0; i < bodyStatements.size() - 1; i++) {
        tree.addChild(bodyStatements.get(i), bodyStatements.get(i + 1));
        tree.sameRank(bodyStatements.get(i), bodyStatements.get(i + 1));
    }

    return bodyStatements.stream().mapToInt(Integer::intValue).toArray();
}


    /**
     * Parses and constructs an ASSIGN statement.
     *
     * @return the ID of the ASSIGN node in the syntax tree.
     * @throws SyntaxException if a syntax error is encountered.
     */
    private int assignStatement() throws SyntaxException {
        Token identifier = currToken;
        match("IDENTIFIER");
        match("ASSIGN");
        int assignNode = tree.makeAssignNode(identifier.getTokenVal());
        tree.addChild(assignNode, expression());
        return assignNode;
    }

    /**
     * Parses and constructs a READ statement.
     *
     * @return the ID of the READ node in the syntax tree.
     * @throws SyntaxException if a syntax error is encountered.
     */
    private int readStatement() throws SyntaxException {
        match("READ");
        Token identifier = currToken;
        match("IDENTIFIER");
        return tree.makeReadNode(identifier.getTokenVal());
    }

    /**
     * Parses and constructs a WRITE statement.
     *
     * @return the ID of the WRITE node in the syntax tree.
     * @throws SyntaxException if a syntax error is encountered.
     */
    private int writeStatement() throws SyntaxException {
        match("WRITE");
        int writeNode = tree.makeWriteNode();
        tree.addChild(writeNode, expression());
        return writeNode;
    }

    /**
     * Parses and constructs an expression.
     *
     * @return the ID of the expression node in the syntax tree.
     * @throws SyntaxException if a syntax error is encountered.
     */
    private int expression() throws SyntaxException {
        int simpleExp = simpleExpression();
        if (currToken.getTokenType().equals("EQUAL") || currToken.getTokenType().equals("LESSTHAN"))
        {
            int opNode = comparisonOp();
            tree.addChild(opNode, simpleExp);
            tree.addChild(opNode, simpleExpression());
            simpleExp = opNode;
        }
        return simpleExp;
    }

    /**
     * Parses and constructs a comparison operator node.
     *
     * @return the ID of the comparison operator node in the syntax tree.
     * @throws SyntaxException if a syntax error is encountered.
     */
    private int comparisonOp() throws SyntaxException {
        Token token = currToken;
        switch (token.getTokenType()) {
            case "LESSTHAN":
                match("LESSTHAN");
                return tree.makeOPNode("<");
            case "EQUAL":
                match("EQUAL");
                return tree.makeOPNode("=");
            default:
                throw new SyntaxException("Syntax error near token \"" + token.getTokenType() + "\"");
        }
    }

    /**
     * Parses and constructs a simple expression.
     *
     * @return the ID of the simple expression node in the syntax tree.
     * @throws SyntaxException if a syntax error is encountered.
     */
    private int simpleExpression() throws SyntaxException {
        int temp = term();
        while (currToken.getTokenType().equals("PLUS") || currToken.getTokenType().equals("MINUS")) {
            int opNode = addOp();
            tree.addChild(opNode, temp);
            tree.addChild(opNode, term());
            temp = opNode;
        }
        return temp;
    }

    /**
     * Parses and constructs an addition or subtraction operator node.
     *
     * @return the ID of the addition or subtraction operator node in the syntax tree.
     * @throws SyntaxException if a syntax error is encountered.
     */
    private int addOp() throws SyntaxException {
        Token token = currToken;
        switch (token.getTokenType()) {
            case "PLUS":
                match("PLUS");
                return tree.makeOPNode("+");
            case "MINUS":
                match("MINUS");
                return tree.makeOPNode("-");
            default:
                throw new SyntaxException("Syntax error near token \"" + token.getTokenType() + "\"");
        }
    }

    /**
     * Parses and constructs a term.
     *
     * @return the ID of the term node in the syntax tree.
     * @throws SyntaxException if a syntax error is encountered.
     */
    private int term() throws SyntaxException {
        int temp = factor();
        while (currToken.getTokenType().equals("MULT") || currToken.getTokenType().equals("DIV")) {
            int opNode = mulOp();
            tree.addChild(opNode, temp);
            tree.addChild(opNode, factor());
            temp = opNode;
        }
        return temp;

    }

    /**
     * Parses and constructs a multiplication or division operator node.
     *
     * @return the ID of the multiplication or division operator node in the syntax tree.
     * @throws SyntaxException if a syntax error is encountered.
     */
    private int mulOp() throws SyntaxException {
        Token token = currToken;
        switch (token.getTokenType()) {
            case "DIV":
                match("DIV");
                return tree.makeOPNode("/");
            case "MULT":
                match("MULT");
                return tree.makeOPNode("*");
            default:
                throw new SyntaxException("Syntax error near token \"" + token.getTokenType() + "\"");
        }
    }

    /**
     * Parses and constructs a factor.
     *
     * @return the ID of the factor node in the syntax tree.
     * @throws SyntaxException if a syntax error is encountered.
     */
    private int factor() throws SyntaxException {
        int result;
        switch (currToken.getTokenType()) {
            case "OPENBRACKET":
                advance();
                result = expression();
                match("CLOSEDBRACKET");
                break;
            case "NUMBER":
                result = tree.makeConstNode(currToken.getTokenVal());
                advance();
                break;
            case "IDENTIFIER":
                result = tree.makeIDNode(currToken.getTokenVal());
                advance();
                break;
            default:
                throw new SyntaxException("Syntax error near token \"" + currToken.getTokenType() + "\"");
        }
        return result;
    }

}
