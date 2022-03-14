package com.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Calendar;

import javax.servlet.http.HttpServletResponse;


// ��Ʈ������2 ���Ͼ��ε�

public class FileManager {
	
	// ��Ʈ����2 ����ٰ� ���Ͼ��ε� �����
	
	
					// ������ �̸��� ���� 
	public static String doFileUpload(File file,String originalFileName , String path) throws IOException { // ������� ���ε����� , ���̴ٰ� ��� 
		
		// upload �� a.txt�� ����
		// a.txt ���� txt�� ������ 
		// ���ϸ�� ������� 20220310115020xxxxx �� .txt�� ���ϰ��̴�
		// ���Ӱ� ����Ǵ� saveFileName�̴�
		// ��ȯ������ �޾Ƴ��߸� �������� ���ϳ��Ӱ� ���̺����ϳ����� db�� �����ϰԵȴ�.
		// �׷��� ��ȯ���� String�̴�.
		
		String newFileName = null;
		
		if(file==null) {
			return null;
		}
		
	
		if(originalFileName.equals("")) {
			return null;
		}
		
		// Ȯ���� ���� �����߸� ���߿� ���ϼ�������
		// abc.txt �ڿ������� .�� ã�Ƽ� ������ ( �μ��� ������ �ϳ��� ������) 
		String fileExt = originalFileName.substring(originalFileName.lastIndexOf(".")); // �ǵ��� �ε������� . ���� txt 
		
		if(fileExt==null || fileExt.equals("")) {
			return null;
		}
		
		// ������ ������ ���ο� ���� �̸� ����
									// �� �� �� �� �� ��
		// 1���� : Ķ���� �� �ν��Ͻ� �ϳ��� ���� �ϴ� ��������
	
		//������ ������ ���ο� �����̸� ����
				newFileName = String.format("%1$tY%1$tm%1$td%1$tH%1$tM%1$tS",
						Calendar.getInstance());
				newFileName += System.nanoTime();  // �����ߺ��������� 10��-9�°�
				newFileName += fileExt; // Ȯ���ڸ� ���̸� ���ε��� ���ο� �����̸��� �������
		
		// ���Ͼ��ε� 
		File f = new File(path);
		
		if(!f.exists()) {
			f.mkdirs();
		}
		
		// Ǯ ���ϱ��� ���Ե� ���
		String fullFilePath = path + File.separator + newFileName;

		
		// -------------���� ���ε� �ҽ��ڵ�--------------
		// ���ε��� ������ �о�� (������ ���Ͼ��ε�� �Ȱ���)
		FileInputStream fis = new FileInputStream(file);

		// ������ �������¾�
		FileOutputStream fos = new FileOutputStream(fullFilePath);
		
		
		int data = 0;
		byte[] buffer = new byte[1024];
		
		// �о�� fis������ �����´�.
		while((data=fis.read(buffer, 0, 1024))!=-1) {
			
			fos.write(buffer,0,data);			
		}
		
		fos.close();
		fis.close();
		
		return newFileName;
	}
	
	// db�� �ִ°� ������ dao
	
	// ���ϴٿ�ε�
	
	// �ٿ�ε�� �������� Ŭ���̾�Ʈ�� response��Ƽ� �ش�.
	// �ٿ�ε�� response�� �ʼ��̴�.
	
	// response , ���� �����̸� , �������������̸� ,  ��� �Ű������� �޴´�.
	public static boolean doFileDownload(HttpServletResponse response,
			String saveFileName, String originalFileName,String path) {
		// a.txt�� �÷ȴµ� a.txt�� ����Ǵµ� b��»���� a.txt �ø��ԵǸ� a1.txt������
		// a1�̶�� �̸����� �ٿ������ a1�� a.txt�� �ٲ��ش�(�����������ϳ���). 
		// �׷��� �������� �����̸��� �ʿ�
		
		try {
			// �������� ������ ���°�� �����̸����� ����
			
			String filePath = path + File.separator + saveFileName;
			
			
			if(originalFileName==null || originalFileName.equals("")) {
				
				originalFileName = saveFileName; // cat.jpg
			
			}
			// ������ �ٿ�޾� Ŭ���̾�Ʈ �Ŀ� �����Ҷ� 
			// ���� �ѱ� �̸� ���� ���� ( �ݵ�� ����Ѵ�. �ѱ������� ���� ) 
			// �ѱ�2����Ʈ ���� 1����Ʈ �������� �ѱ��� 3����Ʈ�� ������ UTF-8�ξ�
			// ISO �������� 8859_1 �� �ᵵ �ȴ�.
			
			originalFileName = 
					new String(originalFileName.getBytes("euc-kr"),"ISO-8859-1");
			
			
			File f = new File(filePath); // ������ �����ϸ� ������ �ٿ���� , ������ �ڹٽ�ũ��Ʈ�� ���� ��� 
			
			// �������������� false 
			if(!f.exists()) {
				return false;
			}
			
			//  html �� utf-8�� ó���Ѵٰ� �˷������
			// application : ��� ���α׷����� ���ư�
			//  abc.txt �տ��ִ°� Ư����ȣ �ƴϸ� ��δ� ���� �ڿ��� 3�ڸ� 
			// .�� octet��� �θ� , ��������� application�� ����� octet��� ��������
			// ����
			response.setContentType("application/octet-stream");
			
			
			// �������� 
			// ������ ������ read �о �������� out�� ����.
			
			// setHeader(String name, String value) : name ����� ���� value�� �����Ѵ�.
			// ����������/���⼳�� (attachment: ÷������)
			response.setHeader("Content-disposition",
					"attachment;fileName=" + originalFileName);
			
			// ����1
			// ���۷� ��ü�� ������ �� �����͸� �о�� ������ ����
			BufferedInputStream bis = new BufferedInputStream(new FileInputStream(f));
			
			// ������ ��½�Ʈ���� ���
			OutputStream out = response.getOutputStream();
			
			
			int data;
			byte[] bytes = new byte[4096];// �ڵ����� ��������
			
			
			// 4096 ����Ʈ�� �о�ͼ� ���������� �о��
			// ����2
			// bytes�� 0��°���� 4096��° ���� �о�ͼ� 
			
			while((data=bis.read(bytes, 0, 4096))!=-1) {		
				out.write(bytes,0,data);// 0���� data�� �縸ŭ
			}
			out.flush();
			out.close();
			bis.close();
			
		} catch (Exception e) {
			System.out.println(e.toString());
			// ������ ������ false
			return false;
		}
		return true;
	}
	
	
	// ������ �����ϴ� �޼ҵ�~~~~~~~~~~~~~
	public static void doFileDelete(String fileName,String path) {
		//---------------�߿�  ���ϻ����Ҷ� �����̸�,���ϰ�� �ʿ� --------------------
		try {
			// path�� ������ ���������� path�� �Ѱ��ٰǵ�
			// ������ �̸��� db�� �ִ� 
			// �׸��� ���ε�� ���� �����ϰ� db������ �����ؾ��Ѵ�.
			
			String filePath = path + File.separator + fileName; // Ǯ����
			File f = new File(filePath); // ��θ� �Ѱ���
			
			// ������ ������ ������ - �������� ���ϻ��� 
			if(f.exists()) {
				f.delete();
			}
		} catch (Exception e) {
			System.out.println(e.toString());

		}
	}
}
