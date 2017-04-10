package com.vinson.tool;

import java.io.IOException;

import org.jnetpcap.JBufferHandler;
import org.jnetpcap.Pcap;
import org.jnetpcap.PcapDumper;
import org.jnetpcap.PcapHeader;
import org.jnetpcap.nio.JBuffer;
import org.jnetpcap.packet.JPacket;
import org.jnetpcap.packet.format.XmlFormatter;

//Debug class
public class Dump {
	public static void dump(Pcap objPcap, int cnt){
		String ofile = "caida/temp.cap";
		PcapDumper dumper = objPcap.dumpOpen(ofile);
		JBufferHandler<PcapDumper> dumpHandler = new JBufferHandler<PcapDumper>(){
			public void nextPacket(PcapHeader header, JBuffer buffer, PcapDumper dumper){
				dumper.dump(header, buffer);
			}
		};
		
		objPcap.loop(cnt, dumpHandler, dumper);

		System.out.printf("%s", ofile);
		dumper.close();
	}
	
	public static void dump(JPacket packet){
		XmlFormatter out = new XmlFormatter(System.out);
		try {
			out.format(packet);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
}
