package com.board;

import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import com.util.MyUtil;
import com.util.dao.CommonDAO;
import com.util.dao.CommonDAOImpl;

public class BoardAction extends ActionSupport
implements Preparable, ModelDriven<BoardDTO>{

	private static final long serialVersionUID = 1L;

	private BoardDTO dto;
	
	
	// getDto 가 modelDriven에 의해서 원하는곳으로 넘어간다.
	public BoardDTO getDto() {
		return dto;
	}

	@Override
	public BoardDTO getModel() {
		return dto;
	}

	@Override
	public void prepare() throws Exception {
		// modelDriven이 객체를 넘기려면 객체를 생성해야한다.
		dto = new BoardDTO();
	}
	
	
	// 사용자가 created메소드로 찾아오면
	// 입력할때 쓸것인가
	// 겉모습만 보여줄것인가
	public String created() throws IOException {
		
		HttpServletRequest request = ServletActionContext.getRequest();
		
		// 앞뒤가 바뀌면 안된다. equals가 뒤에 와야된다.
		
		// 게시물 입력창을 띄운다.
		if(dto==null||dto.getMode()==null||dto.getMode().equals("")) {
			
			request.setAttribute("mode", "create");
			
			return INPUT;
		}
		
		// dto가 null이 아니면 입력이 된것이다.
		
		// 입력 코딩은 좀있다가 
		
		CommonDAO dao = CommonDAOImpl.getInstance();
		
		int maxBoardNum = dao.getIntValue("bbs.maxBoardNum");
		
		// 처음들어가는 데이터 
		dto.setBoardNum(maxBoardNum + 1);
		dto.setIpAddr(request.getRemoteAddr());
		dto.setGroupNum(dto.getBoardNum());
		dto.setDepth(0); 
		dto.setOrderNo(0);
		dto.setParent(0);
		
		dao.insertData("bbs.insertData", dto);
		
		return SUCCESS;
	}
	
	// 리스트 띄우기
	public String list() throws IOException {
		
		CommonDAO dao = CommonDAOImpl.getInstance();
		MyUtil myUtil = new MyUtil();
		
		
		// request 사용하기 위해  생성 
		HttpServletRequest request = ServletActionContext.getRequest();
		
		String cp = request.getContextPath();
		
		int numPerPage = 10;
		int totalPage = 0;
		int totalDataCount = 0;
		
		int currentPage = 1;
		
		if(dto.getPageNum()!=null && !dto.getPageNum().equals("")) {
			currentPage = Integer.parseInt(dto.getPageNum());
		}
		
		if(dto.getSearchValue()==null||dto.getSearchValue().equals("")) {
			dto.setSearchKey("subject");
			dto.setSearchValue("");
		}
	
		if(request.getMethod().equalsIgnoreCase("GET")) {
			dto.setSearchValue(URLDecoder.decode(dto.getSearchValue(),"UTF-8"));
		}
		// 전체페이지 MAP 필요
		Map<String, Object> hMap = new HashMap<String,Object>();
		hMap.put("searchKey", dto.getSearchKey());
		hMap.put("searchValue", dto.getSearchValue());
		
		// 전체 데이터 개수 구하기
		totalDataCount = dao.getIntValue("bbs.dataCount",hMap);
		
		if(totalDataCount!=0) {
			totalPage = myUtil.getPageCount(numPerPage, totalDataCount);
		}
		// 삭제가 있을때 문제가 생길때
		if(currentPage > totalPage) {
			currentPage = totalPage;
		}
		
		int start = (currentPage-1)*numPerPage+1;
		int end = currentPage*numPerPage;
		
		hMap.put("start", start);
		hMap.put("end", end);
		
		// hamp에는 4개가 들어가있다 키와벨류start,end
		
		List<Object> lists = (List<Object>)dao.getListData("bbs.listData",hMap);
		
		
		// 일련번호를 만들기
		int listNum,n=0;
		
		Iterator<Object> it = lists.iterator();
		
		while(it.hasNext()) {
			// dto를 하나꺼내서 만들어서 vo에 listNum에 넣어준다.
			BoardDTO vo = (BoardDTO)it.next();
			listNum = totalDataCount-(start+n-1);
			vo.setListNum(listNum); // 삭제가 되도 일련번호가 만들어짐
			n++;
			
		}
		String param = "";
		String urlList = "";
		String urlArticle = "";
		
		// 널이 아니면 검색을 했다.
		if(!dto.getSearchValue().equals("")) {
			
			param = "searchKey=" + dto.getSearchKey();
			param += "&searchValue="
					+ URLEncoder.encode(dto.getSearchValue(),"UTF-8");
		}
		
		urlList = cp + "/bbs/list.action";
		urlArticle = cp + "/bbs/article.action?pageNum="+currentPage;
		
		// param 널이 아니면 검색이 됐다.
		if(!param.equals("")) {
			
			urlList += "?" + param;
			urlArticle += "&" + param;
		}
		
		// 데이터 넘김
		// 이 내개 데이터와 success를 가지고 돌아감
		request.setAttribute("lists", lists);
		request.setAttribute("totalDataCount", totalDataCount);
		request.setAttribute("pageIndexList",
				myUtil.pageIndexList(currentPage, totalPage, urlList));
		
		request.setAttribute("urlArticle", urlArticle);
		return SUCCESS;
	}
	
	// 클릭해서 dto는 자동으로 넘어온다. 
	public String article() throws Exception {
		
		HttpServletRequest request = ServletActionContext.getRequest();
		CommonDAO dao = CommonDAOImpl.getInstance();
		
		
		// 스트럿츠2가 자동으로 dto를 넘겨줘서 pageNum과 boardNum이 나오게 된다.
		/*System.out.print(dto.getSearchKey());
		System.out.print(dto.getSearchValue());
		System.out.print(dto.getPageNum());
		System.out.print(dto.getBoardNum());*/
		
		// dao에서 하나의 데이터를 가져올것이다.
		// 넘어오는값을 변수에 빼놓는다.
		String searchKey = dto.getSearchKey();
		String searchValue = dto.getSearchValue();
		String pageNum = dto.getPageNum();
		int boardNum = dto.getBoardNum();
		
		if(searchValue==null||searchValue.equals("")) {
			searchKey = "subject";
			searchValue = "";
		}
		if(request.getMethod().equalsIgnoreCase("GET")) {
			searchValue = URLDecoder.decode(searchValue, "UTF-8");
		}
		
		// 조회수 증가 
		dao.updateData("bbs.hitCountUpdate", boardNum);
		
		// 하나의 데이터를 읽어와서 dto에 넣어준다.
		dto = (BoardDTO)dao.getReadData("bbs.readData",boardNum);
		
		// nullnullnull3  검색을 했는데도 key,value,pagenum 없어짐..
		// 위에는 있어서 출력이 됐는데
		// 진행하다 보니까 자동으로 넘어온 dto가 이 밑에서 
		// getReadData에 의해서 기존에 있는게 덮어져서 초기화됨
		// 맨 마지막에 boardNum만 보이게 된다.
		
		// 우리가 별도로 자동으로 넘어온 dto를 빼놔서
		// 아래에서 사용을 하게되는것이다.
		/*System.out.print(dto.getSearchKey());
		System.out.print(dto.getSearchValue());
		System.out.print(dto.getPageNum());
		System.out.print(dto.getBoardNum());*/
		
		if(dto==null) {
			return "read-error";
		}
		
		// dto에서 꺼냈으니 content는 있으나 키벨류페이지넘은 초기화되어있음..
		int lineSu = dto.getContent().split("\r\n").length;
		
		dto.setContent(dto.getContent().replaceAll("\r\n", "<br/>"));
		
		// 이전글 다음글보낼것임
		Map<String, Object> hMap = new HashMap<>();
//		hMap.put("searchKey", dto.getSearchKey()); 이렇게 넣어주면안됨 위에 자동으로 넣은 dto sk,sv,pn 이 초기화 되어있어서
											// 널이 들어가면 검색이 안된다..
											
		hMap.put("searchKey", searchKey); // 그럼 여기다가 위에서 dto.getSearchKey 로
										  // 받은 searchKey변수 를 써줘야 한다.
		hMap.put("searchValue", searchValue);
		hMap.put("groupNum", dto.getGroupNum());
		hMap.put("orderNo", dto.getOrderNo());
		
		// 이전글
		BoardDTO preDTO = (BoardDTO)dao.getReadData("bbs.preReadData",hMap);
		int preBoardNum = 0;
		String preSubject = "";
		if(preDTO!=null) {
			preBoardNum = preDTO.getBoardNum();
			preSubject = preDTO.getSubject();
		}
		
		BoardDTO nextDTO = (BoardDTO)dao.getReadData("bbs.nextReadData",hMap);
		int nextBoardNum = 0;
		String nextSubject = "";
		if(nextDTO!=null) {
			nextBoardNum = nextDTO.getBoardNum();
			nextSubject = nextDTO.getSubject();
		}
		
		// 넘어가는 주소
		String params = "pageNum=" + pageNum;
		if(!searchValue.equals("")) {
			params += "&searchKey=" + searchKey;
			params += "&searchValue="
					+ URLEncoder.encode(searchValue,"UTF-8");
		}
		
		request.setAttribute("preBoardNum", preBoardNum);
		request.setAttribute("preSubject",preSubject);
		request.setAttribute("nextBoardNum", nextBoardNum);
		request.setAttribute("nextSubject",nextSubject);
		
		request.setAttribute("params",params);
		request.setAttribute("lineSu",lineSu);
		request.setAttribute("pageNum",pageNum);
		
		// 이제 sql에 이전글 다음글로 넘어가게 된다.
		return SUCCESS;	
	}
	
	public String updated() throws Exception {
		
		// 게시물 수정 화면 
		HttpServletRequest request = ServletActionContext.getRequest();
		CommonDAO dao = CommonDAOImpl.getInstance();
		
		if(dto.getMode()==null||dto.getMode().equals("")) {
			
			dto = (BoardDTO)dao.getReadData("bbs.readData",dto.getBoardNum()); // 하나의 데이터 읽어옴
		
			if(dto==null) {
				return "read-error";
			}
			
			request.setAttribute("mode", "update"); 
			request.setAttribute("pageNum", dto.getPageNum());
			
			return INPUT;
		}
		// 게시물 수정 
		
		// DTO는 자동으로 넘어감
		
		dao.updateData("bbs.updateData", dto);
		
		return SUCCESS;
		
	}
	
	// 답변
	public String reply() throws Exception {
		
		HttpServletRequest request = ServletActionContext.getRequest();
		
		CommonDAO dao = CommonDAOImpl.getInstance();
		
		if(dao==null||dto.getMode()==null||dto.getMode().equals("")) {
			
			// 부모의 데이터 읽기
			dto = (BoardDTO)dao.getReadData("bbs.readData",dto.getBoardNum());
			
			if(dto==null) {
				return "read-error";
			}
			
			 // 부모의 컨텐츠를 먼저 보여주고 줄을 귿고 내가 쓰고싶은글을쓴다
			String temp = "\r\n\r\n-----------------------------\r\n\r\n";
			temp += "[답변]\r\n";
			
			//부모데이터를 변경해서 내 데이터로 created.jsp에 출력
			dto.setSubject("[답변]" + dto.getSubject()); // 부모꺼로 바꿈
			dto.setContent(dto.getContent() + temp); // 위에 만든 temp를 붙임
			dto.setName(""); // name은 내꺼쓸수있게 부모꺼 지울거임
			dto.setEmail(""); // 내꺼쓸수있게 부모꺼 지울거임
			dto.setPwd(""); //  내꺼쓸수있게 부모꺼 지울거임
			
			
			request.setAttribute("mode", "reply");
			request.setAttribute("pageNum", dto.getPageNum()); 
			
			return INPUT;
		}
		
		// 답변입력 하기전에 orderNum 수정
		// ordeNoUpdate 를 넘겨줘야 한다.  orderNo 1 증가시켜줘야함
		Map<String, Object> hMap = new HashMap<>();
		
		// 위에서 부모의 dto가 자동으로 넘어온다.
		hMap.put("groupNum", dto.getGroupNum()); // 부모의 groupNum
		hMap.put("orderNo", dto.getOrderNo()); // 부모의 orderNo 
		
		dao.updateData("bbs.orderNoUpdate", hMap); // 부모껄 넘기면  orderNo 를 +1 해줌 
		
		// 입력 
		int maxBoardNum = dao.getIntValue("bbs.maxBoardNum");
		
		dto.setBoardNum(maxBoardNum + 1);
		dto.setIpAddr(request.getRemoteAddr());   // 새로운 댓글을 다는 사람의 ip를 넣는다 클라이언트 ip를 넣는다.
		dto.setDepth(dto.getDepth() + 1); // 부모가 가지고 있는 depth 에 + 1해야지 들여쓰기됨 
		dto.setOrderNo(dto.getOrderNo() + 1); // 입력된 데이터에 내 부모의 orderNo에 + 1해줌 ( 현재 입력되는 데이터의 orderNo )		
		
		
		// 실제 데이터에 입력
		dao.insertData("bbs.insertData", dto);
		
		
		return SUCCESS;
	}
	
	
	public String deleted() throws Exception {
		
		CommonDAO dao = CommonDAOImpl.getInstance();
		
		dao.deleteData("bbs.deleteData", dto.getBoardNum());
		
		return SUCCESS;
	}
}
