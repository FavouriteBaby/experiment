package pers.vinson.calc;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Classification {
	private List<StringBuilder> listSrcIP = new ArrayList<StringBuilder>();
	private List<StringBuilder> listDesIP = new ArrayList<StringBuilder>();
	private List<StringBuilder> listSrcPort = new ArrayList<StringBuilder>();
	private List<StringBuilder> listDesPort = new ArrayList<StringBuilder>();
	
	//read txt file
	public void readFile(String filePath){
		BufferedReader reader = null;
		try{
			reader = new BufferedReader(new FileReader(filePath));
			String str = null;
			while((str = reader.readLine()) != null){
				System.out.println(str);
				listSrcIP.add(str.split(",")[0]);
			}

		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(reader != null){
				try{
					reader.close();
				}catch(IOException e){
					e.printStackTrace();
				}
			}
		}
	}
	
	public List<StringBuilder> getSrcIPList(){
		return listSrcIP;
	}
	
	public List<StringBuilder> getDesIPList(){
		return listDesIP;
	}
	
	public List<StringBuilder> getSrcPortList(){
		return listSrcPort;
	}
	
	public List<StringBuilder> getDesPortList(){
		return listDesPort;
	}
	
	public static void main(String[] args){
		Classification cls = new Classification();
		cls.readFile("data/test_5.txt");
	}
}
