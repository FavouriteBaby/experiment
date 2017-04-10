package pers.vinson.PcapParse;

import pers.vinson.Utils.DataUtil;
import pers.vinson.datastruct.IPPacketHeader;

public class ParseIP {
	
	public static IPPacketHeader parse(byte[] content, int offset){
		IPPacketHeader ip = new IPPacketHeader();
		byte[] buff_4 = new byte[4];
		byte[] buff_2 = new byte[2];
		
		byte varHLen = content[offset++];
		if(0 == varHLen)
			return null;
		ip.setVarHLen(varHLen);
		
		byte tos = content[offset++];
		ip.setTos(tos);
		
		for(int nIndex = 0; nIndex < 2; ++nIndex)
			buff_2[nIndex] = content[nIndex + offset];
		short totalLen = DataUtil.byteArrayToShort(buff_2); 
		ip.setTotalLen(totalLen);
		offset += 2;
		
		return ip;
	}
}
