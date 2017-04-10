package pers.vinson.Utils;

public class DataUtil {
	public DataUtil(){}
	
	//byte转16进制字符串
	public static String byteToHexString(byte b){
		return intToHexString(byteToInt(b));
	}

	//将byte数组转成16进制字符串
	public static StringBuilder byteArrayToHexString(byte[] b){
		StringBuilder sbHexString = new StringBuilder();
		for(int nIndex = 0; nIndex < b.length; ++nIndex){
			sbHexString.append(intToHexString(byteToInt(b[nIndex])));
			sbHexString.append(" ");
		}
		return sbHexString;
	}

	//将int转成16进制字符串
	public static String intToHexString(int data){
		return Integer.toHexString(data);
	}
	
	//byte转int
	public static int byteToInt(byte b){
		return (b & 0xff);
	}
}
