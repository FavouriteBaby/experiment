package pers.vinson.PcapParse;

import pers.vinson.Utils.DataUtil;
import pers.vinson.datastruct.UDPPacketHeader;

public class ParseUDP {
	public static UDPPacketHeader parse(byte[] content, int offset, boolean isBigEnd){
		UDPPacketHeader udp = new UDPPacketHeader();
		byte[] buff_2 = new byte[2];
		
		try{
			for(int nIndex = 0; nIndex < 2; ++nIndex)
				buff_2[nIndex] = content[nIndex + offset];
			short srcPort = DataUtil.byteArrayToShort(buff_2);
			udp.setSrcPort(srcPort);
			
			offset += 2;
			for(int nIndex = 0; nIndex < 2; ++nIndex)
				buff_2[nIndex] = content[nIndex + offset];
			short desPort = DataUtil.byteArrayToShort(buff_2);
			udp.setDesPort(desPort);
			
			offset += 2;
			for(int nIndex = 0; nIndex < 2; ++nIndex)
				buff_2[nIndex] = content[nIndex + offset];
			short length = DataUtil.byteArrayToShort(buff_2);
			udp.setLength(length);
			
			offset += 2;
			for(int nIndex = 0; nIndex < 2; ++nIndex)
				buff_2[nIndex] = content[nIndex + offset];
			short checkSum = DataUtil.byteArrayToShort(buff_2);
			udp.setCheckSum(checkSum);
		}catch(ArrayIndexOutOfBoundsException e){
			e.printStackTrace();
			return null;
		}
		
		return udp;
	}
	
	//get decimal format of port
	public static int getDecimalPort(short port){
		int nPort = (int)port;
		if(nPort < 0)
			return 65536+nPort;
		return nPort;
	}
}
