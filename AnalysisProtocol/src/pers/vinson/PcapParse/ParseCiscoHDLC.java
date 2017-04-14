package pers.vinson.PcapParse;

import pers.vinson.Utils.DataUtil;
import pers.vinson.datastruct.ProtocolNum;

public class ParseCiscoHDLC extends GetOffset{
	public ParseCiscoHDLC(){}
	
	public static int protocolType(byte[] content){
		int offset = 0;
		byte[] buff_2 = new byte[2];
		offset += 2;
		for(int nIndex = 0; nIndex < 2; ++nIndex)
			buff_2[nIndex] = content[nIndex + offset];
		
		DataUtil.reverseByteArray(buff_2);
		short nType = DataUtil.byteArrayToShort(buff_2);
		if(nType == 8)
			return ProtocolNum.IP;
		return ProtocolNum.OTHER;
	}
}
