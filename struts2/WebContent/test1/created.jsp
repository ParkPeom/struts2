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
	
	<!-- action에 /itwill/ 적어주면 폴더가 달라도 찾아간다  -->
<form action="<%=cp%>/itwill/created_ok.action" method="post">

<!-- dto안에 getter ,setter 를 만들었으므로 
		dto의 껍데기가 하나 씌어져서 
			dto. 으로 접근해줘야 한다.  -->
아이디 : <input type="text" name="dto.userId"/><br/>
이름 : <input type="text" name="dto.userName"/><br/>
<input type="submit" value="보내기"/><br/>

</form>	
</body>
</html>