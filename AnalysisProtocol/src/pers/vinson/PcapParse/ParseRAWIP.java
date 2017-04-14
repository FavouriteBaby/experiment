package pers.vinson.PcapParse;

import pers.vinson.Utils.DataUtil;
import pers.vinson.datastruct.ProtocolNum;

public class ParseRAWIP {
	public static int protocolType(byte[] content){
		return ProtocolNum.IP;
	}
}
