package pers.vinson.PcapParse;

import javax.xml.crypto.Data;
import pers.vinson.Utils.DataUtil;
import pers.vinson.datastruct.IPPacketHeader;

public class ParseIP {
	public static IPPacketHeader parse(byte[] content, int offset, boolean isBigEnd){
		IPPacketHeader ip = new IPPacketHeader();
		byte[] buff_4 = new byte[4];
		byte[] buff_2 = new byte[2];
		
		//版本号和首部长度
		byte varHLen = content[offset++];
		if(0 == varHLen)
			return null;
		ip.setVarHLen(varHLen);
		
		//服务类型
		byte tos = content[offset++];
		ip.setTos(tos);
		
		//总长度
		for(int nIndex = 0; nIndex < 2; ++nIndex)
			buff_2[nIndex] = content[nIndex + offset];
		short totalLen = DataUtil.byteArrayToShort(buff_2);
		ip.setTotalLen(totalLen);
		
		//标识
		offset += 2;
		for(int nIndex = 0; nIndex < 2; ++nIndex)
			buff_2[nIndex] = content[nIndex + offset];
		short id = DataUtil.byteArrayToShort(buff_2);
		ip.setId(id);

		//标志和13位片偏移
		offset += 2;
		for(int nIndex = 0; nIndex < 2; ++nIndex)
			buff_2[nIndex] = content[nIndex + offset];
		short flagSegment = DataUtil.byteArrayToShort(buff_2);
		ip.setFlagSegment(flagSegment);

		//生存时间
		offset += 2;
		byte ttl = content[offset++];
		ip.setTTL(ttl);

		//协议
		byte protocol = content[offset++];
		ip.setProtocol(protocol);
	
		//16位首部检验和
		for(int nIndex = 0; nIndex < 2; ++nIndex)
			buff_2[nIndex] = content[nIndex + offset];
		short checkSum = DataUtil.byteArrayToShort(buff_2);
		ip.setCheckSum(checkSum);

		//32位源IP地址
		offset += 2;
		for(int nIndex = 0; nIndex < 4; ++nIndex)
			buff_4[nIndex] = content[nIndex + offset];
		int srcIP = DataUtil.byteArrayToInt(buff_4);
		ip.setSrcIP(srcIP);

		//32位目的IP地址
		offset += 4;
		for(int nIndex = 0; nIndex < 4; ++nIndex)
			buff_4[nIndex] = content[nIndex + offset];
		int desIP = DataUtil.byteArrayToInt(buff_4);
		ip.setDesIP(desIP);
		
		return ip;
	}

	//返回IP地址的字符串
	public static String getIPString(int ip){
		byte[] byteIP = new byte[4];
		byteIP = DataUtil.intToByteArray(ip);
		StringBuilder strBuilder = new StringBuilder();
		for(int nIndex = 0; nIndex < 4; ++nIndex){
			strBuilder.append((int) (byteIP[nIndex] & 0xff));
			strBuilder.append(".");
		}
		strBuilder.deleteCharAt(strBuilder.length() - 1);
		return strBuilder.toString();
	}
}
