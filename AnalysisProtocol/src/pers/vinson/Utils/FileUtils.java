package pers.vinson.Utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class FileUtils {
	
	private FileUtils(){}
	
	/**
	 * ��һ�����д���ļ�
	 * @param line ���
	 * @param filepath ��д����ļ�·��
	 * @param append �Ƿ�׷�����
	 * @return flag д���Ƿ�ɹ�
	 */
	public static boolean writeLineToFile(String line, File file, boolean append){
		boolean flag = true;
		try {
			FileWriter fw = new FileWriter(file, append);
			fw.write(line + "\n");
			fw.flush();
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
			flag = false;
		}
		
		return flag;
	}
	
	/**
	 * ��һ�����д���ļ�
	 * @param line ���
	 * @param filepath �ļ�·��
	 * @param append �Ƿ�׷�����
	 */
	public static boolean writeLineToFile(String line, String filepath, boolean append){
		File file = new File(filepath);
		return writeLineToFile(line, file, append);
	}
	
	/**
	 * д�������ݣ���δÿһ������Զ����ϻ��з�
	 * @param lines
	 * @param filepath
	 * @param append
	 * @return flag 
	 */
	public static boolean writeLinesToFile(String[] lines, String filepath, boolean append){
		boolean flag = true;
		try {
			File file = new File(filepath);
			FileWriter fw = new FileWriter(file, append);
			for(String line : lines){
				line += "\n";
				fw.write(line);
			}
			fw.flush();
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
			flag = false;
		}
		
		return flag;
	}
	
	/**
	 * ���ļ��в����ڣ��򴴽��ļ���
	 * @param dirpath	Ŀ¼·��
	 * @return flag		Ŀ¼�����Ƿ�ɹ��ı�־
	 */
	public static boolean createDir(String dirpath){
		boolean flag = false;
		File file = new File(dirpath);
		if(!file.exists()){						// �жϸ�Ŀ¼�Ƿ����
			file.mkdirs();
			flag = true;
		} 
		
		return flag;
	}
	
	/**
	 * ɾ����ļ�
	 * @param filepath : ��ɾ���ļ����ļ���	  
	 * @return		   : �����ļ�ɾ��ɹ�����true�����򷵻�false
	 */
	public static boolean deleteFile(String filepath){
		boolean flag = false;
		File file = new File(filepath);
		// ·��Ϊ�ļ��Ҳ�Ϊ��ʱ��ɾ��
		if(file.isFile() && file.exists()){
			file.delete();
			flag = true;
		}
		return flag;
	}
	
	/**
	 * ɾ��Ŀ¼���ļ��У��Լ�Ŀ¼�µ��ļ�
	 * @param path	   : ��ɾ��Ŀ¼���ļ�·��
	 * @return		   : Ŀ¼ɾ��ɹ�����true�����򷵻�false
	 */
	private static boolean deleteDirectory(String path){
		boolean flag = false;

		//��� path �����ļ��ָ����β���Զ�����ļ��ָ���
		if(!path.endsWith(File.separator)){
			path = path + File.separator;
		}
		File dirFile = new File(path);

		// ��� dir ��Ӧ���ļ������ڣ����߲���һ��Ŀ¼�����˳� 
		if(!dirFile.exists() || !dirFile.isDirectory()){
			return false;
		}
		flag = true;

		// ɾ���ļ����µ������ļ�(������Ŀ¼)
		File[] files = dirFile.listFiles();
		for(int i = 0; i < files.length; i ++){
			// ɾ�����ļ�
			if(files[i].isFile()){
				flag = deleteFile(files[i].getAbsolutePath());
				if(!flag){
					break;
				}
			} else{
				flag = deleteDirectory(files[i].getAbsolutePath());
				if(!flag){
					break;
				}
			}
		}
		if(!flag){
			return false;
		}

		// ɾ��ǰĿ¼
		if(dirFile.delete()){
			return true;
		} else{
			return false;
		}

	}
	
	/**
	 * ͨ�õ��ļ��л��ļ�ɾ���ֱ�ӵ��ô˷���������ʵ��ɾ���ļ��л��ļ��������ļ����µ�����
	 * �ļ�
	 * @param path	   : Ҫɾ���Ŀ¼���ļ�
	 * @return		   : ɾ��ɹ����� true�����򷵻� false��
	 */
	public static boolean deleteFolder(String path){
		boolean flag = false;
		File file = new File(path);
		// �ж��ļ���Ŀ¼�Ƿ����
		if(!file.exists()){
			return flag;
		} else{
			// �ж��Ƿ�Ϊ�ļ�
			if(file.isFile()){		// Ϊ�ļ�ʱ����ɾ���ļ��ķ���
				return deleteFile(path);
			} else{					// ΪĿ¼ʱ����ɾ��Ŀ¼����
				return deleteDirectory(path);
			}
		}
	}
	
	/**
	 * �ر����������
	 * @param is
	 * @param os
	 */
	public static void closeStream(InputStream is, OutputStream os) {
		if(is != null){
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		if(os != null){
			try {
				os.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * ��ָ��·���Ĵ��̴���
	 * @param path �ļ�·�� ·������������ C:\Test\ ����ʽ����������б��
	 */
	public static void openWindow(String path){
		Runtime runtime = Runtime.getRuntime();
		try {
			Process process = runtime.exec("cmd /c start explorer " + path);
			int exitCode = process.waitFor();
			if(exitCode == 0){
				// success
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * ����һ�����ļ�
	 * @param pathname
	 */
	public static void createEmpFile (String pathname) {
		File file = new File(pathname);
		if (file.exists()) {
			deleteFile(pathname);
		}
		
		try {
			file.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * �ж��ļ��Ƿ����
	 * @param file
	 */
	public static boolean isFileEmpty(File file) {
		if (file == null || file.length() < 0) {
			return false;
		} else {
			return true;
		}
	}
	
}
