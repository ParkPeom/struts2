package com.test;

import com.opensymphony.xwork2.ActionSupport;

// 스트럿츠2에서는 ActionSupport를 상속받는다

public class TestAction extends ActionSupport{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String userId;
	private String userName;
	private String message;
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
	//struts1 에 비해 struts2는 필요할때만 request,form,response 달라고 요청하면 됨 (경향컨테이너)
	public String execute() throws Exception {
		
		message = userName + "님 방가방가...";

		return SUCCESS; // 작업이 잘실행되면 SUCCESS쓰라고 제시 
						// 반드시 대문자 안에 "success" 소문자가 들어간 상수 
		
	}
}
