package com.vinson.test;

import java.sql.Date;

import org.jnetpcap.Pcap;
import org.jnetpcap.packet.PcapPacket;
import org.jnetpcap.packet.PcapPacketHandler;

public class PacketAna{
	public static void main(String[] args){
		//���ȴ���һ��������ʾ������Ϣ���ַ������ļ����ַ���
		final StringBuilder errbuf = new StringBuilder();
		final String file = "caida/oc48-mfn.dirA.20020814-160000.UTC.anon.pcap/20020814-090000-0-anon.pcap";

		//��openOffline()������ѡ�е��ļ�
		Pcap pcap = Pcap.openOffline(file, errbuf);

		if(pcap == null){
			System.err.printf("error");
		}

		//�����������ݰ���handler
		PcapPacketHandler<String> jpacketHandler = new PcapPacketHandler<String>(){
			public void nextPacket(PcapPacket packet, String user){
				System.out.printf("Received at %s caplen=%-4d len=%-4d %s\n", new Date(packet.getCaptureHeader().timestampInMillis()),packet.getCaptureHeader().caplen(),packet.getCaptureHeader().wirelen(),user);
			}
		};

		//ץȡ10�����ݰ�
		try{
			int num = pcap.loop(10, jpacketHandler, "jNetPcap rocks");
			System.out.println(num);
		}finally{
			pcap.close();
		}
	}
}