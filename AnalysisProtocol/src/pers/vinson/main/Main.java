package pers.vinson.main;
import java.io.File;

import pers.vinson.PcapParse.*;

public class Main {
	public static void main(String[] args){
		File pcapFile = new File("data/test.pcap");
		File outDir = new File("data/output.txt");
		PcapParser pcap = new PcapParser(pcapFile, outDir);
		
		pcap.parse();
	}
}
