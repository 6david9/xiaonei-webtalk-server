﻿<%@ page pageEncoding="UTF-8" %>
<c:if test="${requestScope.isFromIM != true}">
	<c:if test="${applicationScope.domain != 'kaixin.com'}">
		<c:if test="${empty noSideAd && empty cleanpage && channel != '首页'}">
			<div id="ad100000000070"></div>
			<div id="ad100000000071"></div>
		</c:if>
	</c:if>
</c:if>
