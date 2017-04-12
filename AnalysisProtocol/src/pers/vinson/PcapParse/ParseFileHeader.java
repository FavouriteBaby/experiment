package pers.vinson.PcapParse;

import java.io.IOException;

import pers.vinson.Utils.DataUtils;
import pers.vinson.datastruct.PcapFileHeader;

public class ParseFileHeader {
	//解析pcap文件头
	public static PcapFileHeader parseFileHeader(byte[] file_header, int offset) throws IOException{
		PcapFileHeader fileHeader = new PcapFileHeader();
		byte[] buff_4 = new byte[4];		//4字节的数组
		byte[] buff_2 = new byte[2];		//2字节的数组

		for(int nIndex = 0; nIndex < 4; ++nIndex){
			//读取前4个字节，即文件标识
			buff_4[nIndex] = file_header[nIndex + offset];
		}
		
		int magic = DataUtils.byteArrayToInt(buff_4);	//pcap文件标识
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
	
	//是否为大端形式存储
	public static boolean isBigEnd(PcapFileHeader fileHeader){
		int magic = fileHeader.getMagic();
		if(Integer.toHexString(magic).equals("d4c3b2a1"))
			return false;
		return true;
	}
	
}
