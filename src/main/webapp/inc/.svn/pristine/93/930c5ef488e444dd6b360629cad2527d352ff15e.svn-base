<%@ page pageEncoding="utf-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="newsFeedUrl" value="http://www.renren.com/home?from=homeleft" /> 
<c:if test="${!visiter.userStateInfo.guideDone}">
	<c:set var="newsFeedUrl" value="http://guide.renren.com/guide?from=homeleft" /> 
</c:if>
<c:set var="newFeedTitle" value="${visiter.userStateInfo.guideDone || requestScope.newStageTen}" />
<div class="site-menu-nav-box box">
	<article id="site-menu-nav" class="site-menu-apps">
		<h3>常用</h3>
		<section class="site-menu-apps-base">
			<div class="apps-item">
				<div class="item-title">
					<span class="item-main">
						<a href="${home_url}" ui-async="async"><img class="icon" src="${applicationScope.urlStatic}/imgpro/v6/icon/feed.png" /> 新鲜事</a>
					</span>
				</div>
			</div>
			<div class="apps-item">
				<div class="item-title">
					<span class="item-main">
						<a href="http://matter.renren.com/${visiter.id}?page=0&from=homeleft" ui-async="async"><img class="icon" src="${applicationScope.urlStatic}/imgpro/v6/icon/matter-logo.gif" /> 与我相关<span class="apps-item-count" id="rm_feed_count"></span></a>
					</span>
				</div>
			</div>
		</section>
		<section class="site-menu-apps-favor">
			<c:forEach var="item" items="${favList}" begin="0" end="19">
				<c:set var="itemUrl" value="${item.sideNavUrl}"/>
				<c:if test="${item.appId==166613}">
					<c:set var="itemUrl" value="http://photo.renren.com/photo/${visiter.id}/album/friends"/>
				</c:if>
				<c:if test="${item.appId==45}">
					<c:set var="itemUrl" value="http://status.renren.com/status/?fromType=nomore"/>
				</c:if>
				<div class="apps-item" id="appsItem_${item.appId}">
					<div class="item-title">
						<span class="item-main">
							<a href="${itemUrl}"<c:if test="${item.appId==45||item.appId==60||item.appId==95003||item.appId==120019||item.appId==141879||item.appId==115147||item.appId==166607||item.appId==166613||item.appId==166614||item.appId==166615||item.appId==166616||item.appId==166617}"> ui-async="async"</c:if>><img src="${item.appIcon}" class="icon"> ${item.appName}</a>
						</span>
					</div>
					<div class="item-editor">
						<span class="editor-button"><a href="#" onclick="this.blur();return false;" class="icon">编辑</a></span>
						<span class="editor-operations">
							<a href="#nogo" data-appid="${item.appId}" data-optype="remove" onclick="this.blur();return false;" class="an-operation">移出</a>
							<a href="#nogo" data-optype="order" onclick="this.blur();return false;" class="an-operation">排序</a>
						</span>
					</div>
				</div>
			</c:forEach>
		</section>
		<section id="saveAppsOrder" class="site-menu-apps-favor-save-order" style="display:none;">
			<a href="#nogo" onclick="return false;">完成排序</a>
		</section>
	</article>
	
	<article class="site-menu-apps site-menu-minigroups">
		<h3><a href="http://qun.renren.com/qun/index" ui-async="async" class="more">更多</a>我的群</h3>
		<section>
			<c:forEach var="item" items="${requestScope.miniGrouplist}" begin="0" end="2">
				<div class="apps-item">
					<div class="item-title">
						<span class="item-main">
							<a href="http://qun.renren.com/qun/ugc/home/${item.id}?from=homeleft" class="with-count" id="mg_${item.id}" ui-async="async">
								<img class="icon" src="http://a.xnimg.cn/imgpro/v6/icon/minigroup/${item.icon}.png" />
								${item.name}
								<c:if test="${item.newFeedsNum>0}">
									<span class="apps-item-count">${item.newFeedsNum}</span>
								</c:if>
							</a>
						</span>
					</div>
				</div>
			</c:forEach>
		</section>
		<c:if test="${empty miniGrouplist||fn:length(miniGrouplist)<3}">
			<section class="minigroup-create">
				<script type="text/javascript">
					XN.getFileVersion([
						'http://s.xnimg.cn/n/apps/minigroup/core/js/manage.js?ver=$revxxx$'
					]);
				</script>
				<a href="#" onclick="if(!XN.app.miniGroup){XN.loadFile('http://s.xnimg.cn/n/apps/minigroup/core/js/manage.js',function(){XN.app.miniGroup.createGroup();});}else{XN.app.miniGroup.createGroup();}return false;">创建一个新群</a>
			</section>
		</c:if>
				
	</article>
	
	<article class="site-menu-apps">
		<h3><a href="http://app.renren.com/app/editApps" ui-async="async" class="more">更多</a>我的应用</h3>
        <section class="site-menu-apps-others">
            <div class="apps-item">
                <div class="item-title">
                    <span class="item-main">
						<a href="http://app.${applicationScope.sysdomain}/?origin=101" target="_blank" class="with-count">
							<img width="16" height="16" src="${applicationScope.urlStatic}/imgpro/v6/icon/app.png" class="icon">
							应用中心
							<c:if test="${!empty recent_online_app_count && recent_online_app_count > 0}">
								<c:set var="newAppCount">
									<c:choose>
										<c:when test="${recent_online_app_count > 99}">99+</c:when>
										<c:otherwise>${recent_online_app_count}</c:otherwise>
									</c:choose>
								</c:set>
								<span class="apps-item-count" title="本周新上线${newAppCount}款应用">${newAppCount}</span>
							</c:if>
						</a>
                    </span>
                </div>
            </div>
			<div class="apps-item">
				<div class="item-title">
					<span class="item-main">
						<a href="http://game.renren.com/" target="_blank">
							<img width="16" height="16" src="${applicationScope.urlStatic}/imgpro/v6/icon/game.png" class="icon" /> 
							网页游戏
						</a>
					</span>
				</div>
			</div>
			
			<c:set var="requestCount" value="0" />
			<c:forEach items="${requestScope.userCountMap}" var="entry"> 
				<c:set var="key" value="${entry.key}"></c:set>
				<c:if test="${key==0 || key==40}"><%--0代表邀请，40代表礼物--%>
					<c:set var="requestCount" value="${requestCount + entry.value}" />
				</c:if>
			</c:forEach>
			<c:if test="${requestCount>0}">
				<div id="appsItem_26" class="apps-item">
					<div class="item-title">
						<span class="item-main">
							<a href="http://app.${applicationScope.sysdomain}/app/appRequestList?origin=700045" onclick="if($('allAppsInvite')){$('allAppsInvite').hide();}" ui-async="async" class="with-count">
								<img width="16" height="16" src="http://app.xnimg.cn/application/20101018/16/35/L1MGd11n018153.gif" class="icon">
								应用请求
								<c:if test="${requestCount>99}">
									<c:set var="requestCount" value="99+" />
								</c:if>
								<span id="allAppsInvite" class="apps-item-count" title="${requestCount}条应用请求">${requestCount}</span>
							</a>
						</span>
					</div>
				</div>
			</c:if>
			
			<c:if test="${timeStamp <= 20111229}">
				<div class="apps-item">
					<div class="item-title">
						<span class="item-main">
							<a href="http://app.renren.com/activity/christmas?origin=103" target="_blank">
								<img width="16" height="16" class="icon" src="http://a.xnimg.cn/imgpro/icons/appmc.gif" /> 神秘圣诞节
							</a>
						</span>
					</div>
				</div>
			</c:if>
			
			<c:set var="begin" value="0" />
			<c:set var="end" value="14" />
			<c:set var="origin">
				<c:choose>
					<c:when test="${visiter.userStateInfo.guideDone}">103</c:when>
					<c:otherwise>103&guide</c:otherwise>
				</c:choose>
			</c:set>
            <%@ include file="site-menu-apps-item2.jsp"%>
        </section>
		<section>
			<div class="add-new-apps">
				<a href="http://app.renren.com/" class="add-new-apps-link" target="_blank">添加新应用</a>
			</div>
		</section>
		
	</article>
	
	<c:if test="${ !(empty homeLeftSites||fn:length(homeLeftSites)==0) }">
		<c:set var="hasZhan" value="true" />
	</c:if>

	<c:if test="${ familyName != null && familyUrl != null}">
		<c:set var="hasFamilySpace" value="true" />
	</c:if>

	<c:if test="${ !(empty managePageList||fn:length(managePageList)==0) }">
		<c:set var="hasPage" value="true" />
	</c:if>

	<c:if test="${ !(empty manageLoverPageList||fn:length(manageLoverPageList)==0) }">
		<c:set var="hasLoverSpace" value="true" />
	</c:if>
	
	<c:if test="${hasZhan||hasFamilySpace||hasPage||hasLoverSpace}">
		<article class="site-menu-apps">
			<h3>我管理的</h3>
			<section class="site-menu-apps-admins">
				<c:if test="${hasPage}">
					<c:forEach var="item" items="${managePageList}" begin="0" end="2">
					<div class="apps-item">
						<div class="item-title">
							<span class="item-main">
								<a title="${item.name}" href="http://page.renren.com/${item.id}" target="_blank"><img class="icon" src="${applicationScope.urlStatic}/imgpro/icons/follow.gif" /> ${fn:substring(item.name, 0, 7)}</a>
							</span>
						</div>
					</div>
					</c:forEach>
				</c:if>
				
				<c:if test="${hasZhan}">
					<c:forEach var="item" items="${homeLeftSites}" begin="0" end="2">
					<div class="apps-item">
						<div class="item-title">
							<span class="item-main">
								<a title="${item.name}" href="http://zhan.renren.com/zhan/${item.id}" target="_blank"><img class="icon" src="${applicationScope.urlStatic}/imgpro/icons/zhan.png" /> ${fn:substring(item.name, 0, 7)}</a>
							</span>
						</div>
					</div>
					</c:forEach>
				</c:if>
				
				<c:if test="${hasFamilySpace}">
					<div class="apps-item">
						<div class="item-title">
							<span class="item-main">
								<a title="${familyName}" href="http://jia.renren.com/" target="_blank"><img class="icon" src="${applicationScope.urlStatic}/imgpro/app/home.gif" /> ${fn:substring(familyName, 0, 7)}</a>
							</span>
						</div>
					</div>
				</c:if>
				
				<c:if test="${hasLoverSpace}">
					<c:forEach var="item" items="${manageLoverPageList}" begin="0" end="2">
					<div class="apps-item">
						<div class="item-title">
							<span class="item-main">
								<a title="${item.name}" href="${applicationScope.urlLover}/${item.id}" target="_blank"><img class="icon" src="${applicationScope.urlStatic}/imgpro/page/love/lover-heart.png" /> ${fn:substring(item.name, 0, 7)}</a>
							</span>
						</div>
					</div>
					</c:forEach>
				</c:if>
			</section>
		</article>
	</c:if>

	
</div>

