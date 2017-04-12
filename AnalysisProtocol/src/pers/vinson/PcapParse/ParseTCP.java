package pers.vinson.PcapParse;

import pers.vinson.datastruct.TCPPacketHeader;

public class ParseTCP {
	public static TCPPacketHeader parse(byte[] content, int offset, boolean isBigEnd){
		TCPPacketHeader tcp = new TCPPacketHeader();
		byte[] buff_2 = new byte[2];
		byte[] buff_4 = new byte[4];
		return tcp;
	}
}
