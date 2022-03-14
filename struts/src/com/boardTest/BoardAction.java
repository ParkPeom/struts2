package com.boardTest;

import java.net.URLDecoder;
import java.net.URLEncoder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import com.util.MyUtil;
import com.util.dao.CommonDAO;
import com.util.dao.CommonDAOImpl;

public class BoardAction extends DispatchAction{
		
	public ActionForward created(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {			
		
		//넘어오는 모드를 받아야된다. mode만 넘어온다.
		String mode = request.getParameter("mode");
		
		// mode가 널이거나 mode가 update이거나 구분해줘야함
		if(mode==null) {
			// mode가 null일때
			request.setAttribute("mode", "insert");
		} else { 
			
			// mode가 update 이면 수정창으로 create 창을 띄우겠다
			// created 창에 데이터를 하나씩 넣어주면 된다.
			// 수정화면 띄우면 됨
			CommonDAO dao = CommonDAOImpl.getInstance();
			
			// Article넘겼으니까 수정버튼누르면 num 하고 pageNum 를 받아줘야함
			int num = Integer.parseInt(request.getParameter("num"));
			String pageNum = request.getParameter("pageNum");
			
			// num 으로 하나의 데이터를 읽어오면 됨
			BoardForm dto = 
					(BoardForm)dao.getReadData("board.readData",num);
			
			
			if(dto==null) {
				return mapping.findForward("list");
			}
		
			// 가지고 가야하는 데이터
			request.setAttribute("dto", dto);
			request.setAttribute("mode", "updateOK"); // 진짜 수정을 해야하므로 문자를 넣어서 넘김
			request.setAttribute("pageNum", pageNum);
		
		}
		
		// mode의 insert 가 넘어가는데 
		// 이게 created 문자를 가지고 가서 created.jsp로 넘어간다		
		// 입력일때 created.jsp로 가면 mode에는 insert가 들어감
		return mapping.findForward("created"); 
	
	
	}
	
	public ActionForward created_ok(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		CommonDAO dao = CommonDAOImpl.getInstance();	
		BoardForm f = (BoardForm)form;
		
		//------ mode의 값이 insert 가 왔을때 입력을 시켜준다-------
		String mode = request.getParameter("mode");
		
		if(mode.equals("insert")) {
			
			// sqlmap.xml에 있는 namespace.id 
			// boardTest_sqlMap.xml에있는 board 안에 있는 maxNum 호출
				
			int maxNum = dao.getIntValue("board.maxNum");
			f.setNum(maxNum + 1);
			f.setIpAddr(request.getRemoteAddr());
			
			// board안에 id값이 insertData 호출 
			// boardForm를 매개변수로 받아와야하므로 
			// 위에서 form을 f에 넣어줫으니 f를 줬다.
			dao.insertData("board.insertData", f);	 
	
			//------ mode 가 updateOk가 올때도 적어줘야 한다.-------
			// --------mode의 값이 updateOK 이면 수정을해주면 됨
		} else if (mode.equals("updateOK")) {
			
			// 수정
			String pageNum = request.getParameter("pageNum");
			
			// 다운캐스팅되어온 f를 넘겨주면됨
			// 5개의 데이터가 form 으로 넘어오게 되는걸 다운캐스팅된 f를 넘겨줌
			dao.updateData("board.updateData", f);
			
			// pageNum을 넘기기 위해서 세션으로 넘겨줘야함(board_test.xml에서 pageNum을 url로 넘겨줄수없으므로)
			// 세션으로 올려서 넘겨주면 됨 
			// 가볍게 created_ok로 가서 -> method = list를 실행함
			HttpSession session = request.getSession();
			session.setAttribute("pageNum", pageNum);
			
		}
		
		dao = null;
		
		return mapping.findForward("created_ok"); 
	}
	
	public ActionForward list(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
				
		CommonDAO dao = CommonDAOImpl.getInstance();
		
		String cp = request.getContextPath();
		MyUtil myUtil = new MyUtil();
		
		String pageNum = request.getParameter("pageNum");
		
		int currentPage = 1;
		
		// 위에 수정에서 session에 올려놓은 pageNum 받기
		
		HttpSession session = request.getSession();
		
		if(pageNum==null) {
			pageNum = (String)session.getAttribute("pageNum");
		}
		
		// 만약 세션을 받았다면 지워줘야 한다.
		// 수정이랑 create를 같이써서 데이터남은걸 지워줘야한다.
		session.removeAttribute("pageNum");
		
		
		
		if(pageNum!=null)
			currentPage = Integer.parseInt(pageNum);
		
		String searchKey = request.getParameter("searchKey");
		String searchValue = request.getParameter("searchValue");
		
		if(searchValue==null) {
			searchKey = "subject";
			searchValue = "";
		}else {
			if(request.getMethod().equalsIgnoreCase("GET")) {
				searchValue = URLDecoder.decode(searchValue, "UTF-8");
			}
		}
		
		
		Map<String, Object> hMap = new HashMap<>();
		hMap.put("searchKey", searchKey);
		hMap.put("searchValue", searchValue);
				
		
		// 전체 데이터 개수 구함 
		int dataCount = dao.getIntValue("board.dataCount", hMap);
		
		
		int numPerPage = 5;
		int totalPage = myUtil.getPageCount(numPerPage, dataCount);
		
		if(currentPage>totalPage)
			currentPage = totalPage;
		
		int start = (currentPage-1)*numPerPage+1;
		int end = currentPage*numPerPage;
		
		hMap.put("start", start);
		hMap.put("end", end);
		
		List<Object> lists = 
				dao.getListData("board.listData", hMap);
		
		String param = "";
		if(searchValue!=null&&!searchValue.equals("")) {
			param = "searchKey=" + searchKey;
			param+= "&searchValue=" + URLEncoder.encode(searchValue, "UTF-8");
		}
		
		String listUrl = cp + "/boardTest.do?method=list";
		
		if(!param.equals("")) {
			listUrl += "&" + param;
		}
		
		String pageIndexList = 
				myUtil.pageIndexList(currentPage, totalPage, listUrl);
		
		String articleUrl = 
				cp + "/boardTest.do?method=article&pageNum=" + currentPage;
		
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
		
		CommonDAO dao = CommonDAOImpl.getInstance();

		
		int num = Integer.parseInt(request.getParameter("num"));
		
		
		
		String pageNum = request.getParameter("pageNum");
		
		String searchKey = request.getParameter("searchKey");
		String searchValue = request.getParameter("searchValue");
		
		
		if(searchValue != null) {
			
			searchValue = URLDecoder.decode(searchValue, "UTF-8");
	
		} else {
			searchKey = "subject";
			searchValue = "";
		}
		
		// sql.map 호출한다 
		dao.updateData("board.hitCountUpdate", num);
		
											// 액션에서 여기서 불러옴 id 
		BoardForm dto = (BoardForm)dao.getReadData("board.readData", num);
			
		if(dto==null) {
			
			return mapping.findForward("list");
			
		}
		
		int lineSu = dto.getContent().split("\n").length;
		
		dto.setContent(dto.getContent().replace("\n", "<br/>"));
		
		// 이전글 다음글 
		
		String preUrl = "";
		String nextUrl = "";
		String cp = request.getContextPath();
		
		Map<String, Object> hMap = new HashMap<>();
		
		// 일단 3개 넣어놨다
		hMap.put("searchKey", searchKey);
		hMap.put("searchValue", searchValue);
		hMap.put("num", num);
		
		String preSubject = "";
		// num,subject를 preDTO에 담는다
		BoardForm preDTO = (BoardForm)dao.getReadData("board.preReadData",hMap);
		
		// 3번이면 이전글은 4번이 들어가게된다.
		// preUrl은 이전글을 구하게된다.
		if(preDTO!=null) {
			preUrl = cp + "/boardTest.do?method=article&pageNum=" + pageNum;
			preUrl += "&num=" + preDTO.getNum();
			preSubject = preDTO.getSubject();
		}
		
		
		String nextSubject = "";
		// num,subject를 preDTO에 담는다
		BoardForm nextDTO = (BoardForm)dao.getReadData("board.nextReadData",hMap);
		
		// nextUrl은 다음글을 구하게된다.
		if(preDTO!=null) {
			nextUrl = cp + "/boardTest.do?method=article&pageNum=" + pageNum;
			nextUrl += "&num=" + nextDTO.getNum();
			nextSubject = nextDTO.getSubject();
		}
		
		String urlList = cp + "/boardTest.do?method=list&pageNum=" + pageNum;
		
		if(!searchValue.equals("")) {
			// null이아니면 검색함
			searchValue = URLEncoder.encode(searchValue,"UTF-8");
			
			// 위에서 한번담아서 누적한다.
			urlList += "&searchKey=" + searchKey
					+"&searchValue=" + searchValue;
			
			if(!preUrl.equals("")) {
				preUrl += "&searchKey=" + searchKey
						+"&searchValue=" + searchValue;
			}
			
			if(!nextUrl.equals("")) {
				nextUrl += "&searchKey=" + searchKey
						+"&searchValue=" + searchValue;
			}
		}
		// 수정과 삭제에서 사용할 인수
		// 수정삭제할때 주소를 간단하게 만듬
		String paramArticle = "num=" + num + "&pageNum=" + pageNum;
		
		
	/*	String param = "pageNum=" + pageNum;
		if(searchValue != null && !searchValue.equals("")) {
			
			param += "&searchKey=" + searchKey;
			param += "&searchValue=" + URLEncoder.encode(searchValue, "UTF-8");
			
		}*/
		
		// param으로 담지않은이유 > 다른곳에서 param을 쓰고있어서 	
		request.setAttribute("dto", dto);
		request.setAttribute("preSubject", preSubject);
		request.setAttribute("preUrl", preUrl);
		request.setAttribute("nextSubject", nextSubject);
		request.setAttribute("nextUrl", nextUrl);
		request.setAttribute("linSu", lineSu);
		request.setAttribute("paramArticle", paramArticle);
		request.setAttribute("urlList", urlList);
		
		return mapping.findForward("article");
	}
		
		public ActionForward deleted(ActionMapping mapping, ActionForm form, 
				HttpServletRequest request, HttpServletResponse response)
				throws Exception {
			
			CommonDAO dao = CommonDAOImpl.getInstance();		
			int num = Integer.parseInt(request.getParameter("num"));
			
			String pageNum = request.getParameter("pageNum");
			
			// pageNum을 세션에서 넘겨줌
			dao.deleteData("board.deleteData", num);
			
			HttpSession session = request.getSession();
			
			session.setAttribute("pageNum", pageNum);
			
			return mapping.findForward("deleted");
		}
}






















