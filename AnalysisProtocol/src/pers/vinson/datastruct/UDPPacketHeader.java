package pers.vinson.datastruct;

import pers.vinson.Utils.DataUtils;

public class UDPPacketHeader{
	private short srcPort;		//源端口，2B
	private short desPort;		//目的端口，2B
	private short length;		//UDP长度，2B
	private short checkSum;		//UDP检验和，2B

	public UDPPacketHeader(){}

	public String toString(){
		return "UDPPacketHeader: [ scrPort=" + srcPort
			+ ", desPort=" + desPort
			+ ", length=" + length
			+ ", checkSum=" + DataUtils.shortToHexString(checkSum)
			+ "]";
	}

	public short getSrcPort(){
		return srcPort;
	}
	public void setSrcPort(short srcPort){
		this.srcPort = srcPort;
	}

	public short getDesPort(){
		return desPort;
	}
	public void setDesPort(short desPort){
		this.desPort = desPort;
	}

	public short getLength(){
		return length;
	}
	public void setLength(short length){
		this.length = length;
	}

	public short getCheckSum(){
		return checkSum;
	}
	public void setCheckSum(short checkSum){
		this.checkSum = checkSum;
	}
}