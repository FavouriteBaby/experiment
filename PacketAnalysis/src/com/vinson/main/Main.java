package com.vinson.main;

import com.vinson.protocol.*;

public class Main {
	public static void main(String[] args){
		final String fileName = "caida/oc48-mfn.dirA.20020814-160000.UTC.anon.pcap/20020814-090000-0-anon.pcap";
		//final String fileName = "caida/Faulty.pcap";
		ReadFile objFile = new ReadFile(fileName);
		ProtocolClassifier objClassifier = new ProtocolClassifier();
		objClassifier.classifyPro(objFile.getPcap());
	}
}
