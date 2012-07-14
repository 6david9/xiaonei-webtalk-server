<%@ page contentType="text/html;charset=UTF-8" session="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<script type="text/javascript" src="http://s.xnimg.cn/a21984/jspro/editor/tiny_mce.js"></script>
<script type="text/javascript" src="http://s.xnimg.cn/a24006/jspro/xn.form.editor.js"></script>

<div class="global-publisher-module" data-publishtogroupsapi="http://shell.renren.com/${visiter.id}/channel">
	<form class="status-global-publisher" action="http://shell.renren.com/${visiter.id}/status" method="post">
		<div id="global-publisher-status-box" class="global-publisher-status-box">
			<article id="global-publisher-status">
			<section>
			<div class="editor-box">

				<div class="status-editor">
					<div class="status-inputer">
						<div class="status-textarea">
							<textarea name="content" class="status-content" defaultpos="${guideTextBlackPosition}" defaultmodule="${guideTextWithOffset}" placeholder="${onlineText}"></textarea>
						</div>
						<dl class="global-publisher-selector">
							<dd class="global-publisher-selector-photo"><a class="global-publisher-photo-trigger" href="javascript:;" title="照片"><img src="http://xnimg.cn/modules/global-publisher/res/photo2.png" data-openedsrc="http://xnimg.cn/modules/global-publisher/res/photo.png" alt="照片" /></a></dd>
							<dd class="global-publisher-selector-video"><a class="global-publisher-video-trigger" href="javascript:;" title="视频"><img src="http://xnimg.cn/modules/global-publisher/res/video2.png" data-openedsrc="http://xnimg.cn/modules/global-publisher/res/video.png" alt="视频" /></a></dd>
							<dd class="global-publisher-selector-share"><a class="global-publisher-share-trigger" href="javascript:;" title="分享"><img src="http://xnimg.cn/modules/global-publisher/res/link2.png" data-openedsrc="http://xnimg.cn/modules/global-publisher/res/link.png" alt="分享" /></a></dd>
							<%--<dd class="global-publisher-selector-blog"><a class="global-publisher-blog-trigger" href="javascript:;"><img src="http://xnimg.cn/modules/global-publisher/res/blog.png" alt="日志" /></a></dd>--%>
						</dl>
						<div class="status-toolbar" style="display:none">
							<a class="at-button" href="#at" title="点名"><img src="http://xnimg.cn/modules/global-publisher/res/at-button.png" /></a>
							<a class="emotion-button" href="#emotion" title="表情"><img src="http://xnimg.cn/modules/global-publisher/res/emotion-button.png" /></a>
							<input class="speech-button" style="display:none;" type="text" x-webkit-speech="speech" lang="zh_CN" title="语音输入" />
							<div class="chars-info" style="display:none"><span class="chars-remain">240</span></div>
						</div>
					</div>
					<div class="global-publisher-modules-box">
						<menu>
							<li><a class="close" style="display:none;" href="#" onclick="return false;">关闭</a></li>
						</menu>

						<%--2011/12/12隐藏发日志功能--%>
						<%--<div id="global-publisher-blog-box" class="global-publisher-module-box" style="display:none">--%>
							<%--<article id="global-publisher-blog" class="global-publisher-blog">--%>

							<%--<section>--%>
							<%--<div class="blog-editor-box"></div>--%>
							<%--</section>--%>
							<%--</article>--%>
						<%--</div>--%>

						<div id="global-publisher-video-box" class="global-publisher-module-box" style="display:none">
							<article id="global-publisher-video" class="global-publisher-share">
							<section>

							<div class="input-link-box" style="display:none">
							</div>
							<div class="input-comment-box" style="display:none">
							</div>
							</section>
							</article>
						</div>

						<div id="global-publisher-share-box" class="global-publisher-module-box" style="display:none">

							<article id="global-publisher-share" class="global-publisher-share">
							<section>
							<div class="input-link-box" style="display:none">
							</div>
							<div class="input-comment-box" style="display:none">
							</div>
							</section>
							</article>
						</div>

						<div id="global-publisher-photo-box" class="global-publisher-module-box" data-action="http://shell.renren.com/${visiter.id}/photo" style="display:none">
							<article id="global-publisher-photo" class="global-publisher-photo">
							<section>
							<div class="upload-selector" style="display:none">
								<section class="multiple-uploader">
								<a href="#" onclick="return false;" class="flashUploader">
									<h4 class="flashUploader">多张上传</h4>
									<p class="flashUploader">上传多张照片</p>
								</a>
								</section>
								<section class="single-uploader">
								<div class="global-publisher-photo-trigger">
									<input type="file" name="file" />
									<input style="display:none;" type="submit" name="upload" formtarget="global-publisher_upload" formenctype="multipart/form-data" formaction="http://upload.renren.com/uploadservice.fcgi?pagetype=publisherSingleUploadNew" formmethod="post" />
									<input type="hidden" name="hostid" value="${visiter.id}" />
									<iframe style="display:none;" name="global-publisher_upload" src="http://www.renren.com/ajaxproxy.htm" id="global-publisher_upload"></iframe>

								</div>
								<a href="javascript:return false;">
									<h4>快速上传</h4>
									<p>上传一张照片</p>
								</a>
								</section>
							</div>
							<div class="uploading-module" style="display:none">

								<p><img src="http://xnimg.cn/n/res/icons/indicator.gif" width="16" height="16" />正在上传，请稍候...</p>
							</div>
							<div class="photo-info-box" style="display:none">
							</div>
							</section>
							</article>
						</div>
					</div>
					<div class="global-publisher-footer" style="display:none">
						<%--11/12/7 隐藏发布到功能，联系zheng.lai--%>
						<%--<div class="publish-to-box">--%>
							<%--<h3>发布至：<span class="publish-to-info">全部好友</span> <a class="publish-to-button" href="#publish-to">编辑</a></h3>--%>
							<%--<div class="publish-to-panel-box" style="display:none">--%>
							<%--</div>--%>
						<%--</div>--%>
						<div class="global-publisher-actions">
							<input class="submit" type="submit" value="发布" />
							<input class="altsubmit" style="display:none;" type="submit" value="保存为草稿" />
						</div>
						<div class="last-status" style="display:none;">
							<c:choose> <c:when test="${empty requestScope.ugcstatus && !visiter.userStateInfo.guideDone}">                  
								</c:when>
								<c:otherwise>
									<a ui-async="async" title="${requestScope.fullstatus}" href="${applicationScope.urlStatus}/status?id=${requestScope.ubid}">
										${updateTitle}:${requestScope.ugcstatus}
									</a>
								</c:otherwise>
							</c:choose>     
						</div>
					</div>

				</div>
			</div>
			</section>
			</article>
		</div>
	</form>
</div>
