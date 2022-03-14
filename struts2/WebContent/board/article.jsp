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
<title>게 시 판</title>

<link rel="stylesheet" type="text/css" href="<%=cp%>/board/css/style.css"/>
<link rel="stylesheet" type="text/css" href="<%=cp%>/board/css/article.css"/>


<script type="text/javascript">

/* article의 dto가 자동으로 넘어온다. 넘어오는 dto를 받는다  */
	function sendData(value) {
		
	/* 넘어오는 dto를 스크립트에서 변수로 받을수있다. */
		var boardNum = "${dto.boardNum}";
		var pageNum = "${pageNum}";
		
		var url = "<%=cp%>/bbs/";
		
		if(value=="delete") {
			url += "deleted.action";
		} else if(value=="update") {
			url += "updated.action";
		} else if(value=="reply") {
			url += "reply.action"
		}
		/* params는 serachkey,searchvalue,pageNum이 넘어온다. */
		url += "?boardNum=" + boardNum;
		url += "&${params}"; 
		
		// 이동
		location.replace(url);
	}
</script>

</head>
<body>

<div id="bbs">
	
	<div id="bbs_title">
		게 시 판 (Struts2)
	</div>
	<div id="bbsArticle">
		
		<div id="bbsArticle_header">
			${dto.subject }
		</div>
		
		<div class="bbsArticle_bottomLine">
			<dl>
				<dt>작성자</dt>
				<dd>${dto.name }</dd>
				<dt>줄수</dt>
				<dd>${lineSu }</dd>
			</dl>		
		</div>
		
		<div class="bbsArticle_bottomLine">
			<dl>
				<dt>등록일</dt>
				<dd>${dto.created }</dd>
				<dt>조회수</dt>
				<dd>${dto.hitCount }</dd>
			</dl>		
		</div>
		
		<div id="bbsArticle_content">
			<table width="600" border="0">
			<tr>
				<td style="padding-left: 20px 80px 20px 62px;" 
				valign="top" height="200">
				${dto.content }
				</td>
			</tr>			
			</table>
		</div>
		
		<div class="bbsArticle_bottomLine">
			이전글:
			<!-- 제목이 있을때만 보여준다.  -->
		<c:if test="${!empty preSubject }">
		<a href="<%=cp %>/bbs/article.action?${params }&boardNum=${preBoardNum }">
		${preSubject }</a>
		</c:if>	
		</div>
		
		<div class="bbsArticle_noLine">
			다음글:
			<!-- 제목이 있을때만 보여준다.  -->
		<c:if test="${!empty nextSubject }">
		<a href="<%=cp%>/bbs/article.action?${params}&boardNum=${nextBoardNum}">
		${nextSubject }</a>
		</c:if>		
		</div>
	</div>
	<div class="bbsArticle_noLine" style="text-align: right">
	From : ${dto.ipAddr }
	</div>
	
	<div id="bbsArticle_footer">
		<div id="leftFooter">
			<input type="button" value=" 답변 " class="btn2" onclick="sendData('reply')"/>
			<input type="button" value=" 수정 " class="btn2" onclick="sendData('update')"/>
			<input type="button" value=" 삭제 " class="btn2" onclick="sendData('delete')"/>
		</div>
		<div id="rightFooter">
			<input type="button" value=" 리스트 " class="btn2"
			 onclick="javascript:location.href='<%=cp%>/bbs/list.action?${params }'"/>
		</div>	
	</div>
	
</div>
</body>
</html>






