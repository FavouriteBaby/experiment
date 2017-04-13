package pers.vinson.PcapParse;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import pers.vinson.Utils.DataUtil;
import pers.vinson.Utils.DataUtils;
import pers.vinson.Utils.FileUtils;
import pers.vinson.Utils.LogUtils;
import pers.vinson.datastruct.*;

public class PcapParser extends Observable{
	private File pcap;
	private String savePath;
	private PcapStruct struct;
	private ProtocolData protocolData;
	private IPPacketHeader ipHeader;
	private TCPPacketHeader tcpHeader;
	private UDPPacketHeader udpHeader;
	private boolean isBigEnd;	//is big endian
	private int nOffset = 0;		//record where begin parse

	private byte[] file_header = new byte[24];	//閺傚洣娆㈡径锟�
	private byte[] packet_header = new byte[16];	//pcap閺佺増宓侀崠鍛仈闁拷
	private byte[] content;

	//閺嬪嫰锟界姵鏌熷▔锟�
	public PcapParser(File pcap, File outDir){
		this.pcap = pcap;
		this.savePath = outDir.getAbsolutePath();
	}

	public boolean parse(){
		boolean rs = true;
		struct = new PcapStruct();
		List<PcapPacketHeader> packetHeaders = new ArrayList<PcapPacketHeader>();
		FileInputStream file = null;
		try{
			//鐠囪褰囬弬鍥︽
			file = new FileInputStream(pcap);
			int m = file.read(file_header);		//鐠囪褰噁ile_header闂�鍨閻ㄥ嫭鏆熼幑顕嗙礉楠炶泛鐡ㄩ弨鎯у煂file_header

			if(m > 0){
				//parse .pcap file header
				int offset = 0;
				PcapFileHeader fileHeader = ParseFileHeader.parseFileHeader(file_header, offset);
				if (null == fileHeader) 
					System.out.println("fileHeader is null");
				struct.setFileHeader(fileHeader);
				
				//is big endian
				isBigEnd = ParseFileHeader.isBigEnd(fileHeader);
				
				//Debug record the loop times
				int nIndex = 0;
				System.out.println("============begin parse content============");
				
				//each loop should repeat read packet header to confirm what type of link
				while(m > 0){
					//Debug
					System.out.println("=============parse " + nIndex + " times===============");
					//parse packet header
					m = file.read(packet_header);
					PcapPacketHeader packetHeader = parsePacketHeader(packet_header);
					packetHeaders.add(packetHeader);
										
					//parse content
					content = new byte[packetHeader.getCaplen()];
					m = file.read(content);
					System.out.println("the length of content is: " + content.length);
					
					protocolData = new ProtocolData();
					//is parse over
					boolean isDone = parseContent();
					nOffset += content.length;
					if(isDone)
						break;
					
					//Debug
					nIndex++;
					if(11 == nIndex)
						m = -1;
				}
			}
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}finally{
			FileUtils.closeStream(file, null);
		}
		
		return rs;
	}

	//鐟欙絾鐎絧cap閺傚洣娆㈡径锟�
	public PcapFileHeader parseFileHeader(byte[] file_header) throws IOException{
		PcapFileHeader fileHeader = new PcapFileHeader();
		byte[] buff_4 = new byte[4];		//4鐎涙濡惃鍕殶缂侊拷
		byte[] buff_2 = new byte[2];		//2鐎涙濡惃鍕殶缂侊拷

		int offset = 0;						//娴ｅ秶些
		for(int nIndex = 0; nIndex < 4; ++nIndex){
			//鐠囪褰囬崜锟�4娑擃亜鐡ч懞鍌︾礉閸楄櫕鏋冩禒鑸电垼鐠囷拷
			buff_4[nIndex] = file_header[nIndex + offset];
		}
		
		int magic = DataUtils.byteArrayToInt(buff_4);	//pcap閺傚洣娆㈤弽鍥槕
		fileHeader.setMagic(magic);

		offset += 4;
		for(int nIndex = 0; nIndex < 2; ++nIndex)
			buff_2[nIndex] = file_header[nIndex + offset];
		short version_major = DataUtils.byteArrayToShort(buff_2);
		fileHeader.setVersionMajor(version_major);

		offset += 2;
		for(int nIndex = 0; nIndex < 2; ++nIndex)
			buff_2[nIndex] = file_header[nIndex + offset];
		short version_minor = DataUtils.byteArrayToShort(buff_2);
		fileHeader.setVersionMinor(version_minor);

		offset += 2;
		for(int nIndex = 0; nIndex < 4; ++nIndex)
			buff_4[nIndex] = file_header[nIndex + offset];
		int timezone = DataUtils.byteArrayToInt(buff_4);
		fileHeader.setTimezone(timezone);

		offset += 4;
		for(int nIndex = 0; nIndex < 4; ++nIndex)
			buff_4[nIndex] = file_header[nIndex + offset];
		int sigflags = DataUtils.byteArrayToInt(buff_4);
		fileHeader.setSigflags(sigflags);

		offset += 4;
		for(int nIndex = 0; nIndex < 4; ++nIndex)
			buff_4[nIndex] = file_header[nIndex + offset];
		int snaplen = DataUtils.byteArrayToInt(buff_4);
		fileHeader.setSnaplen(snaplen);

		offset += 4;
		for(int nIndex = 0; nIndex < 4; ++nIndex)
			buff_4[nIndex] = file_header[nIndex + offset];
		DataUtils.reverseByteArray(buff_4);
		int linktype = DataUtils.byteArrayToInt(buff_4);
		fileHeader.setLinktype(linktype);

		return fileHeader;
	}

	//鐟欙絾鐎介弫鐗堝祦閸栧懎銇�
	public PcapPacketHeader parsePacketHeader(byte[] packet_header){
		byte[] buff_4 = new byte[4];
		PcapPacketHeader packetHeader = new PcapPacketHeader();

		int offset = 0;
		for(int nIndex = 0; nIndex < 4; ++nIndex)
			buff_4[nIndex] = packet_header[nIndex + offset];
		int timeSecond = DataUtils.byteArrayToInt(buff_4);
		packetHeader.setTimeSecond(timeSecond);

		offset += 4;
		for(int nIndex = 0; nIndex < 4; ++nIndex)
			buff_4[nIndex] = packet_header[nIndex + offset];
		int timeMicroSecond = DataUtils.byteArrayToInt(buff_4);
		packetHeader.setTimeMicroSecond(timeMicroSecond);

		offset += 4;
		for(int nIndex = 0; nIndex < 4; ++nIndex)
			buff_4[nIndex] = packet_header[nIndex + offset];
		DataUtils.reverseByteArray(buff_4);
		int caplen = DataUtils.byteArrayToInt(buff_4);
		packetHeader.setCaplen(caplen);

		offset += 4;
		for(int nIndex = 0; nIndex < 4; ++nIndex)
			buff_4[nIndex] = packet_header[nIndex + offset];
		DataUtils.reverseByteArray(buff_4);
		int len = DataUtils.byteArrayToInt(buff_4);
		packetHeader.setLen(len);

		return packetHeader;
	}

	//parse protocol
	private boolean parseContent(){
		if(LinkType.CISCOHDLC == struct.getFileHeader().getLinktype()){
			ProtocolType type = ParseCiscoHDLC.protocolType(content);
			if(type == ProtocolType.IP){
				IPPacketHeader ip = ParseIP.parse(content, 4, isBigEnd);
				System.out.println(ParseIP.getIPString(ip.getSrcIP()) + " " + ParseIP.getIPString(ip.getDesIP()));
				if(ProtocolNum.TCP == DataUtil.byteToInt(ip.getProtocol())){
					TCPPacketHeader tcp = ParseTCP.parse(content, 24, isBigEnd);
					System.out.println(ParseTCP.getDecimalPort(tcp.getSrcPort()) + " " + ParseTCP.getDecimalPort(tcp.getDesPort()));
				}
				if(ProtocolNum.UDP == DataUtil.byteToInt(ip.getProtocol())){
					UDPPacketHeader udp = ParseUDP.parse(content, 24, isBigEnd);
					System.out.println(ParseUDP.getDecimalPort(udp.getSrcPort()) + " " + ParseUDP.getDecimalPort(udp.getDesPort()));
				}
			}
		}
		return false;
	}
	
	public int getLinkType(int linktype){
		switch(linktype){
			case 104:
				return LinkType.CISCOHDLC;
			default:
				return LinkType.OTHER;
		}
	}
}