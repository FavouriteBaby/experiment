package pers.vinson.Utils;

import java.util.Date;
import java.text.SimpleDateFormat;

public class DataUtil {
	public DataUtil(){}
	
	//byte鏉烇拷16鏉╂稑鍩楃�涙顑佹稉锟�
	public static String byteToHexString(byte b){
		return intToHexString(byteToInt(b));
	}

	//鐏忓摴yte閺佹壆绮嶆潪顒佸灇16鏉╂稑鍩楃�涙顑佹稉锟�
	public static StringBuilder byteArrayToHexString(byte[] b){
		StringBuilder sbHexString = new StringBuilder();
		for(int nIndex = 0; nIndex < b.length; ++nIndex){
			sbHexString.append(intToHexString(byteToInt(b[nIndex])));
			sbHexString.append(" ");
		}
		return sbHexString;
	}

	//鐏忓攨nt鏉烆剚鍨�16鏉╂稑鍩楃�涙顑佹稉锟�
	public static String intToHexString(int data){
		return Integer.toHexString(data);
	}
	
	//byte鏉炵惒nt
	public static int byteToInt(byte b){
		return (b & 0xff);
	}
	
	//byte[]鏉烆剚鍨歩nt
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
	
	//byte鏉炵憘hort
	public static short byteArrayToShort(byte[] b){
		return byteArrayToShort(b, 0);
	}
	
	public static short byteArrayToShort(byte[] b,int offset){
		return (short) (((b[offset] & 0xff) << 8) | (b[offset + 1] & 0xff)); 
	}
	
	//閺佹壆绮嶉柅鍡氭祮
	public static void reverseByteArray(byte[] arr){
		byte temp;
		int n = arr.length;
		for(int i = 0; i < n / 2; i++){
			temp = arr[i];
			arr[i] = arr[n - 1 - i];
			arr[n - 1 - i] = temp;
		}
	}

	//int 鏉烇拷 byte閺佹壆绮�
	public static byte[] intToByteArray(int i) {
		byte[] result = new byte[4];   
		//閻㈤亶鐝担宥呭煂娴ｅ簼缍�
		result[0] = (byte)((i >> 24) & 0xFF);
		result[1] = (byte)((i >> 16) & 0xFF);
		result[2] = (byte)((i >> 8) & 0xFF); 
		result[3] = (byte)(i & 0xFF);
		return result;
	}

	//short to hexstring
	public static String shortToHexString(short s){
		String hex = intToHexString(s);
		int len = hex.length();
		if(len > 4)
			hex = hex.substring(4);
		len = hex.length();
		if(len < 4){
			int n = 4 - len;
			for(int i = 0; i < n; ++i)
				hex = "0" + hex;
		}
		return "0x" + hex;
	}
	
	//get hight 4bit of 1byte
	public static int getHighByte(byte data){
		int high = ((data & 0xf0) >> 4);
		return high;
	}
	
	//get low 4bit of 1byte
	public static int getLowByte(byte data){
		int low = ((data & 0x0f));
		return low;
	}
	
	//timestamp to date
	public static String timestampToDate(String timestamp){
		SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd");
		String sTime = timeFormat.format(new Date(Long.parseLong(timestamp)));
		return sTime;
	}
}
