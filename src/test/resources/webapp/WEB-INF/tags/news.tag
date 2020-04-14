<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ attribute name="listOfNews" required="true" rtexprvalue="true" type="java.util.List"%> 
<div class="portlet newslist">
<label>News</label>
<c:forEach var="news" items="${listOfNews}">
<div class=news>
<a href="news?id=${news.id}" class=headline><c:if test="${news.important}">+++</c:if><c:out value="${news.headline}"/></a>
<div class=description><c:out value="${news.description}"/></div>
</div>
</c:forEach>
</div>