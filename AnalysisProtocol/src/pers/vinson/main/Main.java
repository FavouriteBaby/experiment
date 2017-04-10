package pers.vinson.main;
import java.io.File;

import pers.vinson.PcapParse.*;

public class Main {
	public static void main(String[] args){
		File pcapFile = new File("data/20020814-090000-0-anon.pcap");
		File outDir = new File("data/output");
		
		PcapParser pcap = new PcapParser(pcapFile, outDir);
		pcap.parse();
	}
}
