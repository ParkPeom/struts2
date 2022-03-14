package com.util;

public class MyUtil {

	// currentPageSetup page numPerBlock 
	
	//��ü������
	public int getPageCount(int numPerPage,int dataCount) { 
							//       3            34 
							// numPerPage : �� �������� ��� ������
							// ��ü ������ ������ ��� �Ѹ�����
							// 34���� �ϳ��� �������� ��� �Ѹ����� 
	int pageCount = 0;
	
	// ��ü ������ ������ �ϳ��� �������� ��� �Ѹ����� ����
	pageCount = dataCount / numPerPage;  // ������ ������ �ʿ����� �˾Ƴ�
	
	if(dataCount % numPerPage != 0) { // 34 % 3 == 0���� �������������� ������ �ϳ��� �ʿ�
		pageCount++; 
		}
		return pageCount; // ��ü ������ ����
	}
	
	// ������ ó�� �޼ҵ�
	// ���� ���� ��������� �����ִ��� , ��ü������ ���� , list.jsp
	public String pageIndexList(int currentPage, int totalPage, String listUrl) { 
	
		int numPerBlock = 5; // 5�� ǥ�� ������ 6 7 8 9 10 ������
		int currentPageSetup; //  ��   ���� ������ �����ԵǸ� 5�������� ���� (���� ȭ��ǥ ��)
		int page; // for���� i 6 7 8 9 10	
		StringBuffer sb = new StringBuffer(); 
		
		if(currentPage == 0 || totalPage == 0) {
			// �����������Ǵ� ��ü������ 0�̸� null ��ȯ
			return "null";
		}
		
		// list.jsp?pageNum=2 �̰ų�    	
		// list.jsp ���鼭 ?�˻������� ������ 
		// list.jsp? searchKey=subject & searchValue=aa & pageNum=2
		//  �� String listUrl�� �Ű������� ���Ե�
		// ? �� �ִ��� ã�ƺ���. 
		if(listUrl.indexOf("?") != -1) {
			// ?�� ������
			listUrl = listUrl + "&";
		} else {
			// ����ǥ�� ������ ����ǥ�� ����
			listUrl = listUrl + "?";
		}
		// ������ 6 7 8 9 10 ������
		// ���� 8�������� 5�������Ƿ� numPerBlock = 5 �ϸ� ������ 3�� 5�� ����
		// 5 =  1 * 5 
		
		// 1 2 3 4 5 ������
		// ������ 6 7 8 9 10 ������
		
		// ������ ���� ���ϴ� ���� 
		
		// ��������  5           6           5            5 
		currentPageSetup = (currentPage/numPerBlock)*numPerBlock;
		//			10            5
			if(currentPage % numPerBlock == 0) { // 10���� �������� 0�̹Ƿ�		
		//				5				5					5
				currentPageSetup = currentPageSetup - numPerBlock; //  10�̿��� 5�� ����
				
			}
			// ���� �������� 5�� ���� ������
			
		// ������
			//		12         5       �ڿ� ���� 5 �̸�  ������ ������.   
			// 
			
			//������ �ʿ��ϸ� �ѹ��� ����� �ʿ����������� �ȸ���
			
			//		15			5				6
			if(totalPage > numPerBlock && currentPageSetup > 0) {
			
				// ���ǿ� �����ϸ� ������ ����
				//<a href="list.jsp?pageNum=2>������</a>"�� ����� �ڵ�
				sb.append("<a href=\"" + listUrl + "pageNum=" 
						+ currentPageSetup + "\">������</a>&nbsp;");
			}
			// �ٷΰ��� ������  6 7 8 9 10
			// 5��������
			// page = ����������
		//   6           5
			page = currentPageSetup + 1; // ���۰� ������ ����  6,7,8,9,10 
			
			//	   6        12       6               5(��������)      5       
			while(page <= totalPage && page <= (currentPageSetup + numPerBlock)) {
				// �����ϸ� 6���� 10���� ����
				
				// ���� �������� ���� �����ִ� �������� ������ ����� ���� 
				if(page == currentPage) {
					sb.append("<font color=\"Fuchsia\">" + page + "</font>&nbsp;");
					//<font color="Fuchsia">9</font>
				} else {
					// ����������
					// 7 ��������?
					//<a href="list.jsp?pageNum=7">7</a>&nbsp;
					sb.append("<a href=\"" + listUrl + "pageNum=" + page + 
							"\">" + page + "</a>&nbsp;");
				}		
				page++; // 6 7 8 9 10 ����
			}
		// ������
		// ������ 6 7 8 9 10 ������
		// ������ 11 12
		//(������)   12             5              5   ������ư����
		//(�ؿ���)   35            10              5   ������ư�Ȼ���
			if(totalPage - currentPageSetup > numPerBlock) {
			//<a href="list.jsp?pageNum=11">������</a>&nbsp;
			sb.append("<a href=\"" + listUrl + "pageNum=" + 
					page + "\">������</a>&nbsp;");
		}
		return sb.toString();
	}
}							 
