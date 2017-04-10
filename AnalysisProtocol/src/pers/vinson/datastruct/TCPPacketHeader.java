package pers.vinson.datastruct;

public class TCPPacketHeader{
	private short srcPort;			//源端口
	private short desPort;			//目的端口
	private int seqNum;				//序号
	private int ackNum;				//确认序号
	private byte headerLen;			//首部长度4bit，保留位4bit
	private byte flags;				//保留位2bit，标志位6bit
	private short window;			//窗口大小，2B
	private short checkSum;			//检验和，2B
	private short urgentPointer;	//紧急指针，2B

	public TCPPacketHeader(){}

	public String toString(){
		return "TCPPacketHeader [srcPort=" + srcPort
			+ ", desPort=" + desPort
			+ ", seqNum=" + seqNum
			+ ", ackNum=" + ackNum
			+ ", headerLen=" + headerLen
			+ ", flags=" + flags
			+ ", window=" + window
			+ ", checkSum=" + checkSum
			+ ", urgentPointer=" + urgentPointer
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

	public int getSeqNum(){
		return seqNum;
	}
	public void setSeqNum(int seqNum){
		this.seqNum = seqNum;
	}

	public int getAckNum(){
		return ackNum;
	}
	public void setAckNum(int ackNum){
		this.ackNum = ackNum;
	}

	public byte getHeaderLen(){
		return headerLen;
	}
	public void setHeaderLen(byte headerLen){
		this.headerLen = headerLen;
	}

	public byte getFlags(){
		return flags;
	}
	public void setFlags(byte flags){
		this.flags = flags;
	}

	public short getWindow(){
		return window;
	}
	public void setWindow(short window){
		this.window = window;
	}

	public short getCheckSum(){
		return checkSum;
	}
	public void setCheckSum(short checkSum){
		this.checkSum = checkSum;
	}

	public short getUrgentPointer(){
		return urgentPointer;
	}
	public void setUrgentPointer(short urgentPointer){
		this.urgentPointer = urgentPointer;
	}
}