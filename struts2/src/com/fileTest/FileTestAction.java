package com.fileTest;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import com.util.FileManager;

public class FileTestAction extends ActionSupport
	implements Preparable , ModelDriven<FileTestDTO>{

	private static final long serialVersionUID = 1L;
	
	private FileTestDTO dto;
	
	
	public FileTestDTO getDto() {
		return dto;
	}

	@Override
	public FileTestDTO getModel() {
		return dto;
	}

	@Override
	public void prepare() throws Exception {
		
		dto = new FileTestDTO();
	
	}
	
	// 파일업로드 입력
	public String created() throws Exception { // 파일 업로드
		
		if(dto==null||dto.getMode()==null||dto.getMode().equals("")) {
			return INPUT;
		}
		
		// dto 값이 있으면	
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		
		// 경로 구함
		String root = session.getServletContext().getRealPath("/"); // 최상의 루트
		String savePath = root + "pds" + File.separator + "data"; // 저장경로
			
		// db에 넣을 saveFileName
		saveFileName = 
				FileManager
				.doFileUpload(dto.getUpload(), dto.getUploadFileName(), savePath); // (dto에있는upload를줌, dto에 uploadFileName ,  저장경로)
		
		// db에 넣을 오리지널파일이름(링크할때필요)
		originalFileName = dto.getUploadFileName();
			
		return SUCCESS;
	}
	
	// 파일 다운로드
	public String download() throws Exception { // 파일 다운로드
		
		
		// dto 값이 있으면	
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		
		HttpSession session = request.getSession();
		
		// 경로 구함
		String root = session.getServletContext().getRealPath("/"); // 최상의 루트
		String savePath = root + "pds" + File.separator + "data"; // 저장경로
		
		FileManager.doFileDownload(response, saveFileName,
				originalFileName, savePath);
		
		
		return null; // 다운로드는 되돌러주는 값이 없다
	}
	
	// 파일 내용 보기 
	public String down() throws Exception { // 파일 보기
			
			
		// dto 값이 있으면	
		HttpServletRequest request = ServletActionContext.getRequest();
		
		HttpSession session = request.getSession();
		
		// 경로 구함
		String root = session.getServletContext().getRealPath("/"); // 최상의 루트
		String savePath = root + "pds" + File.separator + "data"; // 저장경로
		
		
		originalFileName = new String(originalFileName.getBytes("euc-kr"),"8859_1"); // 파일볼때 깨지지않기 위함
		
		String fullFileName = savePath + File.separator + saveFileName;
		
		// 다운을 하는게 아니라 읽어내야 한다.
		inputStream = new FileInputStream(fullFileName);
		
		return SUCCESS;
	}
	
	// 파일 보기
	private InputStream inputStream;  // 화일의 내용을 클릭하면 그 아래한글(프로그램)을 읽어들어서 열어서 볼수있게끔 
		
	
	// db저장용 
	private String saveFileName;
	private String originalFileName;
	

	public InputStream getInputStream() {
		return inputStream;
	}

	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
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
	
	
	
}
