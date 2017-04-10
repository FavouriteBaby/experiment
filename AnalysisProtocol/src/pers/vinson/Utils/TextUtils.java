package pers.vinson.Utils;

public class TextUtils {
	
	private TextUtils () {}
	
	/**
	 * �ж��ַ��Ƿ�Ϊ��
	 * @param str
	 * @return
	 */
	public static boolean isEmpty (String str) {
		if (str == null || str.length() < 0) {
			return true;
		} else {
			return false;
		}
	}
	
}
