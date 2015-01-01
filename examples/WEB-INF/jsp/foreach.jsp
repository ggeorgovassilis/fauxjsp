<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
NEWS SECTION 1: <c:forEach var="news" items="${listOfNews}"><c:out value="${news.id}"/></c:forEach>
NEWS SECTION 2: <c:forEach var="news" begin="1" end="2" items="${listOfNews}"><c:out value="${news.id}"/></c:forEach>
NEWS SECTION 3: <c:forEach var="news" begin="0" end="2" step="3" items="${listOfNews}"><c:out value="${news.id}"/></c:forEach>
VARSTATUS: <c:forEach var="news" items="${listOfNews}" varStatus="vs"><c:out value="${vs.count}"/>,<c:out value="${vs.index}"/>,<c:out value="${vs.first}"/>,<c:out value="${vs.last}"/>
</c:forEach>