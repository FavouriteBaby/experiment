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
	private int nBeginTime = 0;		//begin time
	private boolean isFirstFrame = true;

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
			file = new FileInputStream(pcap);
			int m = file.read(file_header);

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
					PcapPacketHeader packetHeader = ParsePacketHeader.parsePacketHeader(packet_header, isBigEnd);
					packetHeaders.add(packetHeader);
					
					if(isFirstFrame){
						nBeginTime = packetHeader.getTimeMicroSecond();
						System.out.println("Begin=" + nBeginTime);
						isFirstFrame = false;
					}
					int nEndTime = packetHeader.getTimeMicroSecond();
															
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
					if(nEndTime-nBeginTime == 1000000){
						m = -1;
						isFirstFrame = true;
					}
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

	//parse protocol
	private boolean parseContent(){
		if(LinkType.CISCOHDLC == struct.getFileHeader().getLinktype()){
			int nType = ParseCiscoHDLC.protocolType(content);
			if(nType == ProtocolNum.IP){
				IPPacketHeader ip = ParseIP.parse(content, GetOffset.getIPOffset(LinkType.CISCOHDLC), isBigEnd);
				if(ProtocolNum.TCP == DataUtil.byteToInt(ip.getProtocol())){
					TCPPacketHeader tcp = ParseTCP.parse(content, GetOffset.getTransportLayerOffset(LinkType.RAWIP, ip.getVarHLen()), isBigEnd);
					if(null != tcp)
						saveFormat(ip.getSrcIP(), ip.getDesIP(), tcp.getSrcPort(), tcp.getDesPort(), true);
					else
						saveFormat(ip.getSrcIP(), ip.getDesIP());
				}
				if(ProtocolNum.UDP == DataUtil.byteToInt(ip.getProtocol())){
					UDPPacketHeader udp = ParseUDP.parse(content, GetOffset.getTransportLayerOffset(LinkType.RAWIP, ip.getVarHLen()), isBigEnd);
					if(null != udp)
						saveFormat(ip.getSrcIP(), ip.getDesIP(), udp.getSrcPort(), udp.getDesPort(), false);
					else
						saveFormat(ip.getSrcIP(), ip.getDesIP());
				}
			}
		}
		
		if(LinkType.RAWIP == struct.getFileHeader().getLinktype()){
			System.out.println("RAW IP");
			IPPacketHeader ip = ParseIP.parse(content, GetOffset.getIPOffset(LinkType.RAWIP), isBigEnd);
			
			if(ProtocolNum.TCP == DataUtil.byteToInt(ip.getProtocol())){
				TCPPacketHeader tcp = ParseTCP.parse(content, GetOffset.getTransportLayerOffset(LinkType.RAWIP, ip.getVarHLen()), isBigEnd);
				if(null != tcp)
					saveFormat(ip.getSrcIP(), ip.getDesIP(), tcp.getSrcPort(), tcp.getDesPort(), true);
				else
					saveFormat(ip.getSrcIP(), ip.getDesIP());
			}
			if(ProtocolNum.UDP == DataUtil.byteToInt(ip.getProtocol())){
				UDPPacketHeader udp = ParseUDP.parse(content, GetOffset.getTransportLayerOffset(LinkType.RAWIP, ip.getVarHLen()), isBigEnd);
				if(null != udp)
					saveFormat(ip.getSrcIP(), ip.getDesIP(), udp.getSrcPort(), udp.getDesPort(), false);
				else
					saveFormat(ip.getSrcIP(), ip.getDesIP());
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
	
	private void saveFormat(int srcIP, int desIP){
		ProtocolData proContext = new ProtocolData();
		proContext.setSrcIP(ParseIP.getIPString(srcIP));
		proContext.setDesIP(ParseIP.getIPString(desIP));
		protocolContext.add(proContext);
	}
}