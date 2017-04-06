package com.vinson.protocol;

import org.jnetpcap.Pcap;

public class ReadFile {
	private Pcap objPcap;
	
	public ReadFile(String fileName){
		final StringBuilder errbuf = new StringBuilder();
		objPcap = Pcap.openOffline(fileName, errbuf);
		
		if(null == objPcap){
			System.err.println(errbuf);
			return;
		}
	}
	
	public Pcap getPcap(){
		return this.objPcap;
	}
}
