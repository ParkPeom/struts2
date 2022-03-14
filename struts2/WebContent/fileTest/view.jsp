<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<%
	request.setCharacterEncoding("UTF-8");
	String cp = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>

<!-- 클릭했을때 download() 로 처리하게끔 null 로 와서 아무작업도 안하게 함 
 saveFilename 과 originalfilename을 가지고 가게 된다.-->
파일 다운로드<br/>
<a href="<%=cp%>/fileTest/download.action?saveFileName=${saveFileName}
&originalFileName=${originalFileName}">${originalFileName}</a><br/>
	
<br/>

파일보기<br/>
<a href="<%=cp%>/fileTest/down.action?saveFileName=${saveFileName}
&originalFileName=${originalFileName}">${originalFileName}</a><br/>





</body>
</html>