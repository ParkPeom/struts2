package com.fileTest;

import org.apache.struts.action.ActionForm;
import org.apache.struts.upload.FormFile;

public class FileTestForm extends ActionForm {

	private static final long serialVersionUID = 1L;
	
	// 스트럿츠에 파일업로드할때 필수(위에서부터 5개)이다.
	private int num;
	private String subject;
	private String saveFileName;
	private String originalFileName;
	// 스트럿츠에  파일을 업로드하는애
	// 액션폼에서 관리하듯이 똑같이 관리해줌
	private FormFile upload;

	// listnum : 기존에 있는 데이터가 몇개이든간에 rownum 일련번호를 만들어서
	// num 대신 일렬번호를 재정렬 만들어서 넣어줌
	
	// urlFile : 파일클릭했을때 다운로드 경로 
	private int listNum;
	private String urlFile;
	
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getSaveFileName() {
		return saveFileName;
	}
	public void setSaveFileName(String saveFileName) {
		this.saveFileName = saveFileName;
	}
	public String getOriginalFileName() {
		return originalFileName;
	}
	public void setOriginalFileName(String originalFileName) {
		this.originalFileName = originalFileName;
	}
	public FormFile getUpload() {
		return upload;
	}
	public void setUpload(FormFile upload) {
		this.upload = upload;
	}
	public int getListNum() {
		return listNum;
	}
	public void setListNum(int listNum) {
		this.listNum = listNum;
	}
	public String getUrlFile() {
		return urlFile;
	}
	public void setUrlFile(String urlFile) {
		this.urlFile = urlFile;
	}
}
