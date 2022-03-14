package com.join;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import com.util.dao.CommonDAO;
import com.util.dao.CommonDAOImpl;

public class MemberAction extends ActionSupport
implements Preparable, ModelDriven<MemberDTO>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private MemberDTO dto;
	
	private String message;
	
	@Override
	public MemberDTO getModel() {
		return dto;
	}

	@Override
	public void prepare() throws Exception {
		
	}
	
	public MemberDTO getDTO() {
		return dto;
	}
	
	public String getMessage() {
		return message;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}
	
	//ȸ������
		public String created() throws Exception {
			
			if(dto==null||dto.getMode()==null||dto.getMode().equals("")) {
				return INPUT;
			}
			HttpServletRequest request = ServletActionContext.getRequest();
			CommonDAO dao = CommonDAOImpl.getInstance();
			
			//�Է�
			if(dao.getReadData("member.getId", dto)==null) {
				
				dao.insertData("member.insertData", dto);
				dao=null;
			}else {
				request.setAttribute("message", "������ ���̵� �����մϴ�!!");
				
				return INPUT;
			}
			return SUCCESS;
		}

	public String login() throws IOException {
		
		HttpServletRequest request = ServletActionContext.getRequest();
		
		CommonDAO dao = CommonDAOImpl.getInstance();
		MemberDTO loginDTO = (MemberDTO)dao.getReadData("join.getReadData",dto);
		
			if(loginDTO==null) {
				request.setAttribute("message", "���̵� �Ǵ� �н����带 �Է����ּ���");
				return INPUT;
			}
		request.setAttribute("JoinDTO", loginDTO);
		return SUCCESS;
	}
	
	public String logout() throws Exception {
		
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		
		session.removeAttribute("dto");
		session.removeAttribute("JoinDTO");
		session.invalidate();	
		return SUCCESS;
	}
}
