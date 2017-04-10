package pers.vinson.PcapParse;

import pers.vinson.Utils.DataUtil;
import pers.vinson.datastruct.ProtocolType;

public class ParseCiscoHDLC {
	public ParseCiscoHDLC(){}
	
	public static int offset = 0;
	
	public static ProtocolType protocolType(byte[] content){
		byte[] buff_2 = new byte[2];
		offset += 2;
		for(int nIndex = 0; nIndex < 2; ++nIndex)
			buff_2[nIndex] = content[nIndex + offset];
		
		DataUtil.reverseByteArray(buff_2);
		short nType = DataUtil.byteArrayToShort(buff_2);
		System.out.println(nType);
		if(nType == 8){
			return ProtocolType.IP;
		}
		
		return ProtocolType.OTHER;
	}
}
