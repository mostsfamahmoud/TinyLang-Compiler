/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/File.java to edit this template
 */
package compilerproject;


public class Token {
    private int index;
    private String tokenType;  // Change to JavaBean naming convention
    private String tokenVal;   // Change to JavaBean naming convention

    public Token() {
        tokenVal = "";
        tokenType = "";
    }

    public Token(String type, String val, int pos) {
        this.tokenVal = val;
        this.tokenType = type;
        this.index = pos;
    }

  public String getTokenVal() {
    return this.tokenVal;
}

public String getTokenType() {
    return this.tokenType;
}

}
