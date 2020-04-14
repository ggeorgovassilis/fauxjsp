<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:forEach var="news" items="${listOfNews}">${news.id},<c:out value="${news.headline}"/>,<% fauxjsp.test.webapp.dto.News item = (fauxjsp.test.webapp.dto.News)pageContext.getAttribute("news");
out.print(item.getDescription().toUpperCase());
%>,<%=((fauxjsp.test.webapp.dto.News)pageContext.getAttribute("news")).isImportant()%>
</c:forEach>