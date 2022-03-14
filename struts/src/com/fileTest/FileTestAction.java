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
		
		// 업로드 시켜보자	
		CommonDAO dao = CommonDAOImpl.getInstance();
			
		// 오리지널 경로를 쓰려면 세션이 필요
		HttpSession session = request.getSession();
			
		// root = "/"
		String root = session.getServletContext().getRealPath("/");
		
		// 실제 경로(파일저장되는 경로)
		String savePath = root + "pds" + File.separator + "saveFile";

		// 넘어오는 데이터는 FileTestForm에 있다
		FileTestForm f = (FileTestForm)form;
		
		// 파일에 업로드 하고 파일정보를 db에 넣기
		// 파일업로드
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
		
		// 5개의 데이터를 꺼낸다.
		// num을 가지고 일련번호를 만들것이다.
		List<Object> lists = 
				(List<Object>)dao.getListData("fileTest.listData",hMap);

		// 꺼낸다.
		Iterator<Object> it = lists.iterator();
		
		int listNum,n=0;
		String str;
		
		// 리스트의 내용을 하나씩 꺼낸다 데이터타입이 Object이므로 다운캐스팅
		while(it.hasNext()) {
			
			FileTestForm dto = (FileTestForm)it.next();
			// dto 에는 num savefile originalfile 등이 있다
			
			// 일련번호 만드는 공식 
			// 1페이지 5개가 있으면 start 1 부터 시작해서 
			// n은 0이들어가있어서 
			// 5 - ( 1 + 0 - 1); 
			// 5 - 0 = 5  제일첫번째 데이터는 5가 들어간다.
			// 다음 n은 증감연산자로인해 1이 된다.
			
				
			// 일련번호(listNum)은 5, 4 , 3 , 2 , 1
			listNum = totalDataCount - (start + n - 1);
			
			dto.setListNum(listNum);
			
			n++;
			
			// 파일 다운 경로
			// DTO에 주소를 미리 넣어놓는다 
			str = cp + "/fileTest.do?method=download&num=" + dto.getNum();
			
			// 각각의 dto에 url 의 자기자신의 num으로 들어가게된다.
			// UrlFile 다운로드 경로 
			dto.setUrlFile(str);
		}
			
		// 다운로드 경로
		String urlList = cp + "/fileTest.do?method=list";
		
		// lists를 넘긴다.		
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
		
		// 실제 경로
		String savePath = root + "pds" + File.separator + "saveFile";
		
		int num = Integer.parseInt(request.getParameter("num"));
		// num으로 하나의 데이터를 가져온다
		
		// 세이브파일과 오리지널 파일이 필요하기 때문에
		FileTestForm dto =
				(FileTestForm)dao.getReadData("fileTest.readData",num);
		
		
		
		// 저장된파일의 이름으로 삭제한다.
		FileManager.doFileDelete(dto.getSaveFileName(), savePath);
		
	
		// db에 있는 내용을 지운다.
		dao.deleteData("fileTest.deleteData", num);
		
		return mapping.findForward("delete_ok");
	}
	
	// 다운로드
	public ActionForward download(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		CommonDAO dao = CommonDAOImpl.getInstance();
		
		HttpSession session = request.getSession();
			
		String root = session.getServletContext().getRealPath("/");
		
		// 실제 경로
		String savePath = root + "pds" + File.separator + "saveFile";
		
		int num = Integer.parseInt(request.getParameter("num"));
		
		// num으로 하나의 데이터를 가져온다.
		FileTestForm dto = 
				(FileTestForm)dao.getReadData("fileTest.readData", num);
		
		if(dto==null) {
			return mapping.findForward("list");
		}
		
		// 다운로드 한다.
		boolean flag = FileManager.doFileDownload(response, dto.getSaveFileName(),
				dto.getOriginalFileName(), savePath);	
		if(!flag) {
			// flag값이 있지않으면
			response.setContentType("text/html;charset=utf-8");

			// 출력객체 생성
			PrintWriter out = response.getWriter();	
			out.print("<script type='text/javascript'>");
			out.print("alert('다운로드 에러!!');");
			out.print("history.back()");
			out.print("</script>");
		}
		// null 이면 아무대도 안가고 멈춰있는다.
		return mapping.findForward(null);	
	}	
}
