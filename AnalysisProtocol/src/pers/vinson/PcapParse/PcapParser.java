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

	private byte[] file_header = new byte[24];	//文件头
	private byte[] packet_header = new byte[16];	//pcap数据包头部
	private byte[] content;

	//构造方法
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
			//读取文件
			file = new FileInputStream(pcap);
			int m = file.read(file_header);		//读取file_header长度的数据，并存放到file_header

			if(m > 0){
				//解析pcap文件头
				PcapFileHeader fileHeader = parseFileHeader(file_header);
				if (null == fileHeader) 
					System.out.println("fileHeader is null");
				struct.setFileHeader(fileHeader);
				fileHeader.toString();

				while(m > 0){
					//读取pcap数据包头
					m = file.read(packet_header);
					PcapPacketHeader packetHeader = parsePacketHeader(packet_header);
					packetHeaders.add(packetHeader);
					content = new byte[packetHeader.getCaplen()];	//content为协议及数据
					System.out.println("content的长度："+content.length);
					content = new byte[packetHeader.getCaplen()];
					m = file.read(content);
					
					protocolData = new ProtocolData();
					//读取协议类型
					boolean isDone = parseContent();
					//Debug
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

	//解析pcap文件头
	public PcapFileHeader parseFileHeader(byte[] file_header) throws IOException{
		PcapFileHeader fileHeader = new PcapFileHeader();
		byte[] buff_4 = new byte[4];		//4字节的数组
		byte[] buff_2 = new byte[2];		//2字节的数组

		int offset = 0;						//位移
		for(int nIndex = 0; nIndex < 4; ++nIndex){
			//读取前4个字节，即文件标识
			buff_4[nIndex] = file_header[nIndex + offset];
		}
		
		int magic = DataUtils.byteArrayToInt(buff_4);	//pcap文件标识
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

	//解析数据包头
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
		//先逆序再转为int
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

	//解析数据
	private boolean parseContent(){
		System.out.println(struct.getFileHeader().getLinktype());
		if(LinkType.CISCOHDLC == struct.getFileHeader().getLinktype()){
			System.out.println("ciscohdlc");
			ProtocolType type = ParseCiscoHDLC.protocolType(content);
			if(type == ProtocolType.IP){
				System.out.println("ip");
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