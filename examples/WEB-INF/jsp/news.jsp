<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="az" tagdir="/WEB-INF/tags" %>
<az:structure>
<h2><c:if test="${news.important}">+++</c:if><c:if test="${not news.important}">_</c:if><c:out value="${news.headline}"/></h2>
<p class=description><c:out value="${news.description}"/></p>
<p class=fulltext><c:out value="${news.fulltext}"/></p>
</az:structure>