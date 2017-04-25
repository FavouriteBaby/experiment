package pers.vinson.calc;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class Classification {
	private HashMap<String, Integer> srcIP = new HashMap<String, Integer>();
	private HashMap<String, Integer> desIP = new HashMap<String, Integer>();
	private HashMap<String, Integer> srcPort = new HashMap<String, Integer>();
	private HashMap<String, Integer> desPort = new HashMap<String, Integer>();
	
	//read txt file
	public void readFile(String filePath){
		BufferedReader reader = null;
		try{
			reader = new BufferedReader(new FileReader(filePath));
			String str = null;
			while((str = reader.readLine()) != null){
				count(srcIP, str.split(",")[0]);
				count(desIP, str.split(",")[1]);
				count(srcPort, str.split(",")[2]);
				count(desPort, str.split(",")[3]);
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
	
	public static void count(HashMap<String, Integer> hashMap, String str){
		if(!hashMap.containsKey(str)){
			hashMap.put(str, new Integer(1));
		}else{
			int k = hashMap.get(str).intValue()+1;
			hashMap.put(str, new Integer(k));
		}
	}
	
	public HashMap<String, Integer> getSrcIPList(){
		return srcIP;
	}
	
	public HashMap<String, Integer> getDesIPList(){
		return desIP;
	}
	
	public HashMap<String, Integer> getSrcPortList(){
		return srcPort;
	}
	
	public HashMap<String, Integer> getDesPortList(){
		return desPort;
	}
	
	/*
	public static void main(String[] args){
		Classification cls = new Classification();
		cls.readFile("data/output.txt");
		HashMap<String, Integer> hashMap = cls.getSrcIPList();
		Iterator<String> it = hashMap.keySet().iterator();
		while(it.hasNext()){
			String str = (String)it.next();
			System.out.println(str);
			System.out.println(hashMap.get(str));
		}
	}
	*/
}
