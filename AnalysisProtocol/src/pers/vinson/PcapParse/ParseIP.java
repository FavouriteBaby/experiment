package pers.vinson.PcapParse;

import javax.xml.crypto.Data;
import pers.vinson.Utils.DataUtil;
import pers.vinson.datastruct.IPPacketHeader;
import pers.vinson.datastruct.ProtocolNum;
import pers.vinson.datastruct.ProtocolType;

public class ParseIP {
	public static IPPacketHeader parse(byte[] content, int offset, boolean isBigEnd){
		IPPacketHeader ip = new IPPacketHeader();
		byte[] buff_4 = new byte[4];
		byte[] buff_2 = new byte[2];
		
		//鐗堟湰鍙峰拰棣栭儴闀垮害
		byte varHLen = content[offset++];
		if(0 == varHLen)
			return null;
		ip.setVarHLen(varHLen);
		
		//鏈嶅姟绫诲瀷
		byte tos = content[offset++];
		ip.setTos(tos);
		
		//鎬婚暱搴�
		for(int nIndex = 0; nIndex < 2; ++nIndex)
			buff_2[nIndex] = content[nIndex + offset];
		short totalLen = DataUtil.byteArrayToShort(buff_2);
		ip.setTotalLen(totalLen);
		
		//鏍囪瘑
		offset += 2;
		for(int nIndex = 0; nIndex < 2; ++nIndex)
			buff_2[nIndex] = content[nIndex + offset];
		short id = DataUtil.byteArrayToShort(buff_2);
		ip.setId(id);

		//鏍囧織鍜�13浣嶇墖鍋忕Щ
		offset += 2;
		for(int nIndex = 0; nIndex < 2; ++nIndex)
			buff_2[nIndex] = content[nIndex + offset];
		short flagSegment = DataUtil.byteArrayToShort(buff_2);
		ip.setFlagSegment(flagSegment);

		//鐢熷瓨鏃堕棿
		offset += 2;
		byte ttl = content[offset++];
		ip.setTTL(ttl);

		//鍗忚
		byte protocol = content[offset++];
		ip.setProtocol(protocol);
	
		//16浣嶉閮ㄦ楠屽拰
		for(int nIndex = 0; nIndex < 2; ++nIndex)
			buff_2[nIndex] = content[nIndex + offset];
		short checkSum = DataUtil.byteArrayToShort(buff_2);
		ip.setCheckSum(checkSum);

		//32浣嶆簮IP鍦板潃
		offset += 2;
		for(int nIndex = 0; nIndex < 4; ++nIndex)
			buff_4[nIndex] = content[nIndex + offset];
		int srcIP = DataUtil.byteArrayToInt(buff_4);
		ip.setSrcIP(srcIP);

		//32浣嶇洰鐨処P鍦板潃
		offset += 4;
		for(int nIndex = 0; nIndex < 4; ++nIndex)
			buff_4[nIndex] = content[nIndex + offset];
		int desIP = DataUtil.byteArrayToInt(buff_4);
		ip.setDesIP(desIP);
		
		return ip;
	}

	//杩斿洖IP鍦板潃鐨勫瓧绗︿覆
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
	
	//get transport layer protocol type
	public static int getTransportLayerProtocol(byte protocol){
		switch(DataUtil.byteToInt(protocol)){
			case ProtocolNum.TCP:
				return ProtocolNum.TCP;
			case ProtocolNum.UDP:
				return ProtocolNum.UDP;
			default:
				return ProtocolNum.OTHER;
		}
	}
}
