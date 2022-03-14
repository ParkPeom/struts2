package com.test1;

import com.opensymphony.xwork2.ActionSupport;

public class TestAction extends ActionSupport {

	private static final long serialVersionUID = 1L;
	
	// dto를 가지고 와서
	// dto를 한번더 감싸줘서 dto. 으로 접근하는걸 domain object라고 한다.
	// 폼에서 보낼때도 dto.  el태그로 받아낼때도 dto.
	private UserDTO dto; 
	
	
	// DTO에 만들지 않은 이유는 여기서 메세지에 관련된 처리를 보여주려고,..
	// 이 안에서만 만들 수 있는 변수값이 필요할때가 있기 때문에.
	private String message;
	
	
	// dto에 값을 넣고 싶으면 getter/setter
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
		
		// userId를 다이렉트로 접근할수가 없다
		message = dto.getUserName() + "님 방가방가...";
		
		return "ok"; // SUCESS 말고 사용자 정의 문자를 써도됨
	}
	
}
