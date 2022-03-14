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
<link rel="stylesheet" type="text/css" href="<%=cp%>/board/css/list.css"/>

<script type="text/javascript">

	function searchData() {
		
		var f = document.searchForm;
		
		f.action = "<%=cp%>/bbs/list.action";
		f.submit();
	}

</script>

</head>
<body>

<div id="bbsList">
	<div id="bbsList_title">
		게 시 판(Struts2)
	</div>
	<div id="bbsList_header">
		<div id="leftHeader">
		<form action="" method="post" name="searchForm">
			<select name="searchKey" class="selectField">
				<option value="subject">제목</option>
				<option value="name">작성자</option>
				<option value="content">내용</option>
			</select>
			<input type="text" name="searchValue" class="textField"/>
			<input type="button" value=" 검 색 " class="btn2" onclick="searchData();"/>		
		</form>				
		</div>
		<div id="rightHeader">
			<input type="button" value=" 글올리기 " class="btn2"
			 onclick="javascript:location.href='<%=cp%>/bbs/created.action';"/>			
		</div>	
	</div>
	<div id="bbsList_list">
		<div id="title">
			<dl>
				<dt class="num">번호</dt>
				<dt class="subject">제목</dt>
				<dt class="name">작성자</dt>
				<dt class="created">작성일</dt>
				<dt class="hitCount">조회수</dt>
			</dl>
		</div>
		<div id="lists">
		<c:forEach var="dto" items="${lists }">
			<dl>
				<dd class="num">${dto.listNum }</dd>
				<dd class="subject">
				
				<c:if test="${dto.depth!=0 }">
					<c:forEach var="i" begin="1" end="${dto.depth }" step="1">  <!-- 여러개 있을수있으니 -->
						&nbsp; <!-- 들여쓰기  depth 2이면 2칸뛰고 1이면 한칸뛰고 -->
					</c:forEach>
					<img src="<%=cp %>/board/image/re.gif"/>
				</c:if>
				
				<a href="${urlArticle }&boardNum=${dto.boardNum }">
				${dto.subject }</a>
				</dd>
				<dd class="name">${dto.name }</dd>
				<dd class="created">${dto.created }</dd>
				<dd class="hitCount">${dto.hitCount }</dd>
			</dl>
		</c:forEach>
		</div>
		<div id="footer">
			<p>
				<c:if test="${totalDataCount!=0 }">
					${pageIndexList }
				</c:if>
				<c:if test="${totalDataCount==0 }">
					등록된 게시물이 없습니다.
				</c:if>
		</div>
		
	</div>
	
</div>

</body>
</html>







