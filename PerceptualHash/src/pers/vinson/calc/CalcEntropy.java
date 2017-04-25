package pers.vinson.calc;

import java.util.HashMap;
import java.util.Iterator;


public class CalcEntropy {
	private Classification cls = new Classification();
	
	public void calc(){
		count();
		if(null == cls) return;
		System.out.println("calc");
		double srcIPEntropy = calcEntropy(cls.getSrcIPList());
		double desIPEntropy = calcEntropy(cls.getDesIPList());
		double srcPortEntropy = calcEntropy(cls.getSrcPortList());
		double desPortEntropy = calcEntropy(cls.getDesPortList());
		System.out.println(srcIPEntropy);
		System.out.println(desIPEntropy);
		System.out.println(srcPortEntropy);
		System.out.println(desPortEntropy);
	}
	
	public double calcEntropy(HashMap<String, Integer> hashMap){
		Iterator<String> it = hashMap.keySet().iterator();
		int size = hashMap.size();
		String str = null;
		double p = 0.0f;
		double entropy = 0.0f;
		while(it.hasNext()){
			str = (String)it.next();
			p = (double)hashMap.get(str) / size;
			entropy += p * Math.log(p) / Math.log(2);
		}
		return -entropy;
	}
	
	public void count(){
		cls.readFile("data/output.txt");
		HashMap<String, Integer> hashMap = cls.getSrcIPList();
		Iterator<String> it = hashMap.keySet().iterator();
		while(it.hasNext()){
			String str = (String)it.next();
			System.out.println(str);
			System.out.println(hashMap.get(str));
		}
	}
	
	public static void main(String[] args){
		CalcEntropy calc = new CalcEntropy();
		calc.calc();
	}
}
