package com.fileTest;

import java.io.File;

public class FileTestDTO {
	
	private File upload;  // ���ε�� �Է�â�� <input �ؼ� name�� upload �̸��״�� ������
	
	// ������ ������ upload�� FileName���ڸ� �ٿ��� ������ �����
	// struts2�� �ڵ����� ������ �̸��� �־��ش�.
	// upload + FileName 
	private String uploadFileName; // ��Ʈ����2�� upload�ڿ� filename�� �־���
	
	private String mode; // ȭ�鱸����

	
	public File getUpload() {
		return upload;
	}

	public void setUpload(File upload) {
		this.upload = upload;
	}

	public String getUploadFileName() {
		return uploadFileName;
	}

	public void setUploadFileName(String uploadFileName) {
		this.uploadFileName = uploadFileName;
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}
	
}
