package pers.vinson.Save;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;


import pers.vinson.datastruct.ProtocolData;


public class SaveFile {
	public static void createFile(String filePath, String fileName, byte[] content){
		StringBuilder url = new StringBuilder();
		url.append(filePath);
		url.append("\\");
		url.append(fileName);
		url.append(".txt");
		
		File file = new File(url.toString());
		try{
			if(!file.exists()){
				file.createNewFile();
				save(url.toString(), content);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void save(String filePath, byte[] content){
		OutputStream output = null;
		try{
			output = new BufferedOutputStream(new FileOutputStream(filePath));
			output.write(content);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try{
				output.flush();
				output.close();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	
	public static void save(String filePath, int content){
		OutputStream output = null;
		try{
			output = new BufferedOutputStream(new FileOutputStream(filePath));
			output.write(content);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try{
				output.flush();
				output.close();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	
	public static void save(String filePath, List<ProtocolData> content) throws IOException {
		BufferedWriter writer = null;
		try{
			writer = new BufferedWriter(new FileWriter(filePath));
			for(ProtocolData data : content){
				writer.write(data.getDesIP() + "," + data.getDesIP() + "," + data.getSrcPort() + "," + data.getDesPort());
				writer.newLine();
			}
				
		}catch(IOException e){
			e.printStackTrace();
		}finally{
			if(writer != null)
				writer.close();
		}
	}
}
