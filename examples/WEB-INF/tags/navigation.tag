<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ attribute name="navigation" required="true" rtexprvalue="true" type="java.util.List"%> 
<div class="navigation">
<c:forEach var="item" items="${navigation}">
<a class=part href="/fake-portlets/${item.path}"><c:out value="${item.label}"/></a>
</c:forEach>
<div class=logo>Awesome News Portal</div>
</div>