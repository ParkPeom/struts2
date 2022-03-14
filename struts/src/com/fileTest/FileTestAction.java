package com.fileTest;

import java.io.File;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import com.util.FileManager;
import com.util.MyUtil;
import com.util.dao.CommonDAO;
import com.util.dao.CommonDAOImpl;

public class FileTestAction extends DispatchAction {
	
	public ActionForward write(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		return mapping.findForward("write");
	}
	
	public ActionForward write_ok(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		// ���ε� ���Ѻ���	
		CommonDAO dao = CommonDAOImpl.getInstance();
			
		// �������� ��θ� ������ ������ �ʿ�
		HttpSession session = request.getSession();
			
		// root = "/"
		String root = session.getServletContext().getRealPath("/");
		
		// ���� ���(��������Ǵ� ���)
		String savePath = root + "pds" + File.separator + "saveFile";

		// �Ѿ���� �����ʹ� FileTestForm�� �ִ�
		FileTestForm f = (FileTestForm)form;
		
		// ���Ͽ� ���ε� �ϰ� ���������� db�� �ֱ�
		// ���Ͼ��ε�
		String newFileName = 
				FileManager.doFileUpload(f.getUpload(), savePath);
		
		if(newFileName!=null) {
			int maxNum = dao.getIntValue("fileTest.maxNum");
			
			f.setNum(maxNum + 1);
			f.setSaveFileName(newFileName);
			f.setOriginalFileName(f.getUpload().getFileName());
			dao.insertData("fileTest.insertData",f);
		}
		return mapping.findForward("write_ok");
	}
	
	public ActionForward list(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		CommonDAO dao = CommonDAOImpl.getInstance();
		
		MyUtil myUtil = new MyUtil();
		
		String cp = request.getContextPath();
		
		int numPerPage = 5;
		int totalPage = 0;
		int totalDataCount = 0;
		
		String pageNum = request.getParameter("pageNum");
		
		int currentPage = 1;
		
		if(pageNum!=null && !pageNum.equals("")) {
			currentPage = Integer.parseInt(pageNum);
		}
		
		
		
		totalDataCount = dao.getIntValue("fileTest.dataCount");
		
			
		if(totalDataCount!=0) {
			totalPage = myUtil.getPageCount(numPerPage, totalDataCount);
			
		}
		
		if(currentPage > totalPage) {
			currentPage = totalPage;
		}
		
		Map<String, Object> hMap = new HashMap<String, Object>();
		
		int start = (currentPage-1)*numPerPage+1;
		int end = currentPage*numPerPage;
		
		hMap.put("start", start);
		hMap.put("end", end);
		
		// 5���� �����͸� ������.
		// num�� ������ �Ϸù�ȣ�� ������̴�.
		List<Object> lists = 
				(List<Object>)dao.getListData("fileTest.listData",hMap);

		// ������.
		Iterator<Object> it = lists.iterator();
		
		int listNum,n=0;
		String str;
		
		// ����Ʈ�� ������ �ϳ��� ������ ������Ÿ���� Object�̹Ƿ� �ٿ�ĳ����
		while(it.hasNext()) {
			
			FileTestForm dto = (FileTestForm)it.next();
			// dto ���� num savefile originalfile ���� �ִ�
			
			// �Ϸù�ȣ ����� ���� 
			// 1������ 5���� ������ start 1 ���� �����ؼ� 
			// n�� 0�̵��־ 
			// 5 - ( 1 + 0 - 1); 
			// 5 - 0 = 5  ����ù��° �����ʹ� 5�� ����.
			// ���� n�� ���������ڷ����� 1�� �ȴ�.
			
				
			// �Ϸù�ȣ(listNum)�� 5, 4 , 3 , 2 , 1
			listNum = totalDataCount - (start + n - 1);
			
			dto.setListNum(listNum);
			
			n++;
			
			// ���� �ٿ� ���
			// DTO�� �ּҸ� �̸� �־���´� 
			str = cp + "/fileTest.do?method=download&num=" + dto.getNum();
			
			// ������ dto�� url �� �ڱ��ڽ��� num���� ���Եȴ�.
			// UrlFile �ٿ�ε� ��� 
			dto.setUrlFile(str);
		}
			
		// �ٿ�ε� ���
		String urlList = cp + "/fileTest.do?method=list";
		
		// lists�� �ѱ��.		
		request.setAttribute("lists", lists);
		request.setAttribute("pageNum", pageNum);
		request.setAttribute("totalDataCount", totalDataCount);
		request.setAttribute("pageIndexList",
				myUtil.pageIndexList(currentPage, totalPage, urlList));
		return mapping.findForward("list");
	}
	
	// method=delete
	public ActionForward delete(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		CommonDAO dao = CommonDAOImpl.getInstance();
		
		HttpSession session = request.getSession();
		
		String root = session.getServletContext().getRealPath("/");
		
		// ���� ���
		String savePath = root + "pds" + File.separator + "saveFile";
		
		int num = Integer.parseInt(request.getParameter("num"));
		// num���� �ϳ��� �����͸� �����´�
		
		// ���̺����ϰ� �������� ������ �ʿ��ϱ� ������
		FileTestForm dto =
				(FileTestForm)dao.getReadData("fileTest.readData",num);
		
		
		
		// ����������� �̸����� �����Ѵ�.
		FileManager.doFileDelete(dto.getSaveFileName(), savePath);
		
	
		// db�� �ִ� ������ �����.
		dao.deleteData("fileTest.deleteData", num);
		
		return mapping.findForward("delete_ok");
	}
	
	// �ٿ�ε�
	public ActionForward download(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		CommonDAO dao = CommonDAOImpl.getInstance();
		
		HttpSession session = request.getSession();
			
		String root = session.getServletContext().getRealPath("/");
		
		// ���� ���
		String savePath = root + "pds" + File.separator + "saveFile";
		
		int num = Integer.parseInt(request.getParameter("num"));
		
		// num���� �ϳ��� �����͸� �����´�.
		FileTestForm dto = 
				(FileTestForm)dao.getReadData("fileTest.readData", num);
		
		if(dto==null) {
			return mapping.findForward("list");
		}
		
		// �ٿ�ε� �Ѵ�.
		boolean flag = FileManager.doFileDownload(response, dto.getSaveFileName(),
				dto.getOriginalFileName(), savePath);	
		if(!flag) {
			// flag���� ����������
			response.setContentType("text/html;charset=utf-8");

			// ��°�ü ����
			PrintWriter out = response.getWriter();	
			out.print("<script type='text/javascript'>");
			out.print("alert('�ٿ�ε� ����!!');");
			out.print("history.back()");
			out.print("</script>");
		}
		// null �̸� �ƹ��뵵 �Ȱ��� �����ִ´�.
		return mapping.findForward(null);	
	}	
}
