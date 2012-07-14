<%@page pageEncoding="UTF-8" %>

	<div id="publisher-box" style="position:relative;z-index:3;">
	    <div id="publisher" class="publisher-module">
			<%-- 各种小舌头 --%>
			<c:if test="${timeStamp >= 20111208 && timeStamp <= 20111218}">
				<a id="for6years" href="http://6.renren.com/" style="<c:if test="${timeStamp == 20111208}">display:none;</c:if>position:absolute;width:142px;height:20px;top:0px;right:10px;background:url(http://a.xnimg.cn/imgpro/home/publisher-sixys.png) 0 0 no-repeat;outline:0 none;"></a>
				<c:if test="${timeStamp == 20111208}">
					<script type="text/javascript">
						XN.dom.ready(function(){
							var now = new Date();
							if (now.getHours() >= 7) {
								$('for6years').show();
							}
						});
					</script>
				</c:if>
			</c:if>
			
			<div id="publisher-selector-box" class="publisher-selector-box">
	            <header>
	                <dl class="publisher-selector">
	                    <dt>发布：</dt>
	                    <dd id="publisher-status-tab"><a class="publisher-status-trigger" href="#"><img src="http://xnimg.cn/n/res/icons/status.png" /> 状态</a></dd>
	                    <dd id="publisher-photo-tab">
							<form class="publisher-photo-trigger" target="publisher_upload" enctype="multipart/form-data" action="http://upload.renren.com/uploadservice.fcgi?pagetype=publisherSingleUploadNew" method="post">
	                            <input type="file" name="file" /> <input type="hidden" name="hostid" value="${visiter.id}" /><iframe name="publisher_upload" src="http://www.renren.com/ajaxproxy.htm" style="display: none;" id="publisher_upload"></iframe><a onclick="return false;" href="#"><img src="http://xnimg.cn/n/res/icons/photo.png" /> 照片</a>
	                        </form>
	                    </dd>
	                    <dd id="publisher-share-tab"><a class="publisher-share-trigger" href="#"><img src="http://xnimg.cn/n/res/icons/share.png" /> 分享</a></dd>                    
	                </dl>
	            </header>
	        </div>
	
	        <div id="publisher-share-box" style="display:none">
	            <article id="publisher-share" class="publisher-share">
	                <section>
	                    <div class="input-link-box" style="display:none">
	                    </div>
	                    <div class="input-comment-box" style="display:none">
	                    </div>
	                </section>
	            </article>
	        </div>
	
	        <div id="publisher-photo-box" class="publisher-photo-box" style="display:none">
	            <article id="publisher-photo" class="publisher-photo">
	                <section>
	                    <div class="uploading-module" style="display:none">
	                        <p><img src="http://xnimg.cn/n/res/icons/indicator.gif" width="16" height="16" />正在上传，请稍候...</p>
	                    </div>
	                    <div class="success-module" style="display:none">
							<p><img src="http://xnimg.cn/n/res/icons/right.png" /> 发布成功！</p>
	                    </div>
	                    <div class="photo-info-box" style="display:none">
	                    </div>
	                </section>
	            </article>
	        </div>
	
	        <div id="publisher-status-box" class="publisher-status-box" data-publishtogroupsapi="http://shell.renren.com/${visiter.id}/channel">
				<article id="publisher-status" class="publisher-status">
	                <section>
	                    <div class="editor-box">
							<form class="status-publisher" action="http://shell.renren.com/${visiter.id}/status" method="post">
	                            <div class="status-inputer">
									<div class="status-textarea">
										<textarea name="content" class="status-content" required="required" defaultpos="${guideTextBlackPosition}" defaultmodule="${guideTextWithOffset}" placeholder="${onlineText}"></textarea>
									</div>
	                                <input name="isAtHome" type="hidden" value="1" />
									<div class="chars-info" style="display:none"><span class="chars-remain">140</span></div>
									<div class="status-actions">
										<a class="publish-to-button" style="display:none" href="#publish-to">全站</a>
										<input class="submit" style="display:none" type="submit" value="发布" />
									</div>
									<div class="status-toolbar" style="display:none">
										<a class="emotion-button" href="#emotion">表情</a>
										<a class="at-button" href="#at">点名</a>
									</div>
	                            </div>
	                            <div class="last-status">
	                                <c:choose> <c:when test="${empty requestScope.ugcstatus && !visiter.userStateInfo.guideDone}">                  
	                                    </c:when>
	                                    <c:otherwise>
	                                        <a ui-async="async" title="${requestScope.fullstatus}" href="${applicationScope.urlStatus}/status?id=${requestScope.ubid}">
	                                            ${updateTitle}:${requestScope.ugcstatus}
	                                        </a>
	                                    </c:otherwise>
	                                </c:choose>     
	                            </div>
								<div class="publish-to-panel-box" style="display:none">
								</div>
	                        </form>
	                    </div>
	                </section>
	            </article>
	        </div>
	
	    </div>
			
	</div>
	
