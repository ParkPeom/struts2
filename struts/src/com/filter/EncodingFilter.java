package com.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

// UTF-8�� �ٲٴ� �۾�

public class EncodingFilter implements Filter {
	
	private String charset;
	
	@Override
	public void destroy() {
		
	}

	
	// �����۾� 
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		
		String uri;
		// ����8
		// request �� ��δ� object�� ����
		
		
		// ServletRequest�� �ֻ��� ���� �̹Ƿ� HttpServletRequest �� �ٿ�ĳ���� 
		
		if(request instanceof HttpServletRequest) {
			
			// �ٿ�ĳ���� 
			HttpServletRequest req = (HttpServletRequest)request;
			uri = req.getRequestURI();
			// �Ѿ���� req��  �޼ҵ� ����� post����̸�
			// uri �� abc.do�� ������ ecu-kr �� ���ڵ�
			/*// ������ ��� utf-8�� 
			if(charset==null) {
				charset = "UTF-8"; // �ѹ��� ���� "UTF-8" �� �ʱ�ȭ ������ 
			}*/

			// if�� �ȿ����� req
			if(req.getMethod().equalsIgnoreCase("POST")) {
				if(uri.indexOf("abc.do")!=-1) {
					req.setCharacterEncoding("euc-kr");
				} else {			
					// 
					req.setCharacterEncoding(charset);
				}
			}
		}
		
		// ������ �ѱ��. (request�ѹ� ������ �Ҹ�ǹǷ�  �ѹ��� Ǫ���۾� )
		chain.doFilter(request, response);
	}
	
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	
		// getInit - web.xml���� �ִ°��̴�. 
		
		// charset �ʱⰪ ����
		charset = filterConfig.getInitParameter("charset");
		
		
		// ���� �޾Ƽ� �����ǵ� 
		// ����9 ����10 ����11 - �ּ�ó���� �ϸ� �ѱ��� ������	
	
		// ������ req.setCharacterEncoding(charset); UTF-8�� ���޾����� �ؿ��� UTF-8�� �ѹ� �� ���� 
		if(charset==null) {
			charset = "UTF-8"; // �ѹ��� ���� "UTF-8" �� �ʱ�ȭ ������ 
		}
	}
	
}
