<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%
//---------------------------------------------------------------------------------------------------
//-- struts的select option 组件-/inc/strutsOptionBirthdayYearComponent.inc
//-- 外部jsp调用此inc可以传入2个参数:
//-- strutsOptionYearComponent_begin 必须的，最上面的的option的年份
//-- strutsOptionYearComponent_end 必须的，最下面的的option的年份 
//---------------------------------------------------------------------------------------------------
%>
<c:choose>
	<c:when test="${strutsOptionYearComponent_begin >= strutsOptionYearComponent_end}">
		<c:forEach begin="${strutsOptionYearComponent_end}" end="${strutsOptionYearComponent_begin}" varStatus="status">
			<option value="${strutsOptionYearComponent_begin-(status.index-strutsOptionYearComponent_end)}">${strutsOptionYearComponent_begin-(status.index-strutsOptionYearComponent_end)}</option>
			<c:if test="${(strutsOptionYearComponent_begin-(status.index-strutsOptionYearComponent_end)) == '1960'}" >
				<option value="1760">60后</option>
			</c:if>
			<c:if test="${(strutsOptionYearComponent_begin-(status.index-strutsOptionYearComponent_end)) == '1970'}" >
				<option value="1770">70后</option>
			</c:if>
			<c:if test="${(strutsOptionYearComponent_begin-(status.index-strutsOptionYearComponent_end)) == '1980'}" >
				<option value="1780">80后</option>
			</c:if>
			<c:if test="${(strutsOptionYearComponent_begin-(status.index-strutsOptionYearComponent_end)) == '1990'}" >
				<option value="1790">90后</option>
			</c:if>
		</c:forEach>
	</c:when>
	<c:otherwise>
		<c:forEach begin="${strutsOptionYearComponent_begin}" end="${strutsOptionYearComponent_end}" varStatus="status">
			<c:if test="${strutsOptionYearComponent_begin-(status.index-strutsOptionYearComponent_end) == '1960'}" >
				<option value="1760">60后</option>
			</c:if>
			<c:if test="${strutsOptionYearComponent_begin-(status.index-strutsOptionYearComponent_end) == '1970'}" >
				<option value="1770">70后</option>
			</c:if>
			<c:if test="${strutsOptionYearComponent_begin-(status.index-strutsOptionYearComponent_end) == '1980'}" >
				<option value="1780">80后</option>
			</c:if>
			<c:if test="${strutsOptionYearComponent_begin-(status.index-strutsOptionYearComponent_end) == '1990'}" >
				<option value="1790">90后</option>
			</c:if>
			<option value="${status.index}">${status.index}</option>
		</c:forEach>
	</c:otherwise>
</c:choose>