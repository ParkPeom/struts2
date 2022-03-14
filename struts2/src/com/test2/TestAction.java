package com.test2;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;

public class TestAction extends ActionSupport
implements Preparable, ModelDriven<UserDTO> {
	
	// ��Ʈ������ �̱����� �ݵ�� ����� �Ѵ�.
	// dto �ȿ��ִ� ������ userId,pwd,name �� struts2�� �˾Ƽ� �Ѱ��ش�.
	// �ٷ� modelDriven<UserDTO> ���� UserDTO�� �ñ���̴�.(�ϰ� �˾Ƽ� DTO�� ������ ��!)
	// dto�� ��ü�̱⋚���� ��ü������ prepare�޼ҵ带 ���� ��ü�� ������
	// getModel() �� ���� dto�� modelDriven�� ������ ����.
	
	private static final long serialVersionUID = 1L;
	private UserDTO dto;
	
	// getDTO()��
	// struts1 ���� ���� ����׼ǿ��� 
	// request.setAttribute("dto",dto)�� �Ƚ��൵
	// �ڵ����� ������ �ȴ�.
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
	
	// �������� ���� ���ϴ� �޼ҵ� �̸����� ���� �ȴ�.
	public String created() throws Exception {	
		// ��� üũ 
		// dto�� �ΰ��� equals�� ���� �ι� �����ش� 
		// equals�� �տ� ���� �ȵ� 
		if(dto==null||dto.getMode()==null||dto.getMode().equals("")) {	
			return INPUT; // �Ʊ� SUCESS�� ��� ����� ��ȯ�� �ҹ��� input�� ������
		}
		
		// dto�� ���̾ƴҶ�
		// request�� �̷��� ����� ��
		HttpServletRequest request = ServletActionContext.getRequest();
	
		request.setAttribute("message", "��Ʈ���� 2...");
		
//		request.setAttribute("dto", dto); dto�� ���� �̷��� �ѰܾߵǴµ�
		//���� getDTO()������ �ڵ����� �ѱ�
		
		return SUCCESS; // ����ڰ� ���� �Է��Ҷ�
	}
	
}
