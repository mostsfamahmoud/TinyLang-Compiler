/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/File.java to edit this template
 */
package compilerproject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javafx.scene.control.TextArea;

public class Scanner {

    public Scanner(TextArea TextArea1) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    private enum State {
        START, INCOMMENT, INNUM, INID, INASSIGN, DONE
    }

    private String file;
    private int index;
    private ArrayList<Token> tokens = new ArrayList<Token>();
    private int pos;
    private int tokenStart;
    private int fileSize;
    private State state;
    private String tok;

    public Scanner(String data) {
        file = data;
        pos = 0;
        index = 0;
        tokenStart = 0;
        fileSize = file.length();
        State state = State.START;
       Map<Character, String> operatorTokens = new HashMap<>(); // Use HashMap instead of Map
        operatorTokens.put('+', "PLUS");
        operatorTokens.put('-', "MINUS");
        operatorTokens.put('*', "MULT");
        operatorTokens.put('/', "DIV");
        operatorTokens.put('=', "EQUAL");
        operatorTokens.put('<', "LESSTHAN");
        operatorTokens.put('(', "OPENBRACKET");
        operatorTokens.put(')', "CLOSEDBRACKET");
        operatorTokens.put(';', "SEMICOLON");
        while (pos < fileSize) {
            char ch = file.charAt(pos);

            switch (state) {
                case START:
                    String operatorToken = operatorTokens.get(ch);
                    if (operatorToken != null) {
                        tokens.add(new Token(operatorToken, String.valueOf(ch), pos));
                        pos++;
                        continue;
                    }
                    switch (ch) {
                        case ' ':
                            break;
                        case ':':
                            state = State.INASSIGN;
                            tokenStart = pos;
                            break;
                        case '{':
                            state = State.INCOMMENT;
                            tokenStart = pos;
                            break;
                        default:
                            if (Character.isAlphabetic(ch))
                                state = State.INID;
                            else if (Character.isDigit(ch))
                                state = State.INNUM;

                            tokenStart = pos;
                            break;
                    }
                    pos++;
                    continue;
                case INNUM:
                    if (Character.isDigit(ch))
                        pos++;
                    else {
                        String tok = file.substring(tokenStart, pos);
                        tokens.add(new Token("NUMBER", tok, pos - 1));
                        state = State.START;
                    }
                    continue;
                case INID:
                    if (Character.isAlphabetic(ch))
                        pos++;
                    else {
                        String tok = file.substring(tokenStart, pos);
                        String[] keywords = {"if", "then", "else", "repeat", "until", "end", "write", "read"};
                        boolean isKeyword = false;

                        for (String keyword : keywords) {
                            if (keyword.equals(tok)) {
                                tokens.add(new Token(keyword.toUpperCase(), tok, tokenStart));
                                isKeyword = true;
                                break;
                            }
                        }

                        if (!isKeyword)
                            tokens.add(new Token("IDENTIFIER", tok, tokenStart));

                        state = State.START;
                    }
                    continue;
                case INASSIGN:
                    if (ch == '=') {
                        tokens.add(new Token("ASSIGN", ":=", pos));
                        pos++;
                    }
                    state = State.START;
                    continue;
                case INCOMMENT:
                    if (ch == '}')
                        state = State.START;

                    pos++;
            }
        }

        switch (state) {
            case INID:
                String tok = file.substring(tokenStart, pos);
                String[] keywords = {"if", "then", "else", "repeat", "until", "end", "write", "read"};
                boolean isKeyword = false;

                for (String keyword : keywords) {
                    if (keyword.equals(tok)) {
                        tokens.add(new Token(keyword.toUpperCase(), tok, tokenStart));
                        isKeyword = true;
                        break;
                    }
                }

                if (!isKeyword)
                    tokens.add(new Token("IDENTIFIER", tok, tokenStart));

                break;
            case INNUM:
                tok = file.substring(tokenStart, pos);
                tokens.add(new Token("NUMBER", tok, pos - 1));
                break;
        }
    }


    public String getTokens() {
        StringBuilder tokenData = new StringBuilder();
        for (Token token : tokens) {
            tokenData.append(token.getTokenVal()).append(", ").append(token.getTokenType()).append("\n");
        }
        return tokenData.toString();
    }
    public ArrayList<Token> getTokensList() {
    return tokens;
}


    public Token nextToken() {
        if (index >= tokens.size()) {
            return new Token("EOF", "", -1);
        }
        return tokens.get(index++);
    }

}