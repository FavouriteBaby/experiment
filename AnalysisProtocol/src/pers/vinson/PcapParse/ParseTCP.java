package pers.vinson.PcapParse;

import pers.vinson.Utils.DataUtil;
import pers.vinson.datastruct.TCPPacketHeader;

public class ParseTCP {
	public static TCPPacketHeader parse(byte[] content, int offset, boolean isBigEnd){
		TCPPacketHeader tcp = new TCPPacketHeader();
		byte[] buff_2 = new byte[2];
		byte[] buff_4 = new byte[4];

		for(int nIndex = 0; nIndex < 2; ++nIndex)
			buff_2[nIndex] = content[nIndex + offset];
		short srcPort = DataUtil.byteArrayToShort(buff_2);
		System.out.println("srcPort: " + DataUtil.shortToHexString(srcPort));
		tcp.setSrcPort(srcPort);

		offset += 2;
		for(int nIndex = 0; nIndex < 2; ++nIndex)
			buff_2[nIndex] = content[nIndex + offset];
		short desPort = DataUtil.byteArrayToShort(buff_2);
		tcp.setDesPort(desPort);

		offset += 2;
		for(int nIndex = 0; nIndex < 4; ++nIndex)
			buff_4[nIndex] = content[nIndex + offset];
		int seqNum = DataUtil.byteArrayToInt(buff_4);
		tcp.setSeqNum(seqNum);

		offset += 4;
		for(int nIndex = 0; nIndex < 4; ++nIndex)
			buff_4[nIndex] = content[nIndex + offset];
		int ackNum = DataUtil.byteArrayToInt(buff_4);
		tcp.setAckNum(ackNum);

		offset += 4;
		byte headerLen = content[offset++];
		tcp.setHeaderLen(headerLen);

		byte flags = content[offset++];
		tcp.setFlags(flags);

		for(int nIndex = 0; nIndex < 2; ++nIndex)
			buff_2[nIndex] = content[nIndex + offset];
		short window = DataUtil.byteArrayToShort(buff_2);
		tcp.setWindow(window);

		offset += 2;
		for(int nIndex = 0; nIndex < 2; ++nIndex)
			buff_2[nIndex] = content[nIndex + offset];
		short checkSum = DataUtil.byteArrayToShort(buff_2);
		tcp.setCheckSum(checkSum);

		offset += 2;
		for(int nIndex = 0; nIndex < 2; ++nIndex)
			buff_2[nIndex] = content[nIndex + offset];
		short urgentPointer = DataUtil.byteArrayToShort(buff_2);
		tcp.setUrgentPointer(urgentPointer);
		return tcp;
	}
	
	public static int getDecimalPort(short port){
		int nPort = (int)port;
		if(nPort < 0)
			return 65536+nPort;
		return nPort;
	}
}
