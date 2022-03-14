package com.test1;

import com.opensymphony.xwork2.ActionSupport;

public class TestAction extends ActionSupport {

	private static final long serialVersionUID = 1L;
	
	// dto�� ������ �ͼ�
	// dto�� �ѹ��� �����༭ dto. ���� �����ϴ°� domain object��� �Ѵ�.
	// ������ �������� dto.  el�±׷� �޾Ƴ����� dto.
	private UserDTO dto; 
	
	
	// DTO�� ������ ���� ������ ���⼭ �޼����� ���õ� ó���� �����ַ���,..
	// �� �ȿ����� ���� �� �ִ� �������� �ʿ��Ҷ��� �ֱ� ������.
	private String message;
	
	
	// dto�� ���� �ְ� ������ getter/setter
	public UserDTO getDto() {
		return dto;
	}

	public void setDto(UserDTO dto) {
		this.dto = dto;
	}

	public String getMessage() {
		return message;
	}
	
	public String execute() throws Exception {
		
		// userId�� ���̷�Ʈ�� �����Ҽ��� ����
		message = dto.getUserName() + "�� �氡�氡...";
		
		return "ok"; // SUCESS ���� ����� ���� ���ڸ� �ᵵ��
	}
	
}
