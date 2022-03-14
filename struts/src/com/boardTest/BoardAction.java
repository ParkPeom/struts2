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
		
		//�Ѿ���� ��带 �޾ƾߵȴ�. mode�� �Ѿ�´�.
		String mode = request.getParameter("mode");
		
		// mode�� ���̰ų� mode�� update�̰ų� �����������
		if(mode==null) {
			// mode�� null�϶�
			request.setAttribute("mode", "insert");
		} else { 
			
			// mode�� update �̸� ����â���� create â�� ���ڴ�
			// created â�� �����͸� �ϳ��� �־��ָ� �ȴ�.
			// ����ȭ�� ���� ��
			CommonDAO dao = CommonDAOImpl.getInstance();
			
			// Article�Ѱ����ϱ� ������ư������ num �ϰ� pageNum �� �޾������
			int num = Integer.parseInt(request.getParameter("num"));
			String pageNum = request.getParameter("pageNum");
			
			// num ���� �ϳ��� �����͸� �о���� ��
			BoardForm dto = 
					(BoardForm)dao.getReadData("board.readData",num);
			
			
			if(dto==null) {
				return mapping.findForward("list");
			}
		
			// ������ �����ϴ� ������
			request.setAttribute("dto", dto);
			request.setAttribute("mode", "updateOK"); // ��¥ ������ �ؾ��ϹǷ� ���ڸ� �־ �ѱ�
			request.setAttribute("pageNum", pageNum);
		
		}
		
		// mode�� insert �� �Ѿ�µ� 
		// �̰� created ���ڸ� ������ ���� created.jsp�� �Ѿ��		
		// �Է��϶� created.jsp�� ���� mode���� insert�� ��
		return mapping.findForward("created"); 
	
	
	}
	
	public ActionForward created_ok(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		CommonDAO dao = CommonDAOImpl.getInstance();	
		BoardForm f = (BoardForm)form;
		
		//------ mode�� ���� insert �� ������ �Է��� �����ش�-------
		String mode = request.getParameter("mode");
		
		if(mode.equals("insert")) {
			
			// sqlmap.xml�� �ִ� namespace.id 
			// boardTest_sqlMap.xml���ִ� board �ȿ� �ִ� maxNum ȣ��
				
			int maxNum = dao.getIntValue("board.maxNum");
			f.setNum(maxNum + 1);
			f.setIpAddr(request.getRemoteAddr());
			
			// board�ȿ� id���� insertData ȣ�� 
			// boardForm�� �Ű������� �޾ƿ;��ϹǷ� 
			// ������ form�� f�� �־�Z���� f�� ���.
			dao.insertData("board.insertData", f);	 
	
			//------ mode �� updateOk�� �ö��� ������� �Ѵ�.-------
			// --------mode�� ���� updateOK �̸� ���������ָ� ��
		} else if (mode.equals("updateOK")) {
			
			// ����
			String pageNum = request.getParameter("pageNum");
			
			// �ٿ�ĳ���õǾ�� f�� �Ѱ��ָ��
			// 5���� �����Ͱ� form ���� �Ѿ���� �Ǵ°� �ٿ�ĳ���õ� f�� �Ѱ���
			dao.updateData("board.updateData", f);
			
			// pageNum�� �ѱ�� ���ؼ� �������� �Ѱ������(board_test.xml���� pageNum�� url�� �Ѱ��ټ������Ƿ�)
			// �������� �÷��� �Ѱ��ָ� �� 
			// ������ created_ok�� ���� -> method = list�� ������
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
		
		// ���� �������� session�� �÷����� pageNum �ޱ�
		
		HttpSession session = request.getSession();
		
		if(pageNum==null) {
			pageNum = (String)session.getAttribute("pageNum");
		}
		
		// ���� ������ �޾Ҵٸ� ������� �Ѵ�.
		// �����̶� create�� ���̽Ἥ �����ͳ����� ��������Ѵ�.
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
				
		
		// ��ü ������ ���� ���� 
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
		
		//������ �������� ������ �ѱ��
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
		
		// sql.map ȣ���Ѵ� 
		dao.updateData("board.hitCountUpdate", num);
		
											// �׼ǿ��� ���⼭ �ҷ��� id 
		BoardForm dto = (BoardForm)dao.getReadData("board.readData", num);
			
		if(dto==null) {
			
			return mapping.findForward("list");
			
		}
		
		int lineSu = dto.getContent().split("\n").length;
		
		dto.setContent(dto.getContent().replace("\n", "<br/>"));
		
		// ������ ������ 
		
		String preUrl = "";
		String nextUrl = "";
		String cp = request.getContextPath();
		
		Map<String, Object> hMap = new HashMap<>();
		
		// �ϴ� 3�� �־����
		hMap.put("searchKey", searchKey);
		hMap.put("searchValue", searchValue);
		hMap.put("num", num);
		
		String preSubject = "";
		// num,subject�� preDTO�� ��´�
		BoardForm preDTO = (BoardForm)dao.getReadData("board.preReadData",hMap);
		
		// 3���̸� �������� 4���� ���Եȴ�.
		// preUrl�� �������� ���ϰԵȴ�.
		if(preDTO!=null) {
			preUrl = cp + "/boardTest.do?method=article&pageNum=" + pageNum;
			preUrl += "&num=" + preDTO.getNum();
			preSubject = preDTO.getSubject();
		}
		
		
		String nextSubject = "";
		// num,subject�� preDTO�� ��´�
		BoardForm nextDTO = (BoardForm)dao.getReadData("board.nextReadData",hMap);
		
		// nextUrl�� �������� ���ϰԵȴ�.
		if(preDTO!=null) {
			nextUrl = cp + "/boardTest.do?method=article&pageNum=" + pageNum;
			nextUrl += "&num=" + nextDTO.getNum();
			nextSubject = nextDTO.getSubject();
		}
		
		String urlList = cp + "/boardTest.do?method=list&pageNum=" + pageNum;
		
		if(!searchValue.equals("")) {
			// null�̾ƴϸ� �˻���
			searchValue = URLEncoder.encode(searchValue,"UTF-8");
			
			// ������ �ѹ���Ƽ� �����Ѵ�.
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
		// ������ �������� ����� �μ�
		// ���������Ҷ� �ּҸ� �����ϰ� ����
		String paramArticle = "num=" + num + "&pageNum=" + pageNum;
		
		
	/*	String param = "pageNum=" + pageNum;
		if(searchValue != null && !searchValue.equals("")) {
			
			param += "&searchKey=" + searchKey;
			param += "&searchValue=" + URLEncoder.encode(searchValue, "UTF-8");
			
		}*/
		
		// param���� ������������ > �ٸ������� param�� �����־ 	
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
			
			// pageNum�� ���ǿ��� �Ѱ���
			dao.deleteData("board.deleteData", num);
			
			HttpSession session = request.getSession();
			
			session.setAttribute("pageNum", pageNum);
			
			return mapping.findForward("deleted");
		}
}






















