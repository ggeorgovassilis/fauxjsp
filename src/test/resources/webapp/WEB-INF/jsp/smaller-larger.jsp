<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:choose>
<c:when test="${value <= 0}">smaller-equals</c:when>
<c:when test="${value < 2}">smaller</c:when>
<c:when test="${value == 3}">equals</c:when>
<c:when test="${value > 5}">larger</c:when>
<c:when test="${value >= 4}">larger-equals</c:when>
</c:choose>
