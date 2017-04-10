package com.vinson.test;

import java.sql.Date;

import org.jnetpcap.Pcap;
import org.jnetpcap.packet.PcapPacket;
import org.jnetpcap.packet.PcapPacketHandler;

public class PacketAna{
	public static void main(String[] args){
		//首先创建一个用来表示错误信息的字符串和文件名字符串
		final StringBuilder errbuf = new StringBuilder();
		final String file = "caida/oc48-mfn.dirA.20020814-160000.UTC.anon.pcap/20020814-090000-0-anon.pcap";

		//用openOffline()方法打开选中的文件
		Pcap pcap = Pcap.openOffline(file, errbuf);

		if(pcap == null){
			System.err.printf("error");
		}

		//用来接收数据包的handler
		PcapPacketHandler<String> jpacketHandler = new PcapPacketHandler<String>(){
			public void nextPacket(PcapPacket packet, String user){
				System.out.printf("Received at %s caplen=%-4d len=%-4d %s\n", new Date(packet.getCaptureHeader().timestampInMillis()),packet.getCaptureHeader().caplen(),packet.getCaptureHeader().wirelen(),user);
			}
		};

		//抓取10个数据包
		try{
			int num = pcap.loop(10, jpacketHandler, "jNetPcap rocks");
			System.out.println(num);
		}finally{
			pcap.close();
		}
	}
}