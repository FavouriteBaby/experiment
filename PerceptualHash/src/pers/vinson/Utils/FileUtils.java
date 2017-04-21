package pers.vinson.Utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class FileUtils {
	public static BufferedReader openFile(String filePath){
		BufferedReader reader = null;
		try{
			reader = new BufferedReader(new FileReader(filePath));
		}catch(IOException e){
			e.printStackTrace();
		}
		return reader;
	}
	
	public static String readByLine(BufferedReader reader){
		StringBuilder str = new StringBuilder();
		return str.toString();
	}
}
