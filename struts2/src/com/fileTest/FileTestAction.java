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
	
	// ���Ͼ��ε� �Է�
	public String created() throws Exception { // ���� ���ε�
		
		if(dto==null||dto.getMode()==null||dto.getMode().equals("")) {
			return INPUT;
		}
		
		// dto ���� ������	
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		
		// ��� ����
		String root = session.getServletContext().getRealPath("/"); // �ֻ��� ��Ʈ
		String savePath = root + "pds" + File.separator + "data"; // ������
			
		// db�� ���� saveFileName
		saveFileName = 
				FileManager
				.doFileUpload(dto.getUpload(), dto.getUploadFileName(), savePath); // (dto���ִ�upload����, dto�� uploadFileName ,  ������)
		
		// db�� ���� �������������̸�(��ũ�Ҷ��ʿ�)
		originalFileName = dto.getUploadFileName();
			
		return SUCCESS;
	}
	
	// ���� �ٿ�ε�
	public String download() throws Exception { // ���� �ٿ�ε�
		
		
		// dto ���� ������	
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		
		HttpSession session = request.getSession();
		
		// ��� ����
		String root = session.getServletContext().getRealPath("/"); // �ֻ��� ��Ʈ
		String savePath = root + "pds" + File.separator + "data"; // ������
		
		FileManager.doFileDownload(response, saveFileName,
				originalFileName, savePath);
		
		
		return null; // �ٿ�ε�� �ǵ����ִ� ���� ����
	}
	
	// ���� ���� ���� 
	public String down() throws Exception { // ���� ����
			
			
		// dto ���� ������	
		HttpServletRequest request = ServletActionContext.getRequest();
		
		HttpSession session = request.getSession();
		
		// ��� ����
		String root = session.getServletContext().getRealPath("/"); // �ֻ��� ��Ʈ
		String savePath = root + "pds" + File.separator + "data"; // ������
		
		
		originalFileName = new String(originalFileName.getBytes("euc-kr"),"8859_1"); // ���Ϻ��� �������ʱ� ����
		
		String fullFileName = savePath + File.separator + saveFileName;
		
		// �ٿ��� �ϴ°� �ƴ϶� �о�� �Ѵ�.
		inputStream = new FileInputStream(fullFileName);
		
		return SUCCESS;
	}
	
	// ���� ����
	private InputStream inputStream;  // ȭ���� ������ Ŭ���ϸ� �� �Ʒ��ѱ�(���α׷�)�� �о�� ��� �����ְԲ� 
		
	
	// db����� 
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
