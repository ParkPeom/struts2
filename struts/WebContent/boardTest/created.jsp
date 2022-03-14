<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
	request.setCharacterEncoding("UTF-8");
	String cp = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>게 시 판(Struts & iBatis)</title>

<link rel="stylesheet" type="text/css" href="<%=cp%>/boardTest/css/style.css"/>
<link rel="stylesheet" type="text/css" href="<%=cp%>/boardTest/css/created.css"/>

<script type="text/javascript" src="<%=cp%>/board/js/util.js"></script>
<script type="text/javascript">

	function sendIt(){
		
		var f = document.myForm;
		
		str = f.subject.value;
		str = str.trim();
		if(!str){
			alert("\n제목을 입력하세요.");
			f.subject.focus();
			return;
		}
		f.subject.value = str;
		
		str = f.name.value;
		str = str.trim();
		if(!str){
			alert("\n이름을 입력하세요.");
			f.name.focus();
			return;
		}			
		/* 
		if(!isValidKorean(str)){
			alert("\n이름을 정확히 입력하세요.");
			f.name.focus()
			return;
		} 
		*/		
		f.name.value = str;
		
		if(f.email.value){
			if(!isValidEmail(f.email.value)){
				alert("\n정상적인 E-Mail을 입력하세요.");
				f.email.focus();
				return;
			}
		}
		
		str = f.content.value;
		str = str.trim();
		if(!str){
			alert("\n내용을 입력하세요.");
			f.content.focus();
			return;
		}
		f.content.value = str;
		
		str = f.pwd.value;
		str = str.trim();
		if(!str){
			alert("\n패스워드를 입력하세요.");
			f.pwd.focus();
			return;
		}
		f.pwd.value = str;
		
		f.action = "<%=cp%>/boardTest.do";
		f.submit();	
	}
</script>
</head>
<body>
<div id="bbs">
	<div id="bbs_title">
		게 시 판(Struts & iBatis)
		
		<!--  dto 와 mode가 넘어오면 value에는 mode에는 update_ok 가 들어옴  -->
		
	</div>
	<form action="" method="post" name="myForm">
	<div id="bbsCreated">
		<div class="bbsCreated_bottomLine">
			<dl>
				<dt>제&nbsp;&nbsp;&nbsp;&nbsp;목</dt>
				<dd>
				<input type="text" name="subject" value="${dto.subject }" size="60" 
				maxlength="100" class="boxTF"/>
				</dd>
			</dl>		
		</div>
		
		<div class="bbsCreated_bottomLine">
			<dl>
				<dt>작 성 자</dt>
				<dd>
				<input type="text" name="name" value="${dto.name }" size="35" 
				maxlength="20" class="boxTF"/>
				</dd>
			</dl>		
		</div>
		
		<div class="bbsCreated_bottomLine">
			<dl>
				<dt>E-Mail</dt>
				<dd>
				<input type="text" name="email" value="${dto.email }" size="35" 
				maxlength="50" class="boxTF"/>
				</dd>
			</dl>		
		</div>
		
		<div id="bbsCreated_content">
			<dl>
				<dt>내&nbsp;&nbsp;&nbsp;&nbsp;용</dt>
				<dd>
				<textarea rows="12" cols="63" name="content"
				class="boxTA">${dto.content }</textarea>
				</dd>
			</dl>
		</div>

		<div class="bbsCreated_noLine">
			<dl>
				<dt>패스워드</dt>
				<dd>
				<input type="password" name="pwd" value=${dto.pwd } size="35" 
				maxlength="7" class="boxTF"/>
				&nbsp;(게시물 수정 및 삭제시 필요!!)
				</dd>
			</dl>		
		</div>	
	</div>
	<div id="bbsCreated_footer">
	
		<input type="hidden" name="method" value="created_ok"/>
	<!--  mode의 단어에는 insert 가 오거나 update 가 오거나  -->
		<input type="hidden" name="mode" value="${mode }"/>
		
		
		
		<c:if test="${mode=='insert' }">		
			<input type="button" value=" 등록하기 " class="btn2" onclick="sendIt();"/>
			<input type="reset" value=" 다시입력 " class="btn2" 
			onclick="document.myForm.subject.focus();"/>
			<input type="button" value=" 작성취소 " class="btn2" 
			onclick="javascript:location.href='<%=cp%>/boardTest.do?method=list';"/>
	</c:if>
	
	
	<!-- 수정을 하기 위한곳 -->
	<!-- 수정인지 업데이트인지 구분하는 곳  -->	
		<c:if test="${mode=='updateOK' }">		
			<input type="hidden" name="num" value="${dto.num }"/>
			<input type="hidden" name="pageNum" value="${pageNum }"/>
			
			<input type="button" value=" 수정하기 " class="btn2" onclick="sendIt();"/>
			<input type="button" value=" 수정취소 " class="btn2" 
			onclick="javascript:location.href='<%=cp%>/boardTest.do?method=list';"/>
		</c:if>
	</div>
	</form>
</div>
</body>
</html>





























