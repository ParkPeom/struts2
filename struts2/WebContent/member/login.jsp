<%@ page contentType="text/html; charset=utf-8"%>
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
<link rel="stylesheet" href="<%=cp%>/member/data/style.css" type="text/css"/>
<script type="text/javascript">
	function login() {
		
		var f = document.myForm;
		if(!f.userId.value){
			alert("아이디를 입력하세요!");
			f.userId.focus();
			return;
		}
		
		if(!f.userPwd.value){
			alert("패스워드를 입력해라 좀.")
			f.userPwd.focus();
			return;
		}
		
		f.action = "<%=cp%>/mem/loin.action";
		f.submit();
		
	}

</script>

</head>
<body>

<br/><br/>

<form action="" method="post" name="myForm">
<table align="center" cellpadding="0" cellspacing="0">
	<tr height="2"><td colspan="2" bgcolor="#cccccc"></td>
	<tr height="30">
		<td colspan="2" align="center"><b>로그인</b></td>
	</tr>
	
	
	<tr height="2"><td colspan="2" bgcolor="#cccccc"></td></tr>
	
	<tr height="25">
		<td width="80" bgcolor="#e6e4e6" align="center">아이디</td>
		<td width="120" style="padding-left: 5px;">
			<input type="text" name="userId" maxlength="10" size="15" style="width: 150px; height: 22px;"/>
		</td>
	</tr>
	
	<tr height="2"><td colspan="2" bgcolor="#cccccc"></td></tr>
	
		<tr height="25">
		<td width="80" bgcolor="#e6e4e6" align="center">패스워드</td>
		<td width="120" style="padding-left: 5px;">
			<input type="password" name="userPwd" maxlength="10" size="15" style="width: 150px; height: 22px;"/>
		</td>
	</tr>
	
	<tr height="2"><td colspan="2" bgcolor="#cccccc"></td></tr>
	<tr height="30">
		<td colspan="2" align="center">
		<input type="hidden" name="mode" value="login">
		<input type="button" value="로그인" class="btn2" onclick="login();">
		<input type="button" value="회원가입" class="btn2" 
		onclick="javascript:location.href='<%=cp %>/mem/created.action';">
		<input type="button" value="게시판" class="btn2"
		onclick="javascript:location.href='<%=cp %>/img/list.action';">
		</td>
	</tr>
	
	<tr height="30">
		<td colspan="2" align="center">
		<font color="red"><b>${message }</b></font>
		</td>
	</tr>
	
	<c:choose>
		<c:when test="${!empty message }">
		<tr height="1"><td colspan="2" bgcolor="#cccccc"></td></tr>
		<tr><td align="center" colspan="2">
		<a href="javascript:location.href='<%=cp %>/mem/searchpw.action';">비밀번호 찾기</a>
		</td></tr>
		<tr height="1"><td colspan="2" bgcolor="#cccccc"></td></tr>
		</c:when>
	

	</c:choose>
</table>
</form>
</body>
</html>