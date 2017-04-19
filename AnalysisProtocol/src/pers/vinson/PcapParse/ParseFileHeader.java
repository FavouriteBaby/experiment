package pers.vinson.PcapParse;

import java.io.IOException;

import pers.vinson.Utils.DataUtils;
import pers.vinson.datastruct.PcapFileHeader;

public class ParseFileHeader {
	//瑙ｆ瀽pcap鏂囦欢澶�
	public static PcapFileHeader parseFileHeader(byte[] file_header, int offset) throws IOException{
		PcapFileHeader fileHeader = new PcapFileHeader();
		byte[] buff_4 = new byte[4];		//4瀛楄妭鐨勬暟缁�
		byte[] buff_2 = new byte[2];		//2瀛楄妭鐨勬暟缁�

		for(int nIndex = 0; nIndex < 4; ++nIndex){
			//璇诲彇鍓�4涓瓧鑺傦紝鍗虫枃浠舵爣璇�
			buff_4[nIndex] = file_header[nIndex + offset];
		}
		
		int magic = DataUtils.byteArrayToInt(buff_4);	//pcap鏂囦欢鏍囪瘑
		fileHeader.setMagic(magic);

		offset += 4;
		for(int nIndex = 0; nIndex < 2; ++nIndex)
			buff_2[nIndex] = file_header[nIndex + offset];
		short version_major = DataUtils.byteArrayToShort(buff_2);
		fileHeader.setVersionMajor(version_major);

		offset += 2;
		for(int nIndex = 0; nIndex < 2; ++nIndex)
			buff_2[nIndex] = file_header[nIndex + offset];
		short version_minor = DataUtils.byteArrayToShort(buff_2);
		fileHeader.setVersionMinor(version_minor);

		offset += 2;
		for(int nIndex = 0; nIndex < 4; ++nIndex)
			buff_4[nIndex] = file_header[nIndex + offset];
		int timezone = DataUtils.byteArrayToInt(buff_4);
		fileHeader.setTimezone(timezone);

		offset += 4;
		for(int nIndex = 0; nIndex < 4; ++nIndex)
			buff_4[nIndex] = file_header[nIndex + offset];
		int sigflags = DataUtils.byteArrayToInt(buff_4);
		fileHeader.setSigflags(sigflags);

		offset += 4;
		for(int nIndex = 0; nIndex < 4; ++nIndex)
			buff_4[nIndex] = file_header[nIndex + offset];
		int snaplen = DataUtils.byteArrayToInt(buff_4);
		fileHeader.setSnaplen(snaplen);

		offset += 4;
		for(int nIndex = 0; nIndex < 4; ++nIndex)
			buff_4[nIndex] = file_header[nIndex + offset];
		DataUtils.reverseByteArray(buff_4);
		int linktype = DataUtils.byteArrayToInt(buff_4);
		fileHeader.setLinktype(linktype);

		return fileHeader;
	}
	
	//鏄惁涓哄ぇ绔舰寮忓瓨鍌�
	public static boolean isBigEnd(PcapFileHeader fileHeader){
		int magic = fileHeader.getMagic();
		if(Integer.toHexString(magic).equals("d4c3b2a1"))
			return true;
		return false;
	}
	
}
