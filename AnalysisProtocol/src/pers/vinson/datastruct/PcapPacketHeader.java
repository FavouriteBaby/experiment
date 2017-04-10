package pers.vinson.datastruct;

public class PcapPacketHeader{
	private int timeSecond;			//数据包捕获的时间，精确到秒，4B
	private int timeMicroSecond;	//数据包捕获的时间，精确到微秒，4B
	private int caplen;				//抓获的数据包保存在pcap文件中的长度，4B
	private int len;				//数据包实际长度，如果文件中保存的不是完整的数据包，此值可能比caplen大，4B

	public String toString(){
		return "PacpPacketHeader [timeSecond=" + timeSecond
		+ ", timeMicroSecond=" + timeMicroSecond
		+ ", caplen=" + caplen
		+ ", len=" + len
		+ "]"; 
	}

	public int getTimeSecond(){
		return timeSecond;
	}
	public void setTimeSecond(int timeSecond){
		this.timeSecond = timeSecond;
	}

	public int getTimeMicroSecond(){
		return timeMicroSecond;
	}
	public void setTimeMicroSecond(int timeMicroSecond){
		this.timeMicroSecond = timeMicroSecond;
	}

	public int getCaplen(){
		return caplen;
	}
	public void setCaplen(int caplen){
		this.caplen = caplen;
	}

	public int getLen(){
		return len;
	}
	public void setLen(int len){
		this.len = len;
	}
}