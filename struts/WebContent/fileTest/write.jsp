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
	
	
<form action="<%=cp%>/fileTest.do" method="post"
enctype="multipart/form-data">

제목 : <input type="text" name="subject"/><br/>
파일 : <input type="file" name="upload"/><br/>

<input type="hidden" name="method" value="write_ok"/>

<input type="submit" value="파일 업로드"/><br/>
<input type="button" value="리스트"
onclick="javascript:location.href='<%=cp%>/fileTest.do?method=list';"/>

</form>	
</body>
</html>