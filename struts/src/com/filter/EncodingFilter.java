package com.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

// UTF-8로 바꾸는 작업

public class EncodingFilter implements Filter {
	
	private String charset;
	
	@Override
	public void destroy() {
		
	}

	
	// 필터작업 
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		
		String uri;
		// 사진8
		// request 는 모두다 object로 받음
		
		
		// ServletRequest가 최상의 조상 이므로 HttpServletRequest 로 다운캐스팅 
		
		if(request instanceof HttpServletRequest) {
			
			// 다운캐스팅 
			HttpServletRequest req = (HttpServletRequest)request;
			uri = req.getRequestURI();
			// 넘어오는 req에  메소드 방식이 post방식이면
			// uri 에 abc.do를 없으면 ecu-kr 로 인코딩
			/*// 나머지 모두 utf-8로 
			if(charset==null) {
				charset = "UTF-8"; // 한번더 검증 "UTF-8" 로 초기화 시켜줌 
			}*/

			// if문 안에서만 req
			if(req.getMethod().equalsIgnoreCase("POST")) {
				if(uri.indexOf("abc.do")!=-1) {
					req.setCharacterEncoding("euc-kr");
				} else {			
					// 
					req.setCharacterEncoding(charset);
				}
			}
		}
		
		// 서버에 넘긴다. (request한번 받으면 소멸되므로  한번더 푸쉬작업 )
		chain.doFilter(request, response);
	}
	
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	
		// getInit - web.xml에서 주는것이다. 
		
		// charset 초기값 지정
		charset = filterConfig.getInitParameter("charset");
		
		
		// 값을 받아서 넣을건데 
		// 사진9 사진10 사진11 - 주석처리를 하면 한글이 깨진다	
	
		// 위에서 req.setCharacterEncoding(charset); UTF-8로 못받았으면 밑에서 UTF-8로 한번 더 검증 
		if(charset==null) {
			charset = "UTF-8"; // 한번더 검증 "UTF-8" 로 초기화 시켜줌 
		}
	}
	
}
