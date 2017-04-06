package com.vinson.protocol;
import com.vinson.tool.*;

import java.io.IOException;

import org.jnetpcap.Pcap;
import org.jnetpcap.packet.JPacket;
import org.jnetpcap.packet.JPacketHandler;
import org.jnetpcap.packet.format.XmlFormatter;
import org.jnetpcap.protocol.lan.Ethernet;
import org.jnetpcap.protocol.network.Icmp;
import org.jnetpcap.protocol.network.Ip4;
import org.jnetpcap.protocol.tcpip.Http;
import org.jnetpcap.protocol.tcpip.Tcp;
import org.jnetpcap.protocol.tcpip.Udp;

public class ProtocolClassifier {
	
	//根据协议类型进行分类
	public void classifyPro(Pcap objPcap){
		StringBuilder errbuf = new StringBuilder("error occur in ProtocolClassifier.classifier()");
		objPcap.loop(10, new JPacketHandler<StringBuilder>(){
			public void nextPacket(JPacket packet, StringBuilder errbuf){
				//System.out.println(packet.getHeaderCount());
				//Dump.dump(objPcap, 1);
				//System.out.println(packet.toString());
				//objPcap.dumpOpen("caida/temp.cap");
				//Dump.dump(packet);
				//System.out.println("num:" + packet.getFrameNumber());
				if(packet.hasHeader(Ethernet.ID)){
					DealProtocol.initEthernet(packet);
					DealProtocol.dealEth();
				}
				
				if(packet.hasHeader(Udp.ID)){
					System.out.println("has");
				}
				
				if(packet.hasHeader(Tcp.ID)){
					DealProtocol.initTcp(packet);
					DealProtocol.dealTcp();
				}
				
				if(packet.hasHeader(Ip4.ID)){
					DealProtocol.initIp(packet);
					DealProtocol.dealIp();
				}
				
				if(packet.hasHeader(Http.ID)){
					System.out.print("http");
				}
				
				if(packet.hasHeader(Icmp.ID)){
					System.out.println("icmp");
				}
						
			}
		}, errbuf);
		
		objPcap.close();
	}
}
