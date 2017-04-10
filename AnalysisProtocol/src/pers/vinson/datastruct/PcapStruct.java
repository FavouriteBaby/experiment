package pers.vinson.datastruct;

import java.util.List;

public class PcapStruct{
	private PcapFileHeader fileHeader;
	private List<PcapPacketHeader> packetHeader;

	public PcapFileHeader getFileHeader(){
		return fileHeader;
	}
	public void setFileHeader(PcapFileHeader fileHeader){
		this.fileHeader = fileHeader;
	}

	public List<PcapPacketHeader> getPacketHeader(){
		return packetHeader;
	}
	public void setPacketHeader(List<PcapPacketHeader> packetHeader){
		this.packetHeader = packetHeader;
	}

	public PcapStruct(){}
}