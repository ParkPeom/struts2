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
<title>회원 가입</title>
<link rel="stylesheet" href="<%=cp %>/member/data/style.css" type="text/css" />
<link rel="stylesheet" href="<%=cp %>/member/data/list.css" type="text/css" />

<script type="text/javascript" src="<%=cp%>/member/data/util.js"></script>
<script type="text/javascript">

	function sendIt() {
		
		var f = document.myForm;
		
		str = f.userId.value;
		if(!str) {
			
			alert("아이디를 입력하세요");
			f.userId.focus();
			
			return;
		}
		f.userId.value = str;
		
		str = f.userPwd.value;
		if(!str) {
			
			alert("패스워드를 입력하세요");
			f.userPwd.focus();
			
			return;
		}
		f.userPwd.value = str;
		
		str = f.userName.value;
		if(!str) {
			
			alert("이름을 입력하세요");
			f.userName.focus();
			
			return;
		}
		f.userName.value = str;
		
		str = f.userBirth.value;
		if(!str) {
			
			alert("생년월일을 입력하세요");
			f.userBirth.focus();
			
			return;
		}
		f.userBirth.value = str;
		
		str = f.userTel.value;
		if(!str) {
			
			alert("연락처를 입력하세요");
			f.userTel.focus();
			
			return;
		}
		f.userTel.value = str;
		
		
		f.action = "<%=cp%>/mem/creatd.action";
		f.submit();
	}
</script>

</head>
<body>
<div id="bbs">
	<div id="bbs_title">
	회원가입
	</div>
	<form action="" method="post" name="myForm">
	<div id="bbsCreated">
		<div class="bbsCreated_bottomLine">
		<dl>
			<dt>아&nbsp;이&nbsp;디</dt>
			<dd>
				<input type="text" name="userId" size="35" maxlength="20" class="boxTF"
				<c:if test="${mode=='updateok' }">readonly="readonly" value="${dto.userId }"</c:if>
				/>
			</dd>
		</dl>
		</div>
		
		<div class="bbsCreated_bottomLine">
		<dl>
			<dt>패스워드</dt>
			<dd><input type="password" name="userPwd" value="${dto.userPwd }" size="35" maxlength="20" class="boxTF"/>
			</dd>
		</dl>
		</div>
		
		<div  class="bbsCreated_bottomLine">
		<dl>
			<dt>이&nbsp;&nbsp;&nbsp;&nbsp;름</dt>
			<dd><input type="text" name="userName" value="${dto.userName }" size="35" maxlength="50" class="boxTF"/></dd>
		</dl>
		</div>
		
		<div class="bbsCreated_bottomLine">
		<dl>
			<dt>생&nbsp;&nbsp;&nbsp;&nbsp;일</dt>
			<dd><input type="text" name="userBirth" value="${dto.userBirth }" size="35" maxlength="50" class="boxTF"/></dd>
		</dl>
		</div>
		
		<div class="bbsCreated_bottomLine">
		<dl>
			<dt>전&nbsp;&nbsp;&nbsp;&nbsp;화</dt>
			<dd><input type="text" name="userTel" value="${dto.userTel }" size="35" maxlength="50" class="boxTF"/></dd>
		</dl>
		</div>
		
	</div>
	
	
	<div id="bbsCreated_footer">
	
		<input type="hidden" name="mode" value="save">
		
			<input type="button" value="가입하기" class="btn2"
			onclick="sendIt();">
			<input type="reset" value="다시입력" class="btn2"
			onclick="document.myForm.userId.focus();"/>
			<input type="button" value="가입하기" class="btn2"
			onclick="javascript:location.href='<%=cp%>/mem/login.action';"/>
			
		<div style="height:30px;">
			<font color="red"><b>${message }</b></font>
		</div>
	</div>
	
	</form>
</div>

</body>
</html>