/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/File.java to edit this template
 */
package compilerproject;


public class Token {
	/**
	 * Token class represents a Token in a Scanner
	 * a token has a type which is IF,THEN,NUMBER,IDENTIFIER ... etc
	 * and has a value which represents the actual string
	 * Examples: (Value:Type)
	 *  (if:IF)
	 *  (459:NUMBER)
	 *  (ahmed:IDENTIFER)
	 */
	

	
	
	public static enum TokenType {
		IF, THEN, ELSE, END, REPEAT, UNTIL, READ, WRITE,
		PLUS, MINUS, TIMES, DIVIDE, EQUAL, LESS_THAN, GREATER_THAN,
		LEFT_PARENTHESIS, RIGHT_PARENTHESIS, SEMI_COLON, ASSIGN,
		NUMBER, IDENTIFER, END_OF_FILE
	}
	
	private String value;
	private TokenType type;
	
	public Token() {
		value = "";
		type = null;
	}
	
	/**
	 * Construct a new Token 
	 * @param type	Type of the token must be from TokenType enum
	 * @param value	The string value of the token
	 */
	public Token(TokenType type, String value){
		this.type = type;
		this.value = value;
	}
	
	/**
	 * @return the token value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * @param value the token value to set
	 */
	public void setValue(String value) {
		this.value = value;
	}

	/**
	 * @return the type of the token
	 */
	public TokenType getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(TokenType type) {
		this.type = type;
	}

	/**
	 * Returns a string representing the token 
	 */
	public String toString(){
		return (value.isEmpty()? "" : value + "   ")
				+ type.name();
	}
}
	