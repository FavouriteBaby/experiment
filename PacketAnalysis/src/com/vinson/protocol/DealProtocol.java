package com.vinson.protocol;

import java.util.Arrays;

import org.jnetpcap.packet.JPacket;
import org.jnetpcap.packet.format.FormatUtils;
import org.jnetpcap.protocol.lan.Ethernet;
import org.jnetpcap.protocol.network.Ip4;
import org.jnetpcap.protocol.tcpip.Tcp;

public class DealProtocol {
	private static Tcp tcp;
	private static Ip4 ip = new Ip4();
	private static Ethernet eth;
	
	//处理TCP协议
	public static void initTcp(JPacket packet){
		tcp = new Tcp();
		packet.getHeader(tcp);
	}	
	public static void dealTcp(){
		System.out.println(tcp.destination());
		System.out.println(tcp.source());
	}
	
	
	//处理IP协议
	public static void initIp(JPacket packet){
		packet.getHeader(ip);
	}
	public static void dealIp(){
		System.out.printf(FormatUtils.ip(ip.source()));
		System.out.printf(FormatUtils.ip(ip.source()));
	}
	
	
	//处理Ethernet协议
	public static void initEthernet(JPacket packet){
			eth = new Ethernet();
			packet.getHeader(eth);
			
			if(packet.hasHeader(ip)){
				System.out.println("has ip");
			}
	}
	public static void dealEth(){
		System.out.println(FormatUtils.mac(eth.destination()));
		System.out.println(FormatUtils.mac(eth.source()));
	}
	
}

