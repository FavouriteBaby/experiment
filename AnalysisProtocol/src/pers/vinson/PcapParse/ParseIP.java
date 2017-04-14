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
		
		//閻楀牊婀伴崣宄版嫲妫ｆ牠鍎撮梹鍨
		byte varHLen = content[offset++];
		if(0 == varHLen)
			return null;
		ip.setVarHLen(varHLen);
		
		//閺堝秴濮熺猾璇茬��
		byte tos = content[offset++];
		ip.setTos(tos);
		
		//閹鏆辨惔锟�
		for(int nIndex = 0; nIndex < 2; ++nIndex)
			buff_2[nIndex] = content[nIndex + offset];
		short totalLen = DataUtil.byteArrayToShort(buff_2);
		ip.setTotalLen(totalLen);
		
		//閺嶅洩鐦�
		offset += 2;
		for(int nIndex = 0; nIndex < 2; ++nIndex)
			buff_2[nIndex] = content[nIndex + offset];
		short id = DataUtil.byteArrayToShort(buff_2);
		ip.setId(id);

		//閺嶅洤绻旈崪锟�13娴ｅ秶澧栭崑蹇曅�
		offset += 2;
		for(int nIndex = 0; nIndex < 2; ++nIndex)
			buff_2[nIndex] = content[nIndex + offset];
		short flagSegment = DataUtil.byteArrayToShort(buff_2);
		ip.setFlagSegment(flagSegment);

		//閻㈢喎鐡ㄩ弮鍫曟？
		offset += 2;
		byte ttl = content[offset++];
		ip.setTTL(ttl);

		//閸楀繗顔�
		byte protocol = content[offset++];
		ip.setProtocol(protocol);
	
		//16娴ｅ秹顩婚柈銊︻梾妤犲苯鎷�
		for(int nIndex = 0; nIndex < 2; ++nIndex)
			buff_2[nIndex] = content[nIndex + offset];
		short checkSum = DataUtil.byteArrayToShort(buff_2);
		ip.setCheckSum(checkSum);

		//32娴ｅ秵绨甀P閸︽澘娼�
		offset += 2;
		for(int nIndex = 0; nIndex < 4; ++nIndex)
			buff_4[nIndex] = content[nIndex + offset];
		int srcIP = DataUtil.byteArrayToInt(buff_4);
		ip.setSrcIP(srcIP);

		//32娴ｅ秶娲伴惃鍑閸︽澘娼�
		offset += 4;
		for(int nIndex = 0; nIndex < 4; ++nIndex)
			buff_4[nIndex] = content[nIndex + offset];
		int desIP = DataUtil.byteArrayToInt(buff_4);
		ip.setDesIP(desIP);
		
		return ip;
	}

	//鏉╂柨娲朓P閸︽澘娼冮惃鍕摟缁楋缚瑕�
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
