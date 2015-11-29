<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:forEach var="news" items="${listOfNews}">${news.id},<c:out value="${news.headline}"/>,<% some invalid code%>
</c:forEach>