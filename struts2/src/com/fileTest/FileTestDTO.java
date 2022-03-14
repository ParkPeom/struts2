package com.fileTest;

import java.io.File;

public class FileTestDTO {
	
	private File upload;  // 업로드는 입력창에 <input 해서 name에 upload 이름그대로 가져옴
	
	// 위에서 정의한 upload에 FileName문자를 붙여서 변수를 만들면
	// struts2가 자동으로 파일의 이름을 넣어준다.
	// upload + FileName 
	private String uploadFileName; // 스트럿츠2가 upload뒤에 filename를 넣어줌
	
	private String mode; // 화면구분자

	
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
