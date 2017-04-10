package pers.vinson.datastruct;
import pers.vinson.Utils.*;

public class IPPacketHeader{
	private byte varHLen;	//版本和首部长度，1B
	private byte tos;		//服务类型，1B
	private short totalLen;	//总长度，2B
	private short id;		//标识，2B
	private short flagSegment;	//标志，3bit、片偏移，13bit
	private byte ttl;			//生存时间，1B
	private byte protocol;		//协议，1B
	private short checkSum;		//首部检验和，2B
	private int srcIP;			//源IP地址，4B
	private int desIP;			//目的IP地址，4B

	public String toString(){
		return "IPHeader [varHLen=" + DataUtils.byteToHexString(varHLen)
				+ ", tos=" + DataUtils.byteToHexString(tos)
				+ ", totalLen" + totalLen
				+ ", id=" + DataUtils.shortToHexString(id)
				+ ", flagSegment" + DataUtils.shortToHexString(flagSegment)
				+ ", ttl=" + ttl
				+ ", protocol" + protocol
				+ ", checkSum" + checkSum
				+ ", srcIP" + DataUtils.intToHexString(srcIP)
				+ ", desIP" + DataUtils.intToHexString(desIP)
				+ "]";
	}

	public IPPacketHeader() {}

	public byte getVarHLen(){
		return varHLen;
	}
	public void setVarHLen(byte varHLen){
		this.varHLen = varHLen;
	}

	public byte getTos(){
		return tos;
	}
	public void setTos(byte tos){
		this.tos = tos;
	}

	public short getTotalLen(){
		return totalLen;
	}
	public void setTotalLen(short totalLen){
		this.totalLen = totalLen;
	}

	public short getId(){
		return id;
	}
	public void setId(short id){
		this.id = id;
	}

	public short getFlagSegment(){
		return flagSegment;
	}
	public void setFlagSegment(short flagSegment){
		this.flagSegment = flagSegment;
	}

	public byte getTTL(){
		return ttl;
	}
	public void setTTL(byte ttl){
		this.ttl = ttl;
	}

	public byte getProtocol(){
		return protocol;
	}
	public void setProtocol(byte protocol){
		this.protocol = protocol;
	}

	public short getCheckSum(){
		return checkSum;
	}
	public void setCheckSum(short checkSum){
		this.checkSum = checkSum;
	}

	public int getSrcIP(){
		return srcIP;
	}
	public void setSrcIP(int srcIP){
		this.srcIP = srcIP;
	}

	public int getDesIP(){
		return desIP;
	}
	public void setDesIP(int desIP){
		this.desIP = desIP;
	}
}