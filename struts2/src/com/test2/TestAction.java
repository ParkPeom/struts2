package com.test2;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;

public class TestAction extends ActionSupport
implements Preparable, ModelDriven<UserDTO> {
	
	// 스트럿츠는 이구조를 반드시 써줘야 한다.
	// dto 안에있는 내용은 userId,pwd,name 를 struts2가 알아서 넘겨준다.
	// 바로 modelDriven<UserDTO> 한테 UserDTO를 맡긴것이다.(니가 알아서 DTO를 관리를 해!)
	// dto가 객체이기떄문에 객체생성을 prepare메소드를 통해 객체를 생성함
	// getModel() 를 통해 dto를 modelDriven이 가지고 간다.
	
	private static final long serialVersionUID = 1L;
	private UserDTO dto;
	
	// getDTO()는
	// struts1 에서 쓰인 보드액션에서 
	// request.setAttribute("dto",dto)를 안써줘도
	// 자동으로 보내게 된다.
	public UserDTO getDto() {
		return dto;
	}

	@Override
	public void prepare() throws Exception {
		dto = new UserDTO();
	}

	@Override
	public UserDTO getModel() {
		return dto;
	}
	
	// 이제부터 내가 원하는 메소드 이름으로 쓰면 된다.
	public String created() throws Exception {	
		// 모드 체크 
		// dto의 널값과 equals로 널을 두번 비교해준다 
		// equals가 앞에 오면 안됨 
		if(dto==null||dto.getMode()==null||dto.getMode().equals("")) {	
			return INPUT; // 아까 SUCESS와 비슷 내장된 반환값 소문자 input이 들어가있음
		}
		
		// dto가 널이아닐때
		// request를 이렇게 만들면 됨
		HttpServletRequest request = ServletActionContext.getRequest();
	
		request.setAttribute("message", "스트럿츠 2...");
		
//		request.setAttribute("dto", dto); dto도 원래 이렇게 넘겨야되는데
		//위에 getDTO()떄문에 자동으로 넘김
		
		return SUCCESS; // 사용자가 값을 입력할때
	}
	
}
