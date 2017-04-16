package pers.vinson.PcapParse;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import pers.vinson.Save.SaveFile;
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

	private byte[] file_header = new byte[24];	//闁哄倸娲ｅ▎銏″緞閿燂拷
	private byte[] packet_header = new byte[16];	//pcap闁轰胶澧楀畵渚�宕犻崨顓滀粓闂侇噯鎷�
	private byte[] content;
	private List<ProtocolData> protocolContext = new ArrayList<ProtocolData>();

	//闁哄瀚伴敓鐣屽У閺岀喎鈻旈敓锟�
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
			//閻犲洩顕цぐ鍥棘閸ワ附顐�
			file = new FileInputStream(pcap);
			int m = file.read(file_header);		//閻犲洩顕цぐ鍣乮le_header闂傦拷閸喖顔婇柣銊ュ閺嗙喖骞戦鍡欑妤犵偠娉涢悺銊╁绩閹冪厒file_header

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

	//閻熸瑱绲鹃悗绲ap闁哄倸娲ｅ▎銏″緞閿燂拷
	public PcapFileHeader parseFileHeader(byte[] file_header) throws IOException{
		PcapFileHeader fileHeader = new PcapFileHeader();
		byte[] buff_4 = new byte[4];		//4閻庢稒顨夋俊顓㈡儍閸曨剚娈剁紓渚婃嫹
		byte[] buff_2 = new byte[2];		//2閻庢稒顨夋俊顓㈡儍閸曨剚娈剁紓渚婃嫹

		int offset = 0;						//濞达絽绉朵簺
		for(int nIndex = 0; nIndex < 4; ++nIndex){
			//閻犲洩顕цぐ鍥礈閿燂拷4濞戞搩浜滈悺褔鎳為崒锔剧闁告娅曢弸鍐╃閼哥數鍨奸悹鍥锋嫹
			buff_4[nIndex] = file_header[nIndex + offset];
		}
		
		int magic = DataUtils.byteArrayToInt(buff_4);	//pcap闁哄倸娲ｅ▎銏ゅ冀閸ヮ亞妲�
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

	//閻熸瑱绲鹃悗浠嬪极閻楀牆绁﹂柛鏍ф噹閵囷拷
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
			int nType = ParseCiscoHDLC.protocolType(content);
			if(nType == ProtocolNum.IP){
				IPPacketHeader ip = ParseIP.parse(content, GetOffset.getIPOffset(LinkType.CISCOHDLC), isBigEnd);
				if(ProtocolNum.TCP == DataUtil.byteToInt(ip.getProtocol())){
					TCPPacketHeader tcp = ParseTCP.parse(content, GetOffset.getTransportLayerOffset(LinkType.RAWIP, ip.getVarHLen()), isBigEnd);
					saveFormat(ip.getSrcIP(), ip.getDesIP(), tcp.getSrcPort(), tcp.getDesPort(), true);
				}
				if(ProtocolNum.UDP == DataUtil.byteToInt(ip.getProtocol())){
					UDPPacketHeader udp = ParseUDP.parse(content, GetOffset.getTransportLayerOffset(LinkType.RAWIP, ip.getVarHLen()), isBigEnd);
					saveFormat(ip.getSrcIP(), ip.getDesIP(), udp.getSrcPort(), udp.getDesPort(), false);
				}
			}
		}
		
		if(LinkType.RAWIP == struct.getFileHeader().getLinktype()){
			System.out.println("RAW IP");
			IPPacketHeader ip = ParseIP.parse(content, GetOffset.getIPOffset(LinkType.RAWIP), isBigEnd);
			
			if(ProtocolNum.TCP == DataUtil.byteToInt(ip.getProtocol())){
				TCPPacketHeader tcp = ParseTCP.parse(content, GetOffset.getTransportLayerOffset(LinkType.RAWIP, ip.getVarHLen()), isBigEnd);
				saveFormat(ip.getSrcIP(), ip.getDesIP(), tcp.getSrcPort(), tcp.getDesPort(), true);
			}
			if(ProtocolNum.UDP == DataUtil.byteToInt(ip.getProtocol())){
				UDPPacketHeader udp = ParseUDP.parse(content, GetOffset.getTransportLayerOffset(LinkType.RAWIP, ip.getVarHLen()), isBigEnd);
				saveFormat(ip.getSrcIP(), ip.getDesIP(), udp.getSrcPort(), udp.getDesPort(), false);
			}
		}
		
		try{
			SaveFile.save(savePath, protocolContext);
		}catch(IOException e){
			e.printStackTrace();
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
	
	private void saveFormat(int srcIP, int desIP, short srcPort, short desPort, boolean isTCP){
		ProtocolData proContext = new ProtocolData();
		proContext.setSrcIP(ParseIP.getIPString(srcIP));
		proContext.setDesIP(ParseIP.getIPString(desIP));
		if(isTCP){
			proContext.setSrcPort(Integer.toString(ParseTCP.getDecimalPort(srcPort)));
			proContext.setDesPort(Integer.toString(ParseTCP.getDecimalPort(desPort)));
		}else{
			proContext.setSrcPort(Integer.toString(ParseUDP.getDecimalPort(srcPort)));
			proContext.setDesPort(Integer.toString(ParseUDP.getDecimalPort(desPort)));
		}
		protocolContext.add(proContext);
	}
}