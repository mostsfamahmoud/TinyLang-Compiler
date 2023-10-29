/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/File.java to edit this template
 */
package compilerproject;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class FileHandler {
	
    /*
     * Function to read a file from a specific path and returns a String
     * */
    public String ReadFile(String filePath) throws IOException
    {
        String line;
        StringBuilder file = new StringBuilder();
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        /* check if the line is empty */
        while((line = reader.readLine()) != null)
        {
            file.append(line).append("\n");
        }
        reader.close();

        return file.toString();
    }
    
    public String[] SplitFile(String file) throws IOException
    {
		String[] Lines = file.replaceAll("\n", ",").replaceAll("   ",",").split(",");

		for (int i = 0; i < Lines.length; i++) {
				Lines[i] = Lines[i].trim();
		}
		
		return Lines;
    }

    public String GenerateCSV(String file) throws IOException
    {
        return file.replaceAll("   ",",");
    }



    /*
    * Function to write the new formatted file into a specific path
    * */
    public void WriteFile(String inString, String outputFilePath) throws IOException
    {
        /* check if the inputPath file is empty */
        if ((inString == null) || (inString.trim().length() == 0))
            return;

        BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilePath));

        writer.write(inString);
        writer.close();
    }
}