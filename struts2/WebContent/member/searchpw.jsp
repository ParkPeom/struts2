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
<title>비밀번호 찾기 페이지</title>

<link rel="stylesheet" href="<%=cp %>/member/data/style.css" type="text/css"/>
	
</head>
<body>
	
<script type="text/javascript">

	
	function sendIt() {
		
		var f = document.myForm;
		
		if(!f.userId.value) {
			
			alert("아이디를 입력하세요");
			f.userId.focus();
			return;
		
		}
		
		if(!f.userTel.value) {
			
			alert("연락처를 입력하세요");
			f.userTel.focus();
			return;
		}
		f.action = "<%=cp%>/mem/searchpw.action";
		f.submit();
	}

</script>		
</body>

<body>
<br/><br/>

<form action="" method="post" name="myForm">
<table align="center" cellpadding="0" cellspacing="0">

<tr height="2"><td colspan="2" bgcolor="#cccccc"></tr>
<tr height="30">

	<td colspan="2" align="center"><b>비밀번호 검색</b></td>
</tr>

<tr height="2"><td colspan="2" bgcolor="#cccccc"></tr>
<tr height="25">

	<td width="80" bgcolor="#e6e4e6" align="center">아이디</td>
	<td width="120" style="padding-left: 5px;">
		<input type="text" name="userId" maxlength="10" size="15"
		style="width: 150px; height: 22px"/>
	</td>
</tr>		
	
	
<tr height="2"><td colspan="2" bgcolor="#cccccc"></tr>
<tr height="25">

	<td width="80" bgcolor="#e6e4e6" align="center">전화번호</td>
	<td width="120" style="padding-left: 5px;">
		<input type="text" name="userTel" maxlength="13" size="25"
		style="width: 150px; height: 22px"/>
	</td>
</tr>	

<tr height="2"><td colspan="2" bgcolor="#cccccc"></tr>
<tr height="30">

	
	<td colspan="2" align="center">
	
	<input type="hidden" value="save" name="mode">
	<input type="button" value="확인" class="btn2" onclick="sendIt();">
	<input type="button" value="취소" class="btn2"
	onclick="javascript:location.href='<%=cp %>/mem/login.action';">
	
	<input type="button" value="회원가입" class="btn2"
	onclick="javascript:location.href='<%=cp %>/mem/created.action';">
	</td>
</tr>	

<tr height="30">
	<td colspan="2" align="center">
	<font color="red"><b>${message }</b></font>
	</td>
</tr>	
<c:choose>
	<c:when test="${!empty message }">
	
	<tr height="1"><td colspan="2" bgcolor="#cccccc"></tr>
	<tr><td align="center" colspan="2">
	<a href="<%=cp %>/mem/searchpw.action">비밀번호 찾기</a>
	</td></tr>
	<tr height="1"><td colspan="2" bgcolor="#cccccc"></tr>
	</c:when>
</c:choose>
</table>
</form>
</body>
</html>