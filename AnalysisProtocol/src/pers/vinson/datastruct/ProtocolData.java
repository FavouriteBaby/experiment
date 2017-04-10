package pers.vinson.datastruct;

public class ProtocolData{
	String srcIP;
	String desIP;
	String srcPort;
	String desPort;
	ProtocolType protocolType = ProtocolType.OTHER;

	public ProtocolData(){}

	public ProtocolData(String srcIP, String desIP, 
		String srcPort, String desPort, ProtocolType protocolType){

	}

	public String getSrcIP(){
		return srcIP;
	}
	public void setSrcIP(String srcIP){
		this.srcIP = srcIP;
	}

	public String getDesIP(){
		return desIP;
	}
	public void setDesIP(String desIP){
		this.desIP = desIP;
	}

	public String getSrcPort(){
		return srcPort;
	}
	public void setSrcPort(String srcPort){
		this.srcPort = srcPort;
	}

	public String getDesPort(){
		return desPort;
	}
	public void setDesPort(String desPort){
		this.desPort = desPort;
	}

	public ProtocolType getProtocolType(){
		return protocolType;
	}
	public void setProtocolType(ProtocolType protocolType){
		this.protocolType = protocolType;
	}

	public String toString(){
		return "ProtocolData [srcIP=" + srcIP
			+ ", desIP=" + desIP
			+ ", srcPort=" + srcPort
			+ ", desPort=" + desPort
			+ ", protocolType=" + protocolType
			+ "]";
	}
}