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
	
	//byte[]转成int
	public static int byteArrayToInt(byte[] b){
		return byteArrayToInt(b, 0);
	}
	
	public static int byteArrayToInt(byte[] b, int offset){
		int value = 0;
		for(int nIndex = 0; nIndex < 4; ++nIndex){
			int shift = (4 - 1 - nIndex) * 8;
			value += (b[nIndex] & 0x000000FF) << shift;
		}
		return value;
	}
	
	//byte转short
	public static short byteArrayToShort(byte[] b){
		return byteArrayToShort(b, 0);
	}
	
	public static short byteArrayToShort(byte[] b,int offset){
		return (short) (((b[offset] & 0xff) << 8) | (b[offset + 1] & 0xff)); 
	}
	
	//数组逆转
	public static void reverseByteArray(byte[] arr){
		byte temp;
		int n = arr.length;
		for(int i = 0; i < n / 2; i++){
			temp = arr[i];
			arr[i] = arr[n - 1 - i];
			arr[n - 1 - i] = temp;
		}
	}
}
