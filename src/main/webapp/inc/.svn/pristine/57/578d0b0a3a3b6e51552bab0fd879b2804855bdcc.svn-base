<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>	

<div id="header">
	<div class="site-header clearfix">
		<div class="user-info clearfix">
			<a href="http://photo.renren.com/getalbumprofile.do?owner=${visiter.id}" class="avatar">
				<img src="${userBasic.tiny_Url}" width="50" height="50" />
			</a>
			<div class="info">
				<div class="user-name">
					<a href="${applicationScope.urlWww}/profile.do?id=${visiter.id}" class="name" title="${visiter.name}">${fn:substring(visiter.name, 0, 3)}</a>
					<span class="icons">
						<c:if test="${requestScope.isVip==1}">
							<a style="${requestScope.homeVipIcon}" class="vip-level" href="${applicationScope.urlI}/icon" title="点击更换VIP图标">${VipLevel}</a>
						</c:if>
						<c:if test="${requestScope.isVip==0}">
							<a class="expired vip-level" href="${applicationScope.urlI}/icon" title="点击更换VIP图标">${VipLevel}</a>
						</c:if>
						<c:if test="${requestScope.isVip!=0 && requestScope.isVip!=1 && visiter.id%10 == 9}">
							<a class="notvip vip-level" href="${applicationScope.urlI}/icon" title="点击设置图标"></a>
						</c:if>
					</span>
				</div>
				<div class="live-days">
					<span title="连续登录${loginCDays}天">${loginCDays}天</span>
				</div>
			</div>
		</div>
		<div id="global-publisher" class="global-publisher-box">
			<jsp:include page="/inc/n/modules/global-publisher.jsp" />
		</div>
		<div class="ugc-operations clearfix">
			<a class="uo-upload-photo flashUploader" href="#nogo" onclick="return false;">传照片</a>
			<a class="uo-new-blog" href="http://blog.renren.com/NewEntry.do">写日志</a>
		</div>
	</div>
</div>