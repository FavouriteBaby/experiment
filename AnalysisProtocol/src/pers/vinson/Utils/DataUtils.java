package pers.vinson.Utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class DataUtils {

	private DataUtils () {}

	/**
	 * ��ʽ��ʮ��������
	 * @param hex
	 * @param type 2�ֽ���ݣ�0�� 4 �ֽ���ݣ�1
	 * @return
	 */
	public static String formatHexData (String hex, int type) {
		int len = hex.length();		// ��ݳ�

		if (type == 0) {			// 2 ���ֽڵ���ݲ� 4 - len ��0
			if (len < 4) {			// ��С�� 4����ǰ�˲� 0, ֱ������ 4 ����
				int correct = 4 - len;
				for (int i = 0; i < correct; i ++) {
					hex = "0" + hex;
				}
			}
		} else if (type == 1) {		// 4 �ֽ���ݲ� 8 - len ��0�������м䲹�ո�
			if (len <= 4) {			// ��С�ڵ��� 4
				int correct = 4 - len;
				for (int i = 0; i < correct; i ++) {
					hex = "0" + hex;// ǰ�˲� 0
				}

				hex += " 0000";		// ��˲��ո��� 0 
			} else if (len > 4) {	// ���Ǵ����ģ����ڵ�4λ�͵���λ��֮����Ͽո�
				//				List<String> datas = Arrays.asList(hex.split(""));
				List<String> datas = new ArrayList<String>();
				for (String s : hex.split("")) {
					datas.add(s);
				}

				// ���� 9 λԪ�أ�����������λ���������Խ�����
				int correct = 9 - len;
				for (int i = 0; i < correct; i ++) {
					datas.add(" ");
				}
				// ����λ���ڲ������
				for (int i = len; i > 3; i --) {	// ��Ԫ�غ���
					datas.set(i, datas.get(i - 1));
				}
				// Ԫ�ز���
				datas.set(4, " ");
				StringBuilder builder = new StringBuilder();
				for (String s : datas) {
					builder.append(s);
				}
				hex = builder.toString();

				if (hex.length() < 9) {
					for (int i = 0; i < correct - 1; i ++) {
						hex += "0";
					}
				}
			}
		} else {					// ��������
			return null;
		}


		return null;
	}

	/**
	 * ��һά���ֽ���������
	 * @param arr
	 */
	public static void reverseByteArray(byte[] arr){
		byte temp;
		int n = arr.length;
		for(int i = 0; i < n / 2; i++){
			temp = arr[i];
			arr[i] = arr[n - 1 - i];
			arr[n - 1 - i] = temp;
		}
	}

	/**
	 * byte ת int
	 * @param b
	 * @return
	 */
	public static int byteToInt (byte b) {
		return (b & 0xff);
	}

	/**
	 * һά�ֽ�����ת int ֵ(4 �ֽ�)
	 * @param b
	 * @return
	 */
	public static int byteArrayToInt(byte[] b){
		return byteArrayToInt(b, 0);
	}

	/**
	 * һά�ֽ�����ת int ֵ(4 �ֽ�)
	 * @param b
	 * @param offset
	 * @return
	 */
	public static int byteArrayToInt(byte[] bytes, int offset){
		int value= 0;
		//�ɸ�λ����λ
		for (int i = 0; i < 4; i++) {
			int shift= (4 - 1 - i) * 8;
			value +=(bytes[i] & 0x000000FF) << shift;//���λ��
		}

		return value;
	}

	/**
	 * һά�ֽ�����ת short ֵ(2 �ֽ�)
	 * @param b
	 * @return
	 */
	public static short byteArrayToShort(byte[] b){
		return byteArrayToShort(b, 0);
	}

	/**
	 * һά�ֽ�����ת short ֵ(2 �ֽ�)
	 * @param b
	 * @param offset
	 * @return
	 */
	public static short byteArrayToShort(byte[] b,int offset){
		return (short) (((b[offset] & 0xff) << 8) | (b[offset + 1] & 0xff)); 
	}

	//将byte转成16进制字符串
	public static String byteToHexString (byte b) {
		return intToHexString(byteToInt(b));
	}

	/**
	 * �� int �������תΪ byte[] 
	 * @param data
	 * @return
	 */
	public static byte[] intToByteArray(int i) {
		byte[] result = new byte[4];   
		//�ɸ�λ����λ
		result[0] = (byte)((i >> 24) & 0xFF);
		result[1] = (byte)((i >> 16) & 0xFF);
		result[2] = (byte)((i >> 8) & 0xFF); 
		result[3] = (byte)(i & 0xFF);
		return result;
	}

	/**
	 * short ת 16 �����ַ�
	 * @param s
	 * @return
	 */
	public static String shortToHexString (short s) {
		String hex = intToHexString(s);
		int len = hex.length();
		if (len > 4) {	// ��ʱ short ֵΪ��ֵ����λ�Ჹ 1����� ffffed5c����˽�ȥ���λ
			hex = hex.substring(4);
		} 

		len = hex.length();
		if (len < 4) {	// ��С�� 4�����λ�� 0
			int n = 4 - len;
			for (int i = 0; i < n; i ++) {
				hex = "0" + hex;
			}
		}

		return "0x" + hex;
	}

	/**
	 * �� int תΪ 16 �����ַ�
	 * @param data
	 * @return
	 */
	public static String intToHexString (int data) {
		return Integer.toHexString(data);
	}

	/**
	 * �������Ƶ������ַ�תΪʮ����
	 * @param str
	 */
	public static int binaryToDecimal (String str) {
		String[] strs = str.split("");
		List<Integer> datas = new ArrayList<Integer>();
		for (String s : strs) {
			datas.add(Integer.valueOf(s));
		}
		int size = datas.size();

		int values = 0;
		if (size <= 16) {
			for (int i = 0; i < size; i ++) {
				values += (datas.get(i) * ((int) Math.pow(2, size - i - 1)));
			}
		} else {	// ��������Ǹ���ֵΪ��ֵ��ǰ�油 1
			// ֻ������� 16 λ��
			int offset = size - 16;
			for (int i = 0; i < 16; i ++) {
				values += (datas.get(i + offset) * ((int) Math.pow(2, 16 - i - 1)));
			}
		}

		return values;
	}

	//将byte数组按顺序拼接成二进制数据
	public static String byteArrayToBinaryString (byte[] bytes) {
		String line = "";
		for (byte b : bytes) {
			line += (Integer.toBinaryString(byteToInt(b)));
		}

		return line;
	}

	/**
	 * �� long ��ݴ���� KB ��λ
	 * @return �ַ�
	 */
	public static String toMBString (long i) {
		DecimalFormat format = new DecimalFormat("#.##");	// ȡС���� 2 λ������ȡ��
		return format.format((i / 1024.0));
	}

	/**
	 * �� long ��ݴ���� KB ��λ(��ȷ��С����2λ, ����ȡ��)
	 * @return
	 */
	public static BigDecimal toMB (long i) {
		return roundDown(i / 1024.0);
	}

	/**
	 * ����ȡ��
	 * @param d
	 * @return
	 */
	public static BigDecimal roundDown (double d) {
		BigDecimal decimal = new BigDecimal((d));
		return scale(decimal, 2);
	}

	/**
	 * ��������ȡ���侫��
	 * @param decimal
	 * @param scale
	 * @return
	 */
	public static BigDecimal scale (BigDecimal decimal, int scale) {
		return decimal.setScale(scale, BigDecimal.ROUND_DOWN);
	}

	/**
	 * У���ļ���
	 * @param filename protocol��Ԫ���ļ��� 
	 * @return ��ϸ�ʽ���ļ���(ipС�ķ���ǰ��)
	 */
	public static String validateFilename (String filename) {
//		LogUtils.printObj("У��ǰ", filename);
		
		String[] s1 = filename.split("\\[");	// TCP  59.175.132.20] 	80]   192.168.1.40]	1581]	
		String protocol = s1[0];
		String ip1 = s1[1].split("\\]")[0];
		String port1 = s1[2].split("\\]")[0];
		String ip2 = s1[3].split("\\]")[0];
		String port2 = s1[4].split("\\]")[0];

		// �и� ip
		String[] ip_s1 = ip1.split("\\.");
		String[] ip_s2 = ip2.split("\\.");

		// �Ƚ� ip �� ip �Ĵ�С
		String rs = protocol + "[" + ip1 + "]"
				 			 + "[" + port1 + "]"
				 			 + "[" + ip2 + "]"
				 			 + "[" + port2 + "]";
		String tmp_ip = ip1;
		String tmp_port = port1;
		
		int ip1_part1 = Integer.valueOf(ip_s1[0]);
		int ip2_part1 = Integer.valueOf(ip_s2[0]);
		
		if (ip1_part1 > ip2_part1) {			// �����ڣ���ֱ�ӽ���
			rs = swape(protocol, ip2, port2, tmp_ip, tmp_port);
		}  else if (ip1_part1 == ip2_part1) {	// ������, ��Ƚϵڶ�λ
			
			int ip1_part2 = Integer.valueOf(ip_s1[1]);
			int ip2_part2 = Integer.valueOf(ip_s2[1]);
			if (ip1_part2 > ip2_part2) {
				rs = swape(protocol, ip2, port2, tmp_ip, tmp_port);
			} else if (ip1_part2 == ip2_part2) {	// ������, ��Ƚϵ���λ
				
				int ip1_part3 = Integer.valueOf(ip_s1[2]);
				int ip2_part3 = Integer.valueOf(ip_s2[2]);
				if (ip1_part3 > ip2_part3) {
					rs = swape(protocol, ip2, port2, tmp_ip, tmp_port);
				} else if (ip1_part3 == ip2_part3) {	// ��С�ڵ���, ��Ƚϵ���λ
					
					int ip1_part4 = Integer.valueOf(ip_s1[3]);
					int ip2_part4 = Integer.valueOf(ip_s2[3]);
					if (ip1_part4 > ip2_part4) {
						rs = swape(protocol, ip2, port2, tmp_ip, tmp_port);
					} 
				}
			}
		} // ��С�ڣ�ֱ�ӷ���
		
//		LogUtils.printObj("У���", rs);
//		LogUtils.printObj("\n");

		return rs;
	}
	
	/**
	 * ����
	 * @param protocol
	 * @param ip2
	 * @param port2
	 * @param tmp_ip
	 * @param tmp_port
	 * @return
	 */
	private static String swape (String protocol, String ip2, String port2, String tmp_ip, String tmp_port) {
		String ip1 = ip2;
		ip2 = tmp_ip;

		String port1 = port2;
		port2 = tmp_port;
		String rs = protocol + "[" + ip1 + "]"
							 + "[" + port1 + "]"
							 + "[" + ip2 + "]"
							 + "[" + port2 + "]";
		
		return rs;
	}

}