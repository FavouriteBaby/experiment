package pers.vinson.PcapParse;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import pers.vinson.Save.SaveFile;
import pers.vinson.Utils.DataUtil;
import pers.vinson.Utils.FileUtils;
import pers.vinson.datastruct.*;

public class PcapParser extends Observable{
	private File pcap;
	private String savePath;
	private PcapStruct struct;
	private IPPacketHeader ip;
	private TCPPacketHeader tcp;
	private UDPPacketHeader udp;
	private boolean isBigEnd;	//is big endian
	private int nBeginTime = 0;		//begin time
	private boolean isFirstFrame = true;
	private int nCount = 5;
	
	private byte[] file_header = new byte[24];	//闁哄倸娲ｅ▎銏″緞閿燂拷
	private byte[] packet_header = new byte[16];	//pcap闁轰胶澧楀畵渚�宕犻崨顓滀粓闂侇噯鎷�
	private byte[] content;
	private List<ProtocolData> protocolContext = new ArrayList<ProtocolData>();
	
	public PcapParser(File pcap){
		this.pcap = pcap;
	}

	public boolean parse(){
		boolean rs = true;
		struct = new PcapStruct();
		List<PcapPacketHeader> packetHeaders = new ArrayList<PcapPacketHeader>();
		FileInputStream file = null;
		RandomAccessFile raf = null;
		FileChannel fc = null;
		MappedByteBuffer mBuff = null;
		try{
			raf = new RandomAccessFile(pcap, "r");
			fc = raf.getChannel();
			mBuff = fc.map(MapMode.READ_ONLY, 0, fc.size());
			System.out.println("fc.size = " + fc.size());
			mBuff.get(file_header);
			
			if(mBuff.position() < fc.size()){
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
				PcapPacketHeader packetHeader = null;
				while(mBuff.position() < fc.size()){
				//while(nIndex < 20){
					//Debug
					System.out.println("=============parse " + nIndex + " times===============");
					
					mBuff.get(packet_header);
					packetHeader = ParsePacketHeader.parsePacketHeader(packet_header, isBigEnd);
					packetHeaders.add(packetHeader);
					
					if(isFirstFrame){
						nBeginTime = packetHeader.getTimeMicroSecond();
						System.out.println("Begin=" + nBeginTime);
						isFirstFrame = false;
						this.savePath = "data/test_" + nCount + ".txt";
					}
					int nEndTime = packetHeader.getTimeMicroSecond();
															
					//parse content
					content = new byte[packetHeader.getCaplen()];
					
					System.out.println("the length of content is: " + content.length);
									
					boolean isDone = parseContent(mBuff, content);
					if(isDone)
						break;
					
					nIndex++;
					if(nEndTime-nBeginTime < 0){
						isFirstFrame = true;
						nCount++;
						SaveFile.save(savePath, protocolContext);
						protocolContext.clear();
					}
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
		}finally{
			FileUtils.closeStream(file, null);
			if(raf != null){
				try{
					raf.close();
				}catch(IOException e){
					e.printStackTrace();
				}
			}
				
			if(fc != null){
				try{
					fc.close();
				}catch(IOException e){
					e.printStackTrace();
				}
			}
		}
		
		return rs;
	}

	//parse protocol
	private boolean parseContent(MappedByteBuffer mBuff, byte[] content){
		mBuff.get(content);
		if(LinkType.CISCOHDLC == struct.getFileHeader().getLinktype()){
			int nType = ParseCiscoHDLC.protocolType(content);
			if(nType == ProtocolNum.IP){
				ip = ParseIP.parse(content, GetOffset.getIPOffset(LinkType.CISCOHDLC), isBigEnd);
				if(ProtocolNum.TCP == DataUtil.byteToInt(ip.getProtocol())){
					tcp = ParseTCP.parse(content, GetOffset.getTransportLayerOffset(LinkType.RAWIP, ip.getVarHLen()), isBigEnd);
					if(null != tcp)
						saveFormat(ip.getSrcIP(), ip.getDesIP(), tcp.getSrcPort(), tcp.getDesPort(), true);
					else
						saveFormat(ip.getSrcIP(), ip.getDesIP());
				}
				if(ProtocolNum.UDP == DataUtil.byteToInt(ip.getProtocol())){
					udp = ParseUDP.parse(content, GetOffset.getTransportLayerOffset(LinkType.RAWIP, ip.getVarHLen()), isBigEnd);
					if(null != udp)
						saveFormat(ip.getSrcIP(), ip.getDesIP(), udp.getSrcPort(), udp.getDesPort(), false);
					else
						saveFormat(ip.getSrcIP(), ip.getDesIP());
				}
			}
		}
		
		if(LinkType.RAWIP == struct.getFileHeader().getLinktype()){
			System.out.println("RAW IP");
			ip = ParseIP.parse(content, GetOffset.getIPOffset(LinkType.RAWIP), isBigEnd);
			
			if(ProtocolNum.TCP == DataUtil.byteToInt(ip.getProtocol())){
				tcp = ParseTCP.parse(content, GetOffset.getTransportLayerOffset(LinkType.RAWIP, ip.getVarHLen()), isBigEnd);
				if(null != tcp)
					saveFormat(ip.getSrcIP(), ip.getDesIP(), tcp.getSrcPort(), tcp.getDesPort(), true);
				else
					saveFormat(ip.getSrcIP(), ip.getDesIP());
			}
			if(ProtocolNum.UDP == DataUtil.byteToInt(ip.getProtocol())){
				udp = ParseUDP.parse(content, GetOffset.getTransportLayerOffset(LinkType.RAWIP, ip.getVarHLen()), isBigEnd);
				if(null != udp)
					saveFormat(ip.getSrcIP(), ip.getDesIP(), udp.getSrcPort(), udp.getDesPort(), false);
				else
					saveFormat(ip.getSrcIP(), ip.getDesIP());
			}
		}
		
/*		try{
			SaveFile.save(savePath, protocolContext);
		}catch(IOException e){
			e.printStackTrace();
		}*/
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
		ProtocolData protocolData = new ProtocolData();
		protocolData.setSrcIP(ParseIP.getIPString(srcIP));
		protocolData.setDesIP(ParseIP.getIPString(desIP));
		if(isTCP){
			protocolData.setSrcPort(Integer.toString(ParseTCP.getDecimalPort(srcPort)));
			protocolData.setDesPort(Integer.toString(ParseTCP.getDecimalPort(desPort)));
		}else{
			protocolData.setSrcPort(Integer.toString(ParseUDP.getDecimalPort(srcPort)));
			protocolData.setDesPort(Integer.toString(ParseUDP.getDecimalPort(desPort)));
		}
		protocolContext.add(protocolData);
	}
	
	private void saveFormat(int srcIP, int desIP){
		ProtocolData protocolData = new ProtocolData();
		protocolData.setSrcIP(ParseIP.getIPString(srcIP));
		protocolData.setDesIP(ParseIP.getIPString(desIP));
		protocolContext.add(protocolData);
	}
}