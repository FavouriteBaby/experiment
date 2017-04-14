package pers.vinson.PcapParse;

import pers.vinson.Utils.DataUtil;
import pers.vinson.datastruct.LinkType;

public class GetOffset {
	private static int offset;
	
	public static int getIPOffset(int nLinkType){
		switch(nLinkType){
		case LinkType.RAWIP:{
			offset = 0;
			return offset;
		}
		case LinkType.CISCOHDLC:{
			offset = 4;
			return offset;
		}
		default:
			return LinkType.OTHER;
		}
	}
	
	private static int getOffset(){
		return offset;
	}
	
	public static int getTransportLayerOffset(int nLinkType, byte varHLen){
		int offset = getOffset();
		offset += 4 * DataUtil.getLowByte(varHLen);
		return offset;
	}
}
