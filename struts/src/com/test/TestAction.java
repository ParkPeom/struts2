package com.test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

// 일반클래스는 액션의 역할을 하기 위해 Action 상속받음
public class TestAction extends Action {

	@Override
	public ActionForward execute(ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response) throws Exception {
	
		String uri = request.getRequestURI();
		
		if(uri.indexOf("/test_ok.do")!=-1) {
			
			TestForm f = (TestForm)form; // object - > TestForm 
						
			request.setAttribute("vo", f); 
			
			return mapping.findForward("ok"); // ok라는 문자를 가지고 돌아가라
		}
		return mapping.findForward("error"); // error라는 글자를 가지고 돌아가라
	}
}
