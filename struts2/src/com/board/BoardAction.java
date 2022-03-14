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
	
	
	// getDto �� modelDriven�� ���ؼ� ���ϴ°����� �Ѿ��.
	public BoardDTO getDto() {
		return dto;
	}

	@Override
	public BoardDTO getModel() {
		return dto;
	}

	@Override
	public void prepare() throws Exception {
		// modelDriven�� ��ü�� �ѱ���� ��ü�� �����ؾ��Ѵ�.
		dto = new BoardDTO();
	}
	
	
	// ����ڰ� created�޼ҵ�� ã�ƿ���
	// �Է��Ҷ� �����ΰ�
	// �Ѹ���� �����ٰ��ΰ�
	public String created() throws IOException {
		
		HttpServletRequest request = ServletActionContext.getRequest();
		
		// �յڰ� �ٲ�� �ȵȴ�. equals�� �ڿ� �;ߵȴ�.
		
		// �Խù� �Է�â�� ����.
		if(dto==null||dto.getMode()==null||dto.getMode().equals("")) {
			
			request.setAttribute("mode", "create");
			
			return INPUT;
		}
		
		// dto�� null�� �ƴϸ� �Է��� �Ȱ��̴�.
		
		// �Է� �ڵ��� ���ִٰ� 
		
		CommonDAO dao = CommonDAOImpl.getInstance();
		
		int maxBoardNum = dao.getIntValue("bbs.maxBoardNum");
		
		// ó������ ������ 
		dto.setBoardNum(maxBoardNum + 1);
		dto.setIpAddr(request.getRemoteAddr());
		dto.setGroupNum(dto.getBoardNum());
		dto.setDepth(0); 
		dto.setOrderNo(0);
		dto.setParent(0);
		
		dao.insertData("bbs.insertData", dto);
		
		return SUCCESS;
	}
	
	// ����Ʈ ����
	public String list() throws IOException {
		
		CommonDAO dao = CommonDAOImpl.getInstance();
		MyUtil myUtil = new MyUtil();
		
		
		// request ����ϱ� ����  ���� 
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
		// ��ü������ MAP �ʿ�
		Map<String, Object> hMap = new HashMap<String,Object>();
		hMap.put("searchKey", dto.getSearchKey());
		hMap.put("searchValue", dto.getSearchValue());
		
		// ��ü ������ ���� ���ϱ�
		totalDataCount = dao.getIntValue("bbs.dataCount",hMap);
		
		if(totalDataCount!=0) {
			totalPage = myUtil.getPageCount(numPerPage, totalDataCount);
		}
		// ������ ������ ������ ���涧
		if(currentPage > totalPage) {
			currentPage = totalPage;
		}
		
		int start = (currentPage-1)*numPerPage+1;
		int end = currentPage*numPerPage;
		
		hMap.put("start", start);
		hMap.put("end", end);
		
		// hamp���� 4���� ���ִ� Ű�ͺ���start,end
		
		List<Object> lists = (List<Object>)dao.getListData("bbs.listData",hMap);
		
		
		// �Ϸù�ȣ�� �����
		int listNum,n=0;
		
		Iterator<Object> it = lists.iterator();
		
		while(it.hasNext()) {
			// dto�� �ϳ������� ���� vo�� listNum�� �־��ش�.
			BoardDTO vo = (BoardDTO)it.next();
			listNum = totalDataCount-(start+n-1);
			vo.setListNum(listNum); // ������ �ǵ� �Ϸù�ȣ�� �������
			n++;
			
		}
		String param = "";
		String urlList = "";
		String urlArticle = "";
		
		// ���� �ƴϸ� �˻��� �ߴ�.
		if(!dto.getSearchValue().equals("")) {
			
			param = "searchKey=" + dto.getSearchKey();
			param += "&searchValue="
					+ URLEncoder.encode(dto.getSearchValue(),"UTF-8");
		}
		
		urlList = cp + "/bbs/list.action";
		urlArticle = cp + "/bbs/article.action?pageNum="+currentPage;
		
		// param ���� �ƴϸ� �˻��� �ƴ�.
		if(!param.equals("")) {
			
			urlList += "?" + param;
			urlArticle += "&" + param;
		}
		
		// ������ �ѱ�
		// �� ���� �����Ϳ� success�� ������ ���ư�
		request.setAttribute("lists", lists);
		request.setAttribute("totalDataCount", totalDataCount);
		request.setAttribute("pageIndexList",
				myUtil.pageIndexList(currentPage, totalPage, urlList));
		
		request.setAttribute("urlArticle", urlArticle);
		return SUCCESS;
	}
	
	// Ŭ���ؼ� dto�� �ڵ����� �Ѿ�´�. 
	public String article() throws Exception {
		
		HttpServletRequest request = ServletActionContext.getRequest();
		CommonDAO dao = CommonDAOImpl.getInstance();
		
		
		// ��Ʈ����2�� �ڵ����� dto�� �Ѱ��༭ pageNum�� boardNum�� ������ �ȴ�.
		/*System.out.print(dto.getSearchKey());
		System.out.print(dto.getSearchValue());
		System.out.print(dto.getPageNum());
		System.out.print(dto.getBoardNum());*/
		
		// dao���� �ϳ��� �����͸� �����ð��̴�.
		// �Ѿ���°��� ������ �����´�.
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
		
		// ��ȸ�� ���� 
		dao.updateData("bbs.hitCountUpdate", boardNum);
		
		// �ϳ��� �����͸� �о�ͼ� dto�� �־��ش�.
		dto = (BoardDTO)dao.getReadData("bbs.readData",boardNum);
		
		// nullnullnull3  �˻��� �ߴµ��� key,value,pagenum ������..
		// ������ �־ ����� �ƴµ�
		// �����ϴ� ���ϱ� �ڵ����� �Ѿ�� dto�� �� �ؿ��� 
		// getReadData�� ���ؼ� ������ �ִ°� �������� �ʱ�ȭ��
		// �� �������� boardNum�� ���̰� �ȴ�.
		
		// �츮�� ������ �ڵ����� �Ѿ�� dto�� ������
		// �Ʒ����� ����� �ϰԵǴ°��̴�.
		/*System.out.print(dto.getSearchKey());
		System.out.print(dto.getSearchValue());
		System.out.print(dto.getPageNum());
		System.out.print(dto.getBoardNum());*/
		
		if(dto==null) {
			return "read-error";
		}
		
		// dto���� �������� content�� ������ Ű�������������� �ʱ�ȭ�Ǿ�����..
		int lineSu = dto.getContent().split("\r\n").length;
		
		dto.setContent(dto.getContent().replaceAll("\r\n", "<br/>"));
		
		// ������ �����ۺ�������
		Map<String, Object> hMap = new HashMap<>();
//		hMap.put("searchKey", dto.getSearchKey()); �̷��� �־��ָ�ȵ� ���� �ڵ����� ���� dto sk,sv,pn �� �ʱ�ȭ �Ǿ��־
											// ���� ���� �˻��� �ȵȴ�..
											
		hMap.put("searchKey", searchKey); // �׷� ����ٰ� ������ dto.getSearchKey ��
										  // ���� searchKey���� �� ����� �Ѵ�.
		hMap.put("searchValue", searchValue);
		hMap.put("groupNum", dto.getGroupNum());
		hMap.put("orderNo", dto.getOrderNo());
		
		// ������
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
		
		// �Ѿ�� �ּ�
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
		
		// ���� sql�� ������ �����۷� �Ѿ�� �ȴ�.
		return SUCCESS;	
	}
	
	public String updated() throws Exception {
		
		// �Խù� ���� ȭ�� 
		HttpServletRequest request = ServletActionContext.getRequest();
		CommonDAO dao = CommonDAOImpl.getInstance();
		
		if(dto.getMode()==null||dto.getMode().equals("")) {
			
			dto = (BoardDTO)dao.getReadData("bbs.readData",dto.getBoardNum()); // �ϳ��� ������ �о��
		
			if(dto==null) {
				return "read-error";
			}
			
			request.setAttribute("mode", "update"); 
			request.setAttribute("pageNum", dto.getPageNum());
			
			return INPUT;
		}
		// �Խù� ���� 
		
		// DTO�� �ڵ����� �Ѿ
		
		dao.updateData("bbs.updateData", dto);
		
		return SUCCESS;
		
	}
	
	// �亯
	public String reply() throws Exception {
		
		HttpServletRequest request = ServletActionContext.getRequest();
		
		CommonDAO dao = CommonDAOImpl.getInstance();
		
		if(dao==null||dto.getMode()==null||dto.getMode().equals("")) {
			
			// �θ��� ������ �б�
			dto = (BoardDTO)dao.getReadData("bbs.readData",dto.getBoardNum());
			
			if(dto==null) {
				return "read-error";
			}
			
			 // �θ��� �������� ���� �����ְ� ���� �ڰ� ���� ���������������
			String temp = "\r\n\r\n-----------------------------\r\n\r\n";
			temp += "[�亯]\r\n";
			
			//�θ����͸� �����ؼ� �� �����ͷ� created.jsp�� ���
			dto.setSubject("[�亯]" + dto.getSubject()); // �θ𲨷� �ٲ�
			dto.setContent(dto.getContent() + temp); // ���� ���� temp�� ����
			dto.setName(""); // name�� ���������ְ� �θ� �������
			dto.setEmail(""); // ���������ְ� �θ� �������
			dto.setPwd(""); //  ���������ְ� �θ� �������
			
			
			request.setAttribute("mode", "reply");
			request.setAttribute("pageNum", dto.getPageNum()); 
			
			return INPUT;
		}
		
		// �亯�Է� �ϱ����� orderNum ����
		// ordeNoUpdate �� �Ѱ���� �Ѵ�.  orderNo 1 �������������
		Map<String, Object> hMap = new HashMap<>();
		
		// ������ �θ��� dto�� �ڵ����� �Ѿ�´�.
		hMap.put("groupNum", dto.getGroupNum()); // �θ��� groupNum
		hMap.put("orderNo", dto.getOrderNo()); // �θ��� orderNo 
		
		dao.updateData("bbs.orderNoUpdate", hMap); // �θ� �ѱ��  orderNo �� +1 ���� 
		
		// �Է� 
		int maxBoardNum = dao.getIntValue("bbs.maxBoardNum");
		
		dto.setBoardNum(maxBoardNum + 1);
		dto.setIpAddr(request.getRemoteAddr());   // ���ο� ����� �ٴ� ����� ip�� �ִ´� Ŭ���̾�Ʈ ip�� �ִ´�.
		dto.setDepth(dto.getDepth() + 1); // �θ� ������ �ִ� depth �� + 1�ؾ��� �鿩����� 
		dto.setOrderNo(dto.getOrderNo() + 1); // �Էµ� �����Ϳ� �� �θ��� orderNo�� + 1���� ( ���� �ԷµǴ� �������� orderNo )		
		
		
		// ���� �����Ϳ� �Է�
		dao.insertData("bbs.insertData", dto);
		
		
		return SUCCESS;
	}
	
	
	public String deleted() throws Exception {
		
		CommonDAO dao = CommonDAOImpl.getInstance();
		
		dao.deleteData("bbs.deleteData", dto.getBoardNum());
		
		return SUCCESS;
	}
}
