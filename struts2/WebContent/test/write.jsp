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

<!-- 입력창  -->

<form action="<%=cp%>/itwill/write_ok.action" method="post">
	
아이디 : <input type="text" name="userId"/><br/> 
이름 : <input type="text" name="userName"/><br/>
<input type="submit" value="보내기"/><br/>



</form>


</body>
</html>