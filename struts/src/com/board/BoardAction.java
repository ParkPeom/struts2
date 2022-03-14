package com.board;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.sql.Connection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import com.util.DBConn;
import com.util.MyUtil;

public class BoardAction extends DispatchAction{

	
	// ActionForward 오버라이딩 메소드명바꿀수있게
	public ActionForward write(ActionMapping mapping, ActionForm form, 
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			
		return mapping.findForward("created");
		
	}
	
	public ActionForward write_ok(ActionMapping mapping, ActionForm form, 
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		Connection conn = DBConn.getConnection();
		BoardDAO dao = new BoardDAO(conn);
		
		BoardForm f = (BoardForm)form;
		
		f.setNum(dao.getMaxNum() + 1);
		f.setIpAddr(request.getRemoteAddr());
		
		dao.insertData(f);
		
		return mapping.findForward("save");
		
	}
	
	public ActionForward list(ActionMapping mapping, ActionForm form, 
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		Connection conn = DBConn.getConnection();
		BoardDAO dao = new BoardDAO(conn);
		
		String cp = request.getContextPath();
		
		MyUtil myUtil = new MyUtil();
		
		String pageNum = request.getParameter("pageNum");

		int currentPage = 1;

		if(pageNum!=null)
			currentPage = Integer.parseInt(pageNum);

		String searchKey = request.getParameter("searchKey");
		String searchValue = request.getParameter("searchValue");

		if(searchValue==null||searchValue.equals("")) {
			searchKey = "subject";
			searchValue = "";
		}else {
			if(request.getMethod().equalsIgnoreCase("GET")) {
				searchValue = URLDecoder.decode(searchValue, "UTF-8");
			}
		}

		int dataCount = dao.getDataCount(searchKey, searchValue);

		int numPerPage = 5;
		int totalPage = myUtil.getPageCount(numPerPage, dataCount);

		if(currentPage>totalPage)
			currentPage = totalPage;

		int start = (currentPage-1)*numPerPage+1;
		int end = currentPage*numPerPage;

		List<BoardForm> lists =
				dao.getLists(start, end, searchKey, searchValue);

		String param = "";
		if(searchValue!=null&&!searchValue.equals("")) {
			param = "searchKey=" + searchKey;
			param+= "&searchValue=" + URLEncoder.encode(searchValue, "UTF-8");
		}

		String listUrl = cp + "/board.do?method=list";

		
		// ? -> & 로바꿈 (키와벨류값을 전달하기위해)
		if(!param.equals("")) {
			listUrl += "&" + param;
		}

		String pageIndexList =
				myUtil.pageIndexList(currentPage, totalPage, listUrl);

		String articleUrl = cp + "/board.do?method=article&pageNum=" + currentPage;

		
		if(!param.equals("")) {
			articleUrl += "&" + param;
		}

		//포워딩 페이지에 데이터 넘기기
		request.setAttribute("lists", lists);
		request.setAttribute("articleUrl", articleUrl);
		request.setAttribute("pageIndexList", pageIndexList);
		request.setAttribute("pageNum", pageNum);
		request.setAttribute("totalPage", totalPage);
		request.setAttribute("dataCount", dataCount);
		
		
		
		return mapping.findForward("list");
		
	}
	
	
	public ActionForward article(ActionMapping mapping, ActionForm form, 
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		Connection conn = DBConn.getConnection();
		BoardDAO dao = new BoardDAO(conn);
		
		int num = Integer.parseInt(request.getParameter("num"));
		String pageNum = request.getParameter("pageNum");
		
		String searchKey = request.getParameter("searchKey");
		String searchValue = request.getParameter("searchValue");
		
		if(searchValue != null) {
			
			searchValue = URLDecoder.decode(searchValue, "UTF-8");
		}
		
		dao.updateHitCount(num);
		
		BoardForm dto = dao.getReadData(num);
		
		
		if(dto==null) {
			
			return mapping.findForward("list");
			
		}
		
		int lineSu = dto.getContent().split("\n").length;
		
		dto.setContent(dto.getContent().replace("\n", "<br/>"));
		
		String param = "pageNum=" + pageNum;
		if(searchValue != null && !searchValue.equals("")) {
			
			param += "&searchKey=" + searchKey;
			param += "&searchValue=" + URLEncoder.encode(searchValue, "UTF-8");
			
		}
		
		// param으로 담지않은이유 > 다른곳에서 param을 쓰고있어서 	
		request.setAttribute("dto", dto);
		request.setAttribute("params", param);
		request.setAttribute("linSu", lineSu);
		request.setAttribute("pageNum", pageNum);
		
		return mapping.findForward("article");
	}
	
	
	public ActionForward updated(ActionMapping mapping, ActionForm form, 
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		
		Connection conn = DBConn.getConnection();
		BoardDAO dao = new BoardDAO(conn);
		
		int num = Integer.parseInt(request.getParameter("num"));
		String pageNum = request.getParameter("pageNum");
		
		String searchKey = request.getParameter("searchKey");
		String searchValue = request.getParameter("searchValue");
		
		if(searchValue != null) {
			searchValue = URLDecoder.decode(searchValue, "UTF-8");
		}
		
		BoardForm dto = dao.getReadData(num);
		
		if(dto==null) {
			return mapping.findForward("list");
			
		}
		
		String param = "pageNum=" + pageNum;
		
		if(searchValue != null&& !searchValue.equals("")) {
			param += "&searchKey=" + searchKey;
			param += "&searchValue=" + URLEncoder.encode(searchValue, "UTF-8");
		}
		
		//이제 데이터 넘김
		request.setAttribute("dto", dto);
		request.setAttribute("pageNum", pageNum);
		request.setAttribute("params", param);
		request.setAttribute("searchKey", searchKey);
		request.setAttribute("searchValue", searchValue);
		
		return mapping.findForward("updated");
	}
	
	
	public ActionForward updated_ok(ActionMapping mapping, ActionForm form, 
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		
		Connection conn = DBConn.getConnection();
		BoardDAO dao = new BoardDAO(conn);
		
		// getparameter 요청 = 나 이거 줘 ! 
		String pageNum = request.getParameter("pageNum");	
		String searchKey = request.getParameter("searchKey");
		String searchValue = request.getParameter("searchValue");
		
		
		// 5개의 데이터가 자동으로 넘어온다.
		// 다운캐스팅 해서 받아낸다.
		BoardForm f = (BoardForm)form;
		
		f.setNum(Integer.parseInt(request.getParameter("num")));
		
		
		//수정된 데이터를 보낸다
		dao.updateData(f);
		
		//되돌아올때
		// &붙여준다 밑에서 + 값을 보내기위해서 
		String param = "&pageNum=" + pageNum;
		
		if(searchValue != null&& !searchValue.equals("")) {
			param += "&searchKey=" + searchKey;
			param += "&searchValue=" + URLEncoder.encode(searchValue, "UTF-8");
		}
		
		// 데이터를 넘겨주면 됨
		//request.setAttribute("params", param);
		//request.setAttribute("pageNum", pageNum);
		
		// return mapping.findForward("updated_ok");	
		
		// 수정버튼 누르면 원래 검색페이지로 돌아오게 하기위한 방법
		// 파라미터 값을 보내려면 xml에서는 못보냄
		// 그래서 여기서 처리해줘야함
		ActionForward af = new ActionForward();
		af.setRedirect(true);
		af.setPath("/board.do?method=list"+ param);
		
		return af;
	}
	
	
	public ActionForward deleted_ok(ActionMapping mapping, ActionForm form, 
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		Connection conn = DBConn.getConnection();
		BoardDAO dao = new BoardDAO(conn);
		
		// pagenum , 설치키 , 설치 벨류 가져와 
			int num = Integer.parseInt(request.getParameter("num"));	
			String pageNum = request.getParameter("pageNum");
			String searchKey = request.getParameter("searchKey");
			String searchValue = request.getParameter("searchValue");
					
			dao.deleteData(num);
					
			// 돌아갈곳
			String param = "&pageNum="+pageNum;
					
			if(searchValue!=null&&!searchValue.equals("")) {
				param += "&searchKey=" + searchKey;
				param += "&searchValue=" + 
				URLEncoder.encode(searchValue,"UTF-8");
			}
		
		// 리스트로 바로간다.
		ActionForward af = new ActionForward();
		af.setRedirect(true);
		af.setPath("/board.do?method=list"+ param);
			
		return af;
		
	}
}
