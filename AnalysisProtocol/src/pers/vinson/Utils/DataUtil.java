package pers.vinson.Utils;

public class DataUtil {
	public DataUtil(){}
	
	//byte杞�16杩涘埗瀛楃涓�
	public static String byteToHexString(byte b){
		return intToHexString(byteToInt(b));
	}

	//灏哹yte鏁扮粍杞垚16杩涘埗瀛楃涓�
	public static StringBuilder byteArrayToHexString(byte[] b){
		StringBuilder sbHexString = new StringBuilder();
		for(int nIndex = 0; nIndex < b.length; ++nIndex){
			sbHexString.append(intToHexString(byteToInt(b[nIndex])));
			sbHexString.append(" ");
		}
		return sbHexString;
	}

	//灏唅nt杞垚16杩涘埗瀛楃涓�
	public static String intToHexString(int data){
		return Integer.toHexString(data);
	}
	
	//byte杞琲nt
	public static int byteToInt(byte b){
		return (b & 0xff);
	}
	
	//byte[]杞垚int
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
	
	//byte杞瑂hort
	public static short byteArrayToShort(byte[] b){
		return byteArrayToShort(b, 0);
	}
	
	public static short byteArrayToShort(byte[] b,int offset){
		return (short) (((b[offset] & 0xff) << 8) | (b[offset + 1] & 0xff)); 
	}
	
	//鏁扮粍閫嗚浆
	public static void reverseByteArray(byte[] arr){
		byte temp;
		int n = arr.length;
		for(int i = 0; i < n / 2; i++){
			temp = arr[i];
			arr[i] = arr[n - 1 - i];
			arr[n - 1 - i] = temp;
		}
	}

	//int 杞� byte鏁扮粍
	public static byte[] intToByteArray(int i) {
		byte[] result = new byte[4];   
		//鐢遍珮浣嶅埌浣庝綅
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
}
